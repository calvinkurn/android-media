package com.tokopedia.pms.bankaccount.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.payment.view.model.PaymentListModel;
import com.tokopedia.config.GlobalConfig;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeBankAccountActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, PaymentListModel paymentListModel){
        Intent intent = new Intent(context, ChangeBankAccountActivity.class);
        intent.putExtra(Constant.PAYMENT_LIST_MODEL_EXTRA, paymentListModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSecureWindowFlag();
    }

    private void setSecureWindowFlag() {
        if(GlobalConfig.APPLICATION_TYPE==GlobalConfig.CONSUMER_APPLICATION||GlobalConfig.APPLICATION_TYPE==GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread(() -> {
                Window window = getWindow();
                if (window != null) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            });
        }
    }


    @Override
    protected Fragment getNewFragment() {
        PaymentListModel paymentListModel = getIntent().getParcelableExtra(Constant.PAYMENT_LIST_MODEL_EXTRA);
        return ChangeBankAccountFragment.createInstance(paymentListModel);
    }
}
