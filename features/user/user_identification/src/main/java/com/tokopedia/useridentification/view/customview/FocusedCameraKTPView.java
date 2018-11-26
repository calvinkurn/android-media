package com.tokopedia.useridentification.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author by alvinatin on 06/11/18.
 */

public class FocusedCameraKTPView extends View {

    private final static int LEFT_DIMEN_DIVIDER = 20;
    private final static double TOP_DIMEN_DIVIDER = 3.22;
    private final static int RIGHT_DIMEN_DIVIDER = 20;
    private final static double BOTTOM_DIMEN_DIVIDER = 2.96;
    private final static int CONST_RADIUS = 20;
    private final static int CONST_STROKE_WIDTH = 10;

    private Paint mTransparentPaint;
    private Paint mSemiBlackPaint;
    private Path mPath = new Path();

    public FocusedCameraKTPView(Context context) {
        super(context);
        initPaints();
    }

    public FocusedCameraKTPView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public FocusedCameraKTPView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }


    private void initPaints() {
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);
        mTransparentPaint.setStrokeWidth(CONST_STROKE_WIDTH);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.TRANSPARENT);
        mSemiBlackPaint.setStrokeWidth(CONST_STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mPath.addRect(getLeft() + (getRight() - getLeft()) / LEFT_DIMEN_DIVIDER,
                    (float) (getTop() + (getBottom() - getTop()) / TOP_DIMEN_DIVIDER),
                    getRight() - (getRight() - getLeft()) / RIGHT_DIMEN_DIVIDER,
                    (float) (getBottom() - (getBottom() - getTop()) / BOTTOM_DIMEN_DIVIDER),
                    Path.Direction.CW);
        } else {
            mPath.addRoundRect(
                    getLeft() + (getRight() - getLeft()) / LEFT_DIMEN_DIVIDER,
                    (float) (getTop() + (getBottom() - getTop()) / TOP_DIMEN_DIVIDER),
                    getRight() - (getRight() - getLeft()) / RIGHT_DIMEN_DIVIDER,
                    (float) (getBottom() - (getBottom() - getTop()) / BOTTOM_DIMEN_DIVIDER),
                    CONST_RADIUS,
                    CONST_RADIUS,
                    Path.Direction.CW
            );
        }
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRect(getLeft() + (getRight() - getLeft()) / LEFT_DIMEN_DIVIDER,
                    (float) (getTop() + (getBottom() - getTop()) / TOP_DIMEN_DIVIDER),
                    getRight() - (getRight() - getLeft()) / RIGHT_DIMEN_DIVIDER,
                    (float) (getBottom() - (getBottom() - getTop()) / BOTTOM_DIMEN_DIVIDER),
                    mTransparentPaint);
        } else {
            canvas.drawRoundRect(getLeft() + (getRight() - getLeft()) / LEFT_DIMEN_DIVIDER,
                    (float) (getTop() + (getBottom() - getTop()) / TOP_DIMEN_DIVIDER),
                    getRight() - (getRight() - getLeft()) / RIGHT_DIMEN_DIVIDER,
                    (float) (getBottom() - (getBottom() - getTop()) / BOTTOM_DIMEN_DIVIDER),
                    CONST_RADIUS,
                    CONST_RADIUS,
                    mTransparentPaint);
        }


        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.parseColor("#a642b549"));
    }
}
