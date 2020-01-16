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
    private Paint mStatusPaint;
    private Path mPath = new Path();
    private Boolean statusColor = false;

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

        mStatusPaint = new Paint();
        mStatusPaint.setStyle(Paint.Style.STROKE);
        mStatusPaint.setColor(Color.WHITE);
        mStatusPaint.setStrokeWidth(CONST_STROKE_WIDTH);
    }

    public void changeColor(){
        statusColor = true;
        pickColor();

        Thread successThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                statusColor = false;
                pickColor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        successThread.start();
    }

    private void pickColor() {
        if(!statusColor){
            mStatusPaint.setColor(Color.WHITE);
        }else{
            mStatusPaint.setColor(Color.GREEN);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        int width = getWidth();
        int height = getHeight();

        float radius;
        if(width < height)
            radius = width/2.7F;
        else
            radius = height/2.7F;

        mPath.addCircle(getRight() / 2,
                getBottom() / 3,
                radius,
                Path.Direction.CW);  //invisible circle
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        canvas.drawPath(mPath, mSemiBlackPaint);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.parseColor("#ae000000"));

        canvas.drawCircle(getRight() / 2,
                getBottom() / 3,
                radius,
                mStatusPaint);

    }
}
