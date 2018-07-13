package com.tokopedia.paymentmanagementsystem.bankdestination.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.paymentmanagementsystem.bankdestination.view.fragment.BankDestinationFragment;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankDestinationActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context, BankDestinationActivity.class);
        return intent;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        return BankDestinationFragment.createInstance();
    }
}
