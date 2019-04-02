package com.tokopedia.tokocash.qrpayment.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class NominalQrPaymentActivity extends BaseSimpleActivity {

    private static final String INFO_QR = "info_qr";
    private static final String IDENTIFIER = "identifier";

    public static Intent newInstance(Context context, String identifier, InfoQrTokoCash infoQrTokoCash) {
        Intent intent = new Intent(context, NominalQrPaymentActivity.class);
        intent.putExtra(IDENTIFIER, identifier);
        intent.putExtra(INFO_QR, infoQrTokoCash);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
