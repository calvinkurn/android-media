package com.tokopedia.tokopoints.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokopoints.R;

public class RoundButton extends AppCompatButton {

    float cornerRadius = 0;
    int buttonColor = 0;

    Path clipPath = new Path();
    RectF clipRectF = new RectF();
    Paint clipPaint = new Paint();

    public RoundButton(Context context) {
        super(context);
        init(null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        readAttributes(attributeSet);
    }

    private void readAttributes(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray array = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.RoundButton, 0, 0);
            cornerRadius = array.getDimension(R.styleable.RoundButton_tpRbCornerRadius, 0);
            buttonColor = array.getColor(R.styleable.RoundButton_tpRbButtonColor, MethodChecker.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundColor(MethodChecker.getColor(getContext(), com.tokopedia.design.R.color.transparent));
        drawRoundBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawRoundBackground(Canvas canvas) {
        if (canvas != null) {
            clipPath.reset();

            clipRectF.top = 0f;
            clipRectF.left = 0f;
            clipRectF.right = canvas.getWidth();
            clipRectF.bottom = canvas.getHeight();
            clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW);
            clipPaint.setStyle(Paint.Style.FILL);
            clipPaint.setColor(buttonColor);

            canvas.clipPath(clipPath);
            canvas.drawPaint(clipPaint);
        }
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor = buttonColor;
        invalidate();
        requestLayout();
    }
}
