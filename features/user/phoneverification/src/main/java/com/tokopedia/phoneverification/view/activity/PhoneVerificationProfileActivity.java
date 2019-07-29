package com.tokopedia.phoneverification.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationProfileFragment;

/**
 * Created by nisie on 2/23/17.
 *  * For navigate: use {@link ApplinkConstInternalGlobal.SETTING_PROFILE_PHONE_VERIFICATION}
 */
public class PhoneVerificationProfileActivity extends BaseSimpleActivity {

    private static final int RESULT_CODE_MANAGE_PROFILE = 123;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        PhoneVerificationProfileFragment fragmentHeader = PhoneVerificationProfileFragment.createInstance();
        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance
                (getPhoneVerificationListener(), false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else if (((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getListener() == null) {
            ((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container))
                    .setPhoneVerificationListener(getPhoneVerificationListener());
        }
        fragmentTransaction.commit();

    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {
                setIntentTarget(Activity.RESULT_CANCELED);
            }


            @Override
            public void onSuccessVerification() {
                setIntentTarget(Activity.RESULT_OK);
            }
        };
    }

    private void setIntentTarget(int result) {
        if (isTaskRoot()) {
            goToManageProfile();
        } else {
            setResult(result);
            finish();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_phone_verification_activation;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void inflateFragment() {
        initView();
    }

    private void goToManageProfile() {
        Intent intent = RouteManager.getIntent(getApplicationContext(), ApplinkConstInternalGlobal.SETTING_PROFILE);
        startActivityForResult(intent, RESULT_CODE_MANAGE_PROFILE);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PhoneVerificationProfileActivity.class);
    }
}
