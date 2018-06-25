package com.tokopedia.paymentmanagementsystem.paymentlist.view.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.paymentmanagementsystem.R;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.fragment.PaymentListFragment;

public class PaymentListActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return PaymentListFragment.createInstance();
    }
}
