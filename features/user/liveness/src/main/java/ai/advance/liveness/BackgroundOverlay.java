package ai.advance.liveness;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BackgroundOverlay extends View {

    private final static int CONST_STROKE_WIDTH = 10;

    private Paint mTransparentPaint;
    private Paint mSemiBlackPaint;
    private Paint mWhitePaint;
    private Path mPath = new Path();

    public BackgroundOverlay(Context context) {
        super(context);
        initPaints();
    }

    public BackgroundOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public BackgroundOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        mWhitePaint = new Paint();
        mWhitePaint.setStyle(Paint.Style.STROKE);
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setStrokeWidth(CONST_STROKE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        mPath.addCircle(getRight() / 2,
                getBottom() / 3,
                272,
                Path.Direction.CW);  //invisible circle
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.parseColor("#ae000000"));

        canvas.drawCircle(getRight() / 2,
                getBottom() / 3,
                275,
                mWhitePaint);
    }
}
