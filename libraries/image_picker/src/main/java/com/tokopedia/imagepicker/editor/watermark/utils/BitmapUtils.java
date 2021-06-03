package com.tokopedia.imagepicker.editor.watermark.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.TypedValue;

import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkImage;
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkText;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.core.content.res.ResourcesCompat;
import timber.log.Timber;

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

    public static void saveAsPNG(Bitmap inputBitmap, String filePath, boolean withTime) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Timber.e("SD card is not available/writable right now.");
        }

        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(Calendar.getInstance().getTime());

        FileOutputStream out = null;
        try {
            if (withTime) {
                out = new FileOutputStream(filePath + timeStamp + ".png");
            } else {
                out = new FileOutputStream(filePath + "watermarked" + ".png");
            }
            inputBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            Timber.e(e.toString());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Timber.e(e.toString());
            }
        }
    }

}
