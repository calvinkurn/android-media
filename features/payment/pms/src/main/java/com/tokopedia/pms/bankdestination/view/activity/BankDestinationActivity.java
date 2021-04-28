package com.tokopedia.pms.bankdestination.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.pms.bankdestination.view.fragment.BankDestinationFragment;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankDestinationActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSecureWindowFlag();
    }

    private void setSecureWindowFlag() {
        runOnUiThread(() -> getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE));
    }

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
