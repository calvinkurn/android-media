package com.tokopedia.shop.settings.common.widget;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

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

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View bottomSheetView = layoutInflater.inflate(getLayoutId(), null);
        initView(bottomSheetView);
        setContentView(bottomSheetView);
    }
}
