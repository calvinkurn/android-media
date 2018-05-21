package com.tokopedia.digital.widget.view.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author anggaprasetiyo on 7/6/17.
 */

public class DigitalItemHolderLayout extends LinearLayout {
    public DigitalItemHolderLayout(Context context) {
        super(context);
    }

    public DigitalItemHolderLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DigitalItemHolderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * 5 / 4;
        setMeasuredDimension(width, height);
    }
}
