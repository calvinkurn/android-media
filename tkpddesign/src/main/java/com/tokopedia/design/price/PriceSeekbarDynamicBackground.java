package com.tokopedia.design.price;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by henrypriyono on 8/15/17.
 */

public class PriceSeekbarDynamicBackground extends View {

    private float firstPointPercentage = 0;
    private float secondPointPercentage = 0;
    private Paint backPaint;
    private Paint frontPaint;
    private int foreColor;
    private int backColor;

    public PriceSeekbarDynamicBackground(Context context) {
        super(context);
        init();
    }

    public PriceSeekbarDynamicBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PriceSeekbarDynamicBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray
                = context.obtainStyledAttributes(attrs, R.styleable.PriceSeekbarDynamicBackground, 0, 0);

        foreColor = typedArray.getColor(R.styleable.PriceSeekbarDynamicBackground_foreColor, Color.GREEN);
        backColor = typedArray.getColor(R.styleable.PriceSeekbarDynamicBackground_backColor, Color.GRAY);
        typedArray.recycle();

        init();
    }

    private void init() {
        backPaint = new Paint();
        backPaint.setAntiAlias(false);
        backPaint.setColor(backColor);
        backPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        frontPaint = new Paint();
        frontPaint.setAntiAlias(false);
        frontPaint.setColor(foreColor);
        frontPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float canvasWidth = canvas.getWidth();
        float canvasHeight = canvas.getHeight();

        float firstPoint = canvasWidth * firstPointPercentage;
        float secondPoint = canvasWidth * secondPointPercentage;

        canvas.drawRect(0, 0, firstPoint, canvasHeight, backPaint);
        canvas.drawRect(firstPoint, 0, secondPoint, canvasHeight, frontPaint);
        canvas.drawRect(secondPoint, 0, canvasWidth, canvasHeight, backPaint);
    }

    public void setFirstPointPercentage(float firstPointPercentage) {
        this.firstPointPercentage = firstPointPercentage;
    }

    public void setSecondPointPercentage(float secondPointPercentage) {
        this.secondPointPercentage = secondPointPercentage;
    }
}
