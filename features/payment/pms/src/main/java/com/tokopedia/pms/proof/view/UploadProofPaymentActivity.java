package com.tokopedia.pms.proof.view;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.payment.view.model.PaymentListModel;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, PaymentListModel paymentListModel){
        Intent intent = new Intent(context, UploadProofPaymentActivity.class);
        intent.putExtra(Constant.PAYMENT_LIST_MODEL_EXTRA , paymentListModel);
        return intent;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        PaymentListModel paymentListModel = getIntent().getParcelableExtra(Constant.PAYMENT_LIST_MODEL_EXTRA);
        return UploadProofPaymentFragment.createInstance(paymentListModel);
    }
}
