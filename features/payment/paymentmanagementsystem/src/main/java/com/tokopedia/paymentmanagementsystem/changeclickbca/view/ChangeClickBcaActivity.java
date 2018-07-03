package com.tokopedia.paymentmanagementsystem.changeclickbca.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.paymentmanagementsystem.common.Constant;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeClickBcaActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, String transactionId, String merchantCode){
        Intent intent = new Intent(context, ChangeClickBcaActivity.class);
        intent.putExtra(Constant.TRANSACTION_ID, transactionId);
        intent.putExtra(Constant.MERCHANT_CODE, merchantCode);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String transactionId = getIntent().getStringExtra(Constant.TRANSACTION_ID);
        String merchantCode = getIntent().getStringExtra(Constant.MERCHANT_CODE);
        return ChangeClickBcaFragment.createInstance(transactionId, merchantCode);
    }
}
