package com.tokopedia.design.bottomsheet;

import android.app.Service;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author okasurya on 2/13/18.
 */

public abstract class BaseBottomSheetView extends BottomSheetDialog {

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(View view);

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

        if (layoutInflater == null) {
            Log.e(this.getClass().getSimpleName(), "LayoutInflater NULL");
            return;
        }

        if (getLayoutId() == 0) {
            Log.e(this.getClass().getSimpleName(), "Layout Id NULL");
            return;
        }

        View bottomSheetView = layoutInflater.inflate(getLayoutId(), null);
        initView(bottomSheetView);
        setContentView(bottomSheetView);
    }
}
