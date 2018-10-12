package com.tokopedia.phoneverification.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.phoneverification.PhoneVerificationAnalytics;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationActivationFragment;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationActivationActivity extends BaseSimpleActivity {

    public static final String EXTRA_IS_MANDATORY = "EXTRA_IS_MANDATORY";
    public static final String EXTRA_IS_LOGOUT_ON_BACK = "EXTRA_LOGOUT_ON_BACK";

    private boolean canSkip = false;
    private boolean isLogoutOnBack = false;
    PhoneVerificationAnalytics analytics;

    public static Intent getIntent(Context context, boolean isMandatory, boolean isLogoutOnBack){
        Intent intent = new Intent(context, PhoneVerificationActivationActivity.class);
        intent.putExtra(EXTRA_IS_MANDATORY, isMandatory);
        intent.putExtra(EXTRA_IS_LOGOUT_ON_BACK, isLogoutOnBack);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IS_MANDATORY)) {
            canSkip = intent.getBooleanExtra(EXTRA_IS_MANDATORY, false);
        }
        if (intent.hasExtra(EXTRA_IS_LOGOUT_ON_BACK)) {
            isLogoutOnBack = intent.getBooleanExtra(EXTRA_IS_LOGOUT_ON_BACK, false);
        }
        super.onCreate(savedInstanceState);
        initView();

        analytics = PhoneVerificationAnalytics.createInstance(getApplicationContext());
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

    private void initView() {
        PhoneVerificationActivationFragment fragmentHeader = PhoneVerificationActivationFragment.createInstance();
        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance(getPhoneVerificationListener(), canSkip);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else if (((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container))
                .getListener() == null) {
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
        if (isTaskRoot()
                && GlobalConfig.isSellerApp()
                && isHasShop()) {
            goToSellerHome();
        } else if (isTaskRoot()
                && GlobalConfig.isSellerApp()) {
            goToSellerShopCreateEdit();
        } else if (isTaskRoot()) {
            goToConsumerHome();
        } else {
            setResult(result);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(analytics != null){
            analytics.eventClickOnBackPressed();
        }

        if (isLogoutOnBack) {
            SessionHandler session = new SessionHandler(this);
            session.Logout(this);
        } else {
            super.onBackPressed();
        }
    }

    private void goToSellerHome() {
        Intent intent = SellerAppRouter.getSellerHomeActivity(PhoneVerificationActivationActivity.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void goToConsumerHome() {
        Intent intent = HomeRouter.getHomeActivityInterfaceRouter(PhoneVerificationActivationActivity.this);
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                HomeRouter.INIT_STATE_FRAGMENT_HOME);
        startActivity(intent);
        finish();
    }

    private void goToSellerShopCreateEdit() {
        Intent intent = SellerRouter.getActivityShopCreateEdit(PhoneVerificationActivationActivity.this);
        startActivity(intent);
        finish();
    }

    private boolean isHasShop() {
        return SessionHandler.isUserHasShop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, PhoneVerificationActivationActivity.class);
        intent.putExtra(EXTRA_IS_MANDATORY, false);
        intent.putExtra(EXTRA_IS_LOGOUT_ON_BACK, false);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
