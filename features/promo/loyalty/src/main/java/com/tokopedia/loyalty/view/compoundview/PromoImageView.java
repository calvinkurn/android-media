package com.tokopedia.loyalty.view.compoundview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author anggaprasetiyo on 08/01/18.
 */

public class PromoImageView extends androidx.appcompat.widget.AppCompatImageView {
    public PromoImageView(Context context) {
        super(context);
    }

    public PromoImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PromoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width * 328 / 600);
    }
}
