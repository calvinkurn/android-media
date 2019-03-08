package com.tokopedia.ovo.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by nathan on 8/18/17.
 */

public class PaymentQRSummaryActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return PaymentQRSummaryFragment.createInstance(getIntent().getStringExtra("QR_DATA"), getIntent().getStringExtra("IMEI"));
    }
}
