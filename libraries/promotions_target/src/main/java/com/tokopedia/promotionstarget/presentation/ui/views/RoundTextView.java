package com.tokopedia.promotionstarget.presentation.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.tokopedia.promotionstarget.R;

import static com.tokopedia.promotionstarget.presentation.ExtensionsKt.getColor;

public class RoundTextView extends AppCompatTextView {

    float cornerRadius = 0;
    int bgColor = 0;

    Path clipPath = new Path();
    RectF clipRectF = new RectF();
    Paint clipPaint = new Paint();

    public RoundTextView(Context context) {
        super(context);
        init(null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        readAttributes(attributeSet);
    }

    private void readAttributes(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray array = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.RoundTextView, 0, 0);
            cornerRadius = array.getDimension(R.styleable.RoundTextView_rtv_cornerRadius, 0);
            bgColor = array.getColor(R.styleable.RoundTextView_rtv_buttonColor, getColor(getContext(), R.color.white));
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundColor(getColor(getContext(), R.color.transparent));
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
            clipPaint.setColor(bgColor);

            canvas.clipPath(clipPath);
            canvas.drawPaint(clipPaint);
        }
    }

    public void setButtonColor(int buttonColor) {
        this.bgColor = buttonColor;
        invalidate();
        requestLayout();
    }
}
