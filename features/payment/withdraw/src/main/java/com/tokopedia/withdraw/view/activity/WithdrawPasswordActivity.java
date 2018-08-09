package com.tokopedia.withdraw.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.withdraw.view.fragment.WithdrawPasswordFragment;

public class WithdrawPasswordActivity extends BaseSimpleActivity {

    public final static String BUNDLE_BANK = "bank";
    public final static String BUNDLE_WITHDRAW = "withdraw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        return WithdrawPasswordFragment.createInstance(bundle);
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, WithdrawPasswordActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
