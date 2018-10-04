package com.tokopedia.pms.clickbca.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.pms.common.Constant;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeClickBcaActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, String transactionId, String merchantCode, String userIdKlikBca){
        Intent intent = new Intent(context, ChangeClickBcaActivity.class);
        intent.putExtra(Constant.TRANSACTION_ID, transactionId);
        intent.putExtra(Constant.MERCHANT_CODE, merchantCode);
        intent.putExtra(Constant.USER_ID_KLIK_BCA, userIdKlikBca);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String transactionId = getIntent().getStringExtra(Constant.TRANSACTION_ID);
        String merchantCode = getIntent().getStringExtra(Constant.MERCHANT_CODE);
        String userIdKlikBca = getIntent().getStringExtra(Constant.USER_ID_KLIK_BCA);
        return ChangeClickBcaFragment.createInstance(transactionId, merchantCode, userIdKlikBca);
    }
}
