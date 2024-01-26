package com.tokopedia.imagepreview.touch_view_pager_lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.tokopedia.imagepreview.R;

/**
 * Created by Rizky on 07/08/18.
 */
public class ImagePreviewTouchViewPager extends ViewPager {

    //private View currentView;
    private boolean AllowPageSwitching = true;
    private boolean isSmoothScroll = true;
    private boolean useDefaultMeasurement = false;

    public ImagePreviewTouchViewPager(Context context) {
        super(context);
    }

    public ImagePreviewTouchViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ImagePreviewTouchViewPager);
        try {
            AllowPageSwitching = styledAttributes.getBoolean(R.styleable.ImagePreviewTouchViewPager_can_swipe, true);
            useDefaultMeasurement = styledAttributes.getBoolean(R.styleable.ImagePreviewTouchViewPager_default_measure, false);
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
                childView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = childView.getMeasuredHeight();
                if (h > height) height = h;
            }

            if (height != 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
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
                try {
                    return super.onInterceptTouchEvent(arg0);
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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