package com.tokopedia.pms.proof.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSecureWindowFlag();
    }

    private void setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread(() -> {
                Window window = getWindow();
                if (window != null) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            });
        }
    }

    public static Intent createIntent(Context context, BasePaymentModel paymentListModel) {
        Intent intent = new Intent(context, UploadProofPaymentActivity.class);
        intent.putExtra(Constant.PAYMENT_LIST_MODEL_EXTRA, paymentListModel);
        return intent;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        BasePaymentModel paymentListModel = getIntent().getParcelableExtra(Constant.PAYMENT_LIST_MODEL_EXTRA);
        return UploadProofPaymentFragment.createInstance(paymentListModel);
    }
}
