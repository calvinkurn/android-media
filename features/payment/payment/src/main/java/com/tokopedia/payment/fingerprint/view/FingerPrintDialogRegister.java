package com.tokopedia.payment.fingerprint.view;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.payment.R;

/**
 * Created by zulfikarrahman on 3/26/18.
 */

public class FingerPrintDialogRegister extends BottomSheets {

    private TextView descFingerprint;

    @Override
    public int getLayoutResourceId() {
        return R.layout.partial_bottom_sheet_fingerprint_view;
    }

    @Override
    public void initView(View view) {
        descFingerprint = view.findViewById(R.id.desc_fingerprint);
    }

    public void updateTitle(String title){
        TextView textViewTitle = getView().findViewById(com.tokopedia.design.R.id.tv_title);
        textViewTitle.setText(title);
    }
}
