package com.tokopedia.digital.newcart.presentation.fragment;

import android.app.Activity;

import com.tokopedia.design.component.Dialog;
import com.tokopedia.digital.R;

public class DigitalPostPaidDialog extends Dialog {

    public DigitalPostPaidDialog(Activity context, Type type) {
        super(context, type);
    }

    @Override
    public int layoutResId() {
        return R.layout.dialog_digital_post_paid;
    }
}
