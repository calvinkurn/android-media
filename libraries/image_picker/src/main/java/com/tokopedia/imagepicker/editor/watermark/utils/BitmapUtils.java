package com.tokopedia.imagepicker.editor.watermark.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;

import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkText;

import androidx.core.content.res.ResourcesCompat;

public class BitmapUtils {

    public static Bitmap textAsBitmap(Context context, WatermarkText watermarkText) {
        TextPaint watermarkPaint = new TextPaint();
        watermarkPaint.setColor(watermarkText.getTextShadowColor());
        watermarkPaint.setStyle(watermarkText.getStyle());

        if (watermarkText.getAlpha() >= 0 && watermarkText.getAlpha() <= 255) {
            watermarkPaint.setAlpha(watermarkText.getAlpha());
        }

        float value = (float) watermarkText.getSize();
        int pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics());
        watermarkPaint.setTextSize(pixel);

        if (watermarkText.getTextShadowBlurRadius() != 0
                || watermarkText.getTextShadowXOffset() != 0
                || watermarkText.getTextShadowYOffset() != 0) {
            watermarkPaint.setShadowLayer(watermarkText.getTextShadowBlurRadius(),
                    watermarkText.getTextShadowXOffset(),
                    watermarkText.getTextShadowYOffset(),
                    watermarkText.getTextShadowColor());
        }

        if (watermarkText.getTypeFaceId() != 0) {
            Typeface typeface = ResourcesCompat.getFont(context, watermarkText.getTypeFaceId());
            watermarkPaint.setTypeface(typeface);
        }

        watermarkPaint.setAntiAlias(true);
        watermarkPaint.setTextAlign(Paint.Align.LEFT);
        watermarkPaint.setStrokeWidth(5);

        float baseline = (int) (-watermarkPaint.ascent() + 1f);
        Rect bounds = new Rect();
        watermarkPaint.getTextBounds(watermarkText.getText(),
                0, watermarkText.getText().length(), bounds);

        int boundWidth = bounds.width() + 20;
        int mTextMaxWidth = (int) watermarkPaint.measureText(watermarkText.getText());
        if (boundWidth > mTextMaxWidth) {
            boundWidth = mTextMaxWidth;
        }
        StaticLayout staticLayout = new StaticLayout(watermarkText.getText(),
                0, watermarkText.getText().length(),
                watermarkPaint, mTextMaxWidth, android.text.Layout.Alignment.ALIGN_NORMAL, 2.0f,
                2.0f, false);

        int lineCount = staticLayout.getLineCount();
        int height = (int) (baseline + watermarkPaint.descent() + 3) * lineCount;
        Bitmap image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        if (boundWidth > 0 && height > 0) {
            image = Bitmap.createBitmap(boundWidth, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(image);
        canvas.drawColor(watermarkText.getBackgroundColor());
        staticLayout.draw(canvas);
        return image;
    }

    public static Bitmap resizeBitmap(Bitmap watermarkImg, float size, Bitmap backgroundImg) {
        int bitmapWidth = watermarkImg.getWidth();
        int bitmapHeight = watermarkImg.getHeight();
        float scale = (backgroundImg.getWidth() * size) / bitmapWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(watermarkImg, 0, 0,
                bitmapWidth, bitmapHeight, matrix, true);
    }

    public static Bitmap resizeBitmap(Bitmap inputBitmap, int maxImageSize) {
        float ratio = Math.min(
                (float) maxImageSize / inputBitmap.getWidth(),
                (float) maxImageSize / inputBitmap.getHeight());
        int width = Math.round(ratio * inputBitmap.getWidth());
        int height = Math.round(ratio * inputBitmap.getHeight());

        return Bitmap.createScaledBitmap(inputBitmap, width,
                height, true);
    }

}
