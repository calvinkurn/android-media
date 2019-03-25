package com.tokopedia.collapsing.tab.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.collapsing.tab.layout.R;

public class ExpandingLineView extends View {

    private Paint paint;
    private int paintColor;
    private float fraction = 0f;

    public ExpandingLineView(Context context) {
        super(context);
        init();
    }

    public ExpandingLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandingLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandingLineView);
        try {
            paintColor = styledAttributes.getColor(R.styleable.ExpandingLineView_lineColor, Color.WHITE);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(paintColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect bounds = canvas.getClipBounds();
        canvas.drawRect(bounds.left,
                bounds.top,
                bounds.left + fraction * bounds.width(),
                bounds.bottom,
                paint
        );
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }
}
