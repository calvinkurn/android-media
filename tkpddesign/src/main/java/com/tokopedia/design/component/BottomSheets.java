package com.tokopedia.design.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.design.base.BaseBottomSheetView;

/**
 * Created by meta on 15/03/18.
 */

public class BottomSheets extends BaseBottomSheetView {

    public enum Type {
        NORMAL,
        FULL
    }

    public BottomSheets(@NonNull Context context) {
        super(context);
    }

    public BottomSheets(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public BottomSheets(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView(View view) {

    }

    public void setType(Type type) {

    }
}
