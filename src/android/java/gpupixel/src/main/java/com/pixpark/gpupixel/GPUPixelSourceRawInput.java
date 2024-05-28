/*
 * GPUPixel
 *
 * Created by PixPark on 2021/6/24.
 * Copyright © 2021 PixPark. All rights reserved.
 */

package com.pixpark.gpupixel;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class GPUPixelSourceRawInput extends GPUPixelSource {
    private Object object_this;
    private GPUPixel.GPUPixelLandmarkCallback landmarkCallback;

    private SurfaceTexture mSurfaceTexture = null;
    public GPUPixelSourceRawInput() {
        object_this = this;

        if (mNativeClassID != 0) return;
        GPUPixel.getInstance().runOnDraw(new Runnable() {
            @Override
            public void run() {
                mNativeClassID = GPUPixel.nativeSourceRawInputNew();
            }
        });
    }

    public void addTargetFilter(final long targetId) {
        GPUPixel.getInstance().runOnDraw(new Runnable() {
            @Override
            public void run() {
                GPUPixel.nativeSourceAddFilter(targetId);
            }
        });
    }

    public void SetRotation(int rotation)
    {
        GPUPixel.nativeSourceRawInputSetRotation(mNativeClassID, rotation);
    }

    public void uploadBytes(final int[] pixels, int width, int height, int stride) {
        GPUPixel.nativeSourceRawInputUploadBytes(mNativeClassID, pixels, width, height, stride);
        proceed(true, false);
    }

    public void uploadByteBuffer(final ByteBuffer pixels, int width, int height, int stride) {
        GPUPixel.nativeWaterbusUploadBytes(mNativeClassID, pixels, width, height, stride);
        proceed(true, false);
    }

    public void setLandmarkCallbck(GPUPixel.GPUPixelLandmarkCallback filter) {
        landmarkCallback = filter;

        GPUPixel.getInstance().runOnDraw(new Runnable() {
            @Override
            public void run() {
                GPUPixel.nativeSetLandmarkCallbackRawInput(object_this, mNativeClassID);
            }
        });
    }

    // callback by native
    public void onFaceLandmark(float[] landmarks) {
        if(landmarkCallback != null) {
            landmarkCallback.onFaceLandmark(landmarks);
        }
    }
}
