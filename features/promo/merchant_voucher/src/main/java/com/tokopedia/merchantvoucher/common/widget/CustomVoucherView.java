package com.tokopedia.merchantvoucher.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.tokopedia.merchantvoucher.R;

/*
    +---------------------+   +--------+
    |                     +-+-+        |
    |                       |          |
    |                       |          |
    |                     +-+-+        |
    +---------------------+   +--------+
 */
public class CustomVoucherView extends FrameLayout {

    private Path path;
    private Path linePath;
    private Paint borderPaint;
    private Paint dashPaint;
    private boolean requiresShapeUpdate = true;

    protected int cornerRadius;
    protected int mScallopRadius;
    protected float mScallopRelativePosition;
    protected int mShadowRadius;
    protected int mDashWidth;
    protected int mDashGap;
    protected int mDashColor;

    @Nullable
    protected Drawable drawable = null;

    public CustomVoucherView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomVoucherView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomVoucherView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        applyAttrs(context, attrs);

        setDrawingCacheEnabled(true);
        setWillNotDraw(false);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            setLayerType(LAYER_TYPE_SOFTWARE, borderPaint);
        } else {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void applyAttrs(Context context, AttributeSet attrs){
        if (attrs != null) {
            int defaultRadius = dpToPx(4f);
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomVoucherView);
            cornerRadius = attributes.getDimensionPixelSize(R.styleable.CustomVoucherView_vchCornerRadius, defaultRadius);
            mScallopRadius = attributes.getDimensionPixelSize(R.styleable.CustomVoucherView_vchScallopRadius, defaultRadius * 2);
            mScallopRelativePosition = attributes.getFloat(R.styleable.CustomVoucherView_vchScallopRelativePosition, 0.7f);
            if (attributes.hasValue(R.styleable.CustomVoucherView_vchElevation)) {
                mShadowRadius = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchElevation, 0);
            } else if (attributes.hasValue(R.styleable.CustomVoucherView_android_elevation)) {
                mShadowRadius = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_android_elevation, 0);
            }
            mDashWidth = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchDashWidth, defaultRadius);
            mDashGap = attributes.getDimensionPixelOffset(R.styleable.CustomVoucherView_vchDashGap, defaultRadius);
            mDashColor = attributes.getColor(R.styleable.CustomVoucherView_vchDashColor, getResources().getColor(android.R.color.black));
            attributes.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initVoucherPath();
        if (borderPaint == null) {
            borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(Color.WHITE);
            borderPaint.setStrokeWidth(1);
            borderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            borderPaint.setStrokeJoin(Paint.Join.BEVEL);
            borderPaint.setShadowLayer(mShadowRadius, 0, mShadowRadius / 2, Color.GRAY);
        }

        if (dashPaint == null) {
            dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            dashPaint.setStyle(Paint.Style.STROKE);
            dashPaint.setPathEffect(new DashPathEffect(new float[]{mDashGap, mDashGap}, 0));
            dashPaint.setStrokeWidth(mDashWidth);
            dashPaint.setColor(mDashColor);
        }

        if (path !=null) {
            canvas.drawPath(path, borderPaint);
        }
        if (linePath!= null) {
            canvas.drawPath(linePath, dashPaint);
        }
        super.onDraw(canvas);
    }

    private void initVoucherPath(){
        if (requiresShapeUpdate && getWidth() > 0) {
            if (path == null) {
                path = new Path();
            } else {
                path.reset();
            }
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            int minX = getPaddingLeft();
            int maxX = canvasWidth - getPaddingRight();
            int minY = getPaddingTop();
            int maxY = canvasHeight - getPaddingBottom();
            int objectWidth = maxX - minX;

            float scallopXCenterPos = mScallopRelativePosition * objectWidth + minX;

            path.moveTo(minX + cornerRadius, minY);
            path.lineTo(scallopXCenterPos - mScallopRadius, minY);
            path.arcTo(new RectF(scallopXCenterPos - mScallopRadius,
                            minY - mScallopRadius, scallopXCenterPos + mScallopRadius,
                            minY + mScallopRadius),
                    180f, -180f);
            path.lineTo(maxX - cornerRadius, minY);
            path.arcTo(new RectF(maxX - cornerRadius * 2f, minY, maxX, minY + cornerRadius * 2f), -90, 90);
            path.lineTo(maxX, maxY - cornerRadius);
            path.arcTo(new RectF(maxX - cornerRadius * 2f, maxY - cornerRadius * 2f, maxX, maxY), 0, 90);
            path.lineTo(scallopXCenterPos + mScallopRadius, maxY);
            path.arcTo(new RectF(scallopXCenterPos - mScallopRadius,
                            maxY - mScallopRadius, scallopXCenterPos + mScallopRadius,
                            maxY + mScallopRadius),
                    0f, -180f);
            path.lineTo(scallopXCenterPos - mScallopRadius, maxY);
            path.lineTo(minX + cornerRadius, maxY);
            path.arcTo(new RectF(minX, maxY - cornerRadius * 2f, minX + cornerRadius * 2f, maxY), 90, 90);
            path.lineTo(minX, minY + cornerRadius);
            path.arcTo(new RectF(minX, minY, minX + cornerRadius * 2f, minY + cornerRadius * 2f), 180, 90);
            path.close();

            if (linePath == null) {
                linePath = new Path();
            } else {
                linePath.reset();
            }
            linePath.moveTo(scallopXCenterPos , minY + mScallopRadius);
            linePath.lineTo(scallopXCenterPos , maxY - mScallopRadius);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            this.requiresShapeUpdate = true;
            postInvalidate();
        }
    }

    protected int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }


}
