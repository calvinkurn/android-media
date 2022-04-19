package com.tokopedia.home_account.account_settings.presentation.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

public class TagRoundedSpan extends ReplacementSpan {

    private Context context;

    private float cornerRadius;
    private float leftPadding;
    private float rightPadding;
    private int backgroundColor;
    private int textColor;

    public TagRoundedSpan(
            Context context,
            int cornerRadius,
            @ColorRes int backgroundColor,
            @ColorRes int textColor
    ) {
        super();
        this.context = context;
        this.backgroundColor = ContextCompat.getColor(context, backgroundColor);
        this.textColor = ContextCompat.getColor(context, textColor);
        this.cornerRadius = toPixel(cornerRadius);
        this.leftPadding = toPixel(5f);
        this.rightPadding = toPixel(5f);
    }

    @Override
    public int getSize(@NotNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    @Override
    public void draw(
            @NotNull Canvas canvas,
            CharSequence text,
            int start,
            int end,
            float x,
            int top,
            int y,
            int bottom,
            @NotNull Paint paint
    ) {
        float emptySpace = (bottom - y) / 2f;
        float bgTop = top + emptySpace;
        float bgBottom = bottom - emptySpace;
        float bgRight = x + measureText(paint, text, start, end) + rightPadding + leftPadding;
        float yText = y - emptySpace;
        float xText = x + leftPadding;
        RectF rect = new RectF(x, bgTop, bgRight, bgBottom);

        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        paint.setColor(textColor);
        canvas.drawText(text, start, end, xText, yText, paint);
    }

    private float toPixel(float dip) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                context.getResources().getDisplayMetrics()
        );
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}
