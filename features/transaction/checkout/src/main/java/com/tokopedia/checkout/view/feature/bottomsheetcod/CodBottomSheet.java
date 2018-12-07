package com.tokopedia.checkout.view.feature.bottomsheetcod;

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
    public void initView(View view) {
        mCancel = view.findViewById(R.id.button_bottom_sheet_cod);
        mCancel.setOnClickListener(view1 -> dismiss());
    }

    @Override
    protected String title() {
        return getString(R.string.branding_cod);
    }
}
