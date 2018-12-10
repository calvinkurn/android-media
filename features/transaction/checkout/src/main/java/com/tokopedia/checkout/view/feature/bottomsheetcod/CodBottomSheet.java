package com.tokopedia.checkout.view.feature.bottomsheetcod;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.tokopedia.checkout.R;
import com.tokopedia.design.component.BottomSheets;

/**
 * Created by fajarnuha on 06/12/18.
 */
public class CodBottomSheet extends BottomSheets {

    private Button mCancel;

    @Override
    public int getLayoutResourceId() {
        return R.layout.bottom_sheet_cod_notification;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (getBottomSheetBehavior() != null)
            getBottomSheetBehavior().setPeekHeight(metrics.heightPixels / 2);
    }

    @Override
    public void initView(View view) {
        mCancel = view.findViewById(R.id.button_bottom_sheet_cod);
        mCancel.setOnClickListener(view1 -> dismiss());
    }

    @Override
    protected String title() {
        return getString(R.string.branding_cod);
    }
}
