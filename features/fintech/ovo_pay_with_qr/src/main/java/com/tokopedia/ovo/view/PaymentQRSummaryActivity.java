package com.tokopedia.ovo.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class PaymentQRSummaryActivity extends BaseSimpleActivity {
    public static final String QR_DATA = "QR_DATA";
    public static final String IMEI = "IMEI";

    @Override
    protected Fragment getNewFragment() {
        return PaymentQRSummaryFragment.createInstance(getIntent().getStringExtra(QR_DATA), getIntent().getStringExtra(IMEI));
    }
}
