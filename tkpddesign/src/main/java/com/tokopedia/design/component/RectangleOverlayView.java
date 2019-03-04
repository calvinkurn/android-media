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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

        RectF ktpRectangle_2 = new RectF(
                (getWidth() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_width))+20,
                (getHeight() / 2 - getResources().getDimensionPixelSize(R.dimen.ktp_view_height))+20,
                (getWidth() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_width))-20,
                (getHeight() / 2 + getResources().getDimensionPixelSize(R.dimen.ktp_view_height))-20);


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.black_99));

        paint.setAlpha(255);
        osCanvas.drawRect(mainRectangle, paint);

        osCanvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.img_frame_id)), null, ktpRectangle, null);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        osCanvas.drawRoundRect(ktpRectangle_2, 0, 0, paint);

    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        return bitmap;
    }

}