package com.tokopedia.design.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CircleOverlayView extends View {

    public CircleOverlayView(Context context) {
        super(context);
    }

    public CircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        createWindowFrame(canvas);
    }

    protected void createWindowFrame(Canvas canvas) {

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());
        RectF innerRectangle = new RectF(getWidth()/2 - 300, getHeight()/2 - 700, getWidth()/2 + 300, getHeight()/2 );
        RectF ktpRectangle = new RectF(getWidth()/2 -200, getHeight()/2 + 100, getWidth()/2 + 200, getHeight()/2 + 300 );

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#e642b549"));
        paint.setAlpha(99);
        canvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawOval(innerRectangle, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawRoundRect(ktpRectangle,10,10,paint);
    }

}