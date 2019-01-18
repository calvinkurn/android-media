package com.tokopedia.design.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tokopedia.design.R;

public class RectangleOverlayView extends LinearLayout {

    private Bitmap bitmap;

    public RectangleOverlayView(Context context) {
        super(context);
    }

    public RectangleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (bitmap == null) {
            createBitmap();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void createBitmap() {

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF mainRectangle = new RectF(0, 0, getWidth(), getHeight());

        RectF ktpRectangle = new RectF(
                getWidth() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_width),
                getHeight() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_height),
                getWidth() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_width),
                getHeight() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_height));


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.colorPrimary));

        paint.setAlpha(255);
        osCanvas.drawRect(mainRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        osCanvas.drawRoundRect(ktpRectangle, 10, 10, paint);

    }

}