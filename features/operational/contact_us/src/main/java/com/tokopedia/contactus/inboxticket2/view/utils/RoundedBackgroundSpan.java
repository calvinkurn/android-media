package com.tokopedia.contactus.inboxticket2.view.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

public class RoundedBackgroundSpan extends ReplacementSpan {

    private static final int CORNER_RADIUS = 15;

    private static float PADDING_X;
    private static float PADDING_Y;

    private static float MAGIC_NUMBER;

    private int mBackgroundColor;
    private int mTextColor;
    private float mTextSize;
    private float textHeightWrap;

    /**
     * @param backgroundColor color value, not res id
     * @param textSize        in pixels
     */
    RoundedBackgroundSpan(int backgroundColor, int textColor, float textSize, float paddingx, float paddingy, float magicnumer, float textheightwrap) {
        mBackgroundColor = backgroundColor;
        mTextColor = textColor;
        mTextSize = textSize;
        PADDING_X = paddingx;
        PADDING_Y = paddingy;
        MAGIC_NUMBER = magicnumer;
        textHeightWrap = textheightwrap;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint = new Paint(paint); // make a copy for not editing the referenced paint

        paint.setTextSize(mTextSize);

        // Draw the rounded background
        paint.setColor(mBackgroundColor);
        float tagBottom = top + textHeightWrap + PADDING_Y + mTextSize + PADDING_Y + textHeightWrap;
        float tagRight = x + getTagWidth(text, start, end, paint);
        RectF rect = new RectF(x, top, tagRight, tagBottom);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);

        // Draw the text
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x + PADDING_X, tagBottom - PADDING_Y - textHeightWrap - MAGIC_NUMBER, paint);
    }

    private int getTagWidth(CharSequence text, int start, int end, Paint paint) {
        return Math.round(PADDING_X + paint.measureText(text.subSequence(start, end).toString()) + PADDING_X);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint = new Paint(paint); // make a copy for not editing the referenced paint
        paint.setTextSize(mTextSize);
        return getTagWidth(text, start, end, paint);
    }
}