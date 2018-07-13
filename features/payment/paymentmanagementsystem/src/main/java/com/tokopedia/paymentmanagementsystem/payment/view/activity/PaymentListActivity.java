package com.tokopedia.paymentmanagementsystem.payment.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.paymentmanagementsystem.payment.view.fragment.PaymentListFragment;

public class PaymentListActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return PaymentListFragment.createInstance();
    }

    public static Intent createIntent(Activity context) {
        return new Intent(context, PaymentListActivity.class);
    }
}
