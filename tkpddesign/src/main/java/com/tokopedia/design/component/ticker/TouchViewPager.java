package com.tokopedia.design.component.ticker;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tokopedia.design.R;

/**
 * Created by Rizky on 07/08/18.
 */
public class TouchViewPager extends ViewPager {

    //private View currentView;
    private boolean AllowPageSwitching = true;
    private boolean isSmoothScroll = true;
    private boolean useDefaultMeasurement = false;

    public TouchViewPager(Context context) {
        super(context);
    }

    public TouchViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TouchViewPager);
        try {
            AllowPageSwitching = styledAttributes.getBoolean(R.styleable.TouchViewPager_can_swipe, true);
            useDefaultMeasurement = styledAttributes.getBoolean(R.styleable.TouchViewPager_default_measure, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    public void SetAllowPageSwitching(boolean permission){
        AllowPageSwitching = permission;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (useDefaultMeasurement) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int height = 0;
            View childView = null;

            for (int i = 0; i < getChildCount(); ++i) {
                childView = getChildAt(i);
                childView.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                int h = childView.getMeasuredHeight();
                if (h > height) height = h;
            }

            if (height != 0) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, childView));
        }
    }

    /**
     * Determines the height of this view
     * https://stackoverflow.com/questions/30884837/viewpager-not-working-if-set-layout-height-wrap-content
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(AllowPageSwitching) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            } else {
                return super.onInterceptTouchEvent(arg0);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if(AllowPageSwitching) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            } else {
                return super.onTouchEvent(arg0);
            }
        } else {
            return false;
        }
    }

    public void setSmoothScroll(boolean smoothScroll) {
        isSmoothScroll = smoothScroll;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, isSmoothScroll);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, isSmoothScroll);
    }

}