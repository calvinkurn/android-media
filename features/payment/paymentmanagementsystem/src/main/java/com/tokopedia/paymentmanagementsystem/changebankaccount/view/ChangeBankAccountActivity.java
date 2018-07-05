package com.tokopedia.paymentmanagementsystem.changebankaccount.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.paymentmanagementsystem.changeclickbca.view.ChangeClickBcaActivity;
import com.tokopedia.paymentmanagementsystem.common.Constant;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.model.PaymentListModel;

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
    protected Fragment getNewFragment() {
        PaymentListModel paymentListModel = getIntent().getParcelableExtra(Constant.PAYMENT_LIST_MODEL_EXTRA);
        return ChangeBankAccountFragment.createInstance(paymentListModel);
    }
}
