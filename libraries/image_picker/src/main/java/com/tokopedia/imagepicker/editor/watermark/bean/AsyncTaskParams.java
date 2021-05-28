package com.tokopedia.imagepicker.editor.watermark.bean;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * This is a simple class that can help we put multiple primitive
 * parameters into the task.
 *
 * @author huangyz0918 (huangyz0918@gmail.com)
 */
public class AsyncTaskParams {
    private Bitmap backgroundImg;
    private WatermarkText watermarkText;
    private Bitmap watermarkImg;
    private Context context;

    public AsyncTaskParams(Context context, Bitmap backgroundImg, WatermarkText watermarkText, Bitmap watermarkImg) {
        this.backgroundImg = backgroundImg;
        this.watermarkText = watermarkText;
        this.watermarkImg = watermarkImg;
    }

    public AsyncTaskParams(Context context, Bitmap backgroundImg, Bitmap watermarkImg) {
        this.backgroundImg = backgroundImg;
        this.watermarkImg = watermarkImg;
    }

    public AsyncTaskParams(Context context, Bitmap backgroundImg, WatermarkText watermarkText) {
        this.backgroundImg = backgroundImg;
        this.watermarkText = watermarkText;
    }

    /**
     * Getters and Setters for {@link AsyncTaskParams}.
     */
    public Bitmap getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(Bitmap backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public WatermarkText getWatermarkText() {
        return watermarkText;
    }

    public void setWatermarkText(WatermarkText watermarkText) {
        this.watermarkText = watermarkText;
    }

    public Bitmap getWatermarkImg() {
        return watermarkImg;
    }

    public void setWatermarkImg(Bitmap watermarkImg) {
        this.watermarkImg = watermarkImg;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}