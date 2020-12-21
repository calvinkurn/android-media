package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.fragment.setting.AccountSettingFragment;

public class AccountSettingActivity extends BaseSimpleActivity {

    private static final int REQUEST_CHANGE_PASSWORD = 123;

    public static Intent createIntent(Context context) {
        return new Intent(context, AccountSettingActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    NetworkErrorHelper.showSnackbar(this, getString(R.string.message_success_change_profile));
                    setResult(resultCode, new Intent());
                    break;
                case REQUEST_CHANGE_PASSWORD:
                    NetworkErrorHelper.showGreenCloseSnackbar(this, getString(R.string.message_success_change_password));
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if(getIntent().getExtras()!= null){
            bundle.putAll(getIntent().getExtras());
        }
        return AccountSettingFragment.createInstance(bundle);
    }
}
