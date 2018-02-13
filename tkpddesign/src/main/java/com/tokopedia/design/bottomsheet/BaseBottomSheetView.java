package com.tokopedia.design.bottomsheet;

import android.app.Service;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author okasurya on 2/13/18.
 */

public abstract class BaseBottomSheetView extends BottomSheetDialog {
    public BaseBottomSheetView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseBottomSheetView(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected BaseBottomSheetView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View bottomSheetView = layoutInflater.inflate(getLayoutId(), null);
        setContentView(bottomSheetView);
        initView();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * Initialize your view here e.g: findViewById etc...
     */
    protected abstract void initView();
}
