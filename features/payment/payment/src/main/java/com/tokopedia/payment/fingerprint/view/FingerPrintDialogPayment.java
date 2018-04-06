package com.tokopedia.payment.fingerprint.view;

import android.view.View;
import android.widget.Button;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.payment.R;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class FingerPrintDialogPayment extends FingerPrintDialog {
    private View containerOtp;
    private Button buttonUseOtp;
    private View.OnClickListener listenerButtonOtp;

    @Override
    public int getLayoutResourceId() {
        return R.layout.partial_bottom_sheet_fingerprint_view_payment;
    }

    @Override
    protected String title() {
        return getString(R.string.fingerprint_label_scan_your_fingerprint);
    }

    @Override
    public void initView(View view) {
        containerOtp = view.findViewById(R.id.container_otp);
        buttonUseOtp = view.findViewById(R.id.button_use_otp);
        buttonUseOtp.setOnClickListener(listenerButtonOtp);
    }

    public void setVisibilityContainer(boolean isVisible){
        if(isVisible){
            containerOtp.setVisibility(View.VISIBLE);
        }else{
            containerOtp.setVisibility(View.GONE);
        }
        updateHeight();
    }

    public void setClickListenerButtonOtp(View.OnClickListener listenerButtonOtp){
        this.listenerButtonOtp = listenerButtonOtp;
    }
}
