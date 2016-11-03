package com.tokopedia.tkpd.recharge.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Created by Tokopedia 05 on 7/11/2016.
 */
public class WrapContentViewPager extends ViewPager {
    private View currentView;

    public WrapContentViewPager(Context context) {
        super(context);
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (currentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        currentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = currentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void measureCurrentView(View currentView) {
        this.currentView = currentView;
        requestLayout();
    }
}
