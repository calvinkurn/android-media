package com.tokopedia.filter.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tokopedia.design.R;

public class ColorSampleView extends View {

    private static final float OUTLINE_WIDTH = 2;
    private Paint fillPaint;
    private Paint outlinePaint;

    public ColorSampleView(Context context) {
        super(context);
        init(context);
    }

    public ColorSampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorSampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int defaultFillColor = context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N50);
        fillPaint = new Paint();
        fillPaint.setAntiAlias(false);
        fillPaint.setColor(defaultFillColor);
        fillPaint.setStyle(Paint.Style.FILL);

        int outlineColor = context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100);
        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(false);
        outlinePaint.setColor(outlineColor);
        outlinePaint.setStrokeWidth(OUTLINE_WIDTH);
        outlinePaint.setStyle(Paint.Style.STROKE);
    }

    public void setColor(int color) {
        fillPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = (float) canvas.getWidth() / 2;
        float cy = (float) canvas.getHeight() / 2;
        float radius = (float) canvas.getWidth() / 2 - OUTLINE_WIDTH;

        canvas.drawCircle(cx, cy, radius, fillPaint);
        canvas.drawCircle(cx, cy, radius, outlinePaint);
    }
}
