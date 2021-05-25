package com.tokopedia.tokomart.common.view.badge;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BadgeContainer extends ViewGroup {

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        if(!(getParent() instanceof RelativeLayout)){
            super.dispatchRestoreInstanceState(container);
        }
    }

    public BadgeContainer(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View targetView = null, badgeView = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!(child instanceof BadgeView)) {
                targetView = child;
            } else {
                badgeView = child;
            }
        }
        if (targetView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            targetView.measure(widthMeasureSpec, heightMeasureSpec);
            if (badgeView != null) {
                badgeView.measure(MeasureSpec.makeMeasureSpec(targetView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(targetView.getMeasuredHeight(), MeasureSpec.EXACTLY));
            }
            setMeasuredDimension(targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
        }
    }

    public ImageView getImageViewFromContainer() {
        for(int index=0; index<((ViewGroup)this).getChildCount(); ++index) {
            View nextChild = ((ViewGroup)this).getChildAt(index);
            if (nextChild instanceof ImageView) {
                return (ImageView) nextChild;
            }
        }
        return null;
    }
}