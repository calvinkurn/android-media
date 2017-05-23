package com.tokopedia.core.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Square image view, will always follow its height
 */
public class SquareHFrameLayout extends FrameLayout {

    public SquareHFrameLayout(Context context) {
        super(context);
    }

    public SquareHFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareHFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
