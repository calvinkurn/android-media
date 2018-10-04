package com.tokopedia.topads.dashboard.view.helper;

import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

/**
 * @author normansyahputa on 2/13/17.
 */

public class BottomSheetHelper {
    private final View peakView;
    private int actionBarSize;
    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback;
    private int height;
    private boolean isShown;

    public BottomSheetHelper(BottomSheetBehavior mBottomSheetBehavior,
                             View viewById, int actionBarSize) {
        this.mBottomSheetBehavior = mBottomSheetBehavior;

        //Let's peek it, programmatically
        peakView = viewById;
        this.actionBarSize = actionBarSize;
    }

    public void showBottomSheet() {
        isShown = true;
        mBottomSheetBehavior.setPeekHeight((int) (((View)peakView.getParent()).getHeight() + actionBarSize));
        peakView.requestLayout();
    }

    public void dismissBottomSheet() {
        isShown = false;
        mBottomSheetBehavior.setPeekHeight(0);
        peakView.requestLayout();
    }

    public boolean isShown() {
        return isShown;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback bottomSheetCallback) {
        this.bottomSheetCallback = bottomSheetCallback;

        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);
        }
    }

    public void collapse() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void expanded() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public int getState() {
        return mBottomSheetBehavior.getState();
    }
}
