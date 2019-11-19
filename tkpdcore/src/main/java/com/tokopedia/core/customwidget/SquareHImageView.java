package com.tokopedia.core.customwidget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Square image view, will always follow its height
 */
public class SquareHImageView extends AppCompatImageView {

    public SquareHImageView(Context context) {
        super(context);
    }

    public SquareHImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareHImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
