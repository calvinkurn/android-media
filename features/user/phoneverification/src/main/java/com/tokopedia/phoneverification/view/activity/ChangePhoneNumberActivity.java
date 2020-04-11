package com.tokopedia.phoneverification.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.phoneverification.view.fragment.ChangePhoneNumberFragment;

/**
 * Created by nisie on 2/23/17.
 */

public class ChangePhoneNumberActivity extends BaseSimpleActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected Fragment getNewFragment() {
        return ChangePhoneNumberFragment.createInstance();
    }

    public static Intent getChangePhoneNumberIntent(Context context, String phoneNumber) {
        Intent intent = new Intent(context, ChangePhoneNumberActivity.class);
        intent.putExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER, phoneNumber);
        return intent;
    }
}
