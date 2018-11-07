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
        mTransparentPaint.setStrokeWidth(10);

        mSemiBlackPaint = new Paint();
        mSemiBlackPaint.setColor(Color.TRANSPARENT);
        mSemiBlackPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

//        mPath.addCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 550, Path.Direction.CW);
        if (Build.VERSION.SDK_INT < 21) {
            mPath.addRect(getLeft() + (getRight() - getLeft()) / 20,
                    (float) (getTop() + (getBottom() - getTop()) / 2.84),
                    getRight() - (getRight() - getLeft()) / 20,
                    (float) (getBottom() - (getBottom() - getTop()) / 3.38),
                    Path.Direction.CW);
        } else {
            mPath.addRoundRect(
                    getLeft() + (getRight() - getLeft()) / 20,
                    (float) (getTop() + (getBottom() - getTop()) / 2.84),
                    getRight() - (getRight() - getLeft()) / 20,
                    (float) (getBottom() - (getBottom() - getTop()) / 3.38),
                    20,
                    20,
                    Path.Direction.CW
            );
        }
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

//        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 550, mTransparentPaint);
        if (Build.VERSION.SDK_INT < 21) {
            canvas.drawRect(getLeft() + (getRight() - getLeft()) / 20,
                    (float) (getTop() + (getBottom() - getTop()) / 2.84),
                    getRight() - (getRight() - getLeft()) / 20,
                    (float) (getBottom() - (getBottom() - getTop()) / 3.38),
                    mTransparentPaint);
        } else {
            canvas.drawRoundRect(getLeft() + (getRight() - getLeft()) / 20,
                    (float) (getTop() + (getBottom() - getTop()) / 2.84),
                    getRight() - (getRight() - getLeft()) / 20,
                    (float) (getBottom() - (getBottom() - getTop()) / 3.38),
                    20,
                    20,
                    mTransparentPaint);
        }


        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.parseColor("#a642b549"));
    }
}
