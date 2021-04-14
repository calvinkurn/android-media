package com.tokopedia.shop.common.view;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by nathan on 6/2/17.
 */

public class ShopPageBaseCustomView extends FrameLayout {

    public ShopPageBaseCustomView(@NonNull Context context) {
        super(context);
    }

    public ShopPageBaseCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopPageBaseCustomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        ShopPageSavedState ss = new ShopPageSavedState(superState);
        ss.initChildrenStates();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.getChildrenStates());
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof ShopPageSavedState) {
            ShopPageSavedState ss = (ShopPageSavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).restoreHierarchyState(ss.getChildrenStates());
            }
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
