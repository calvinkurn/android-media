package com.tokopedia.design.menu;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.design.bottomsheet.BaseBottomSheetView;

/**
 * @author okasurya on 2/13/18.
 */

public class Menus extends BaseBottomSheetView {
    public Menus(@NonNull Context context) {
        super(context);
    }

    public Menus(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected Menus(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }
}
