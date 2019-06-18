package com.tokopedia.phoneverification.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.phoneverification.PhoneVerificationAnalytics;
import com.tokopedia.phoneverification.PhoneVerificationConst;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.R;
import com.tokopedia.phoneverification.di.DaggerPhoneVerificationComponent;
import com.tokopedia.phoneverification.di.PhoneVerificationComponent;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationActivationFragment;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import static com.tokopedia.phoneverification.view.activity.PhoneVerificationProfileActivity.getCallingIntent;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationActivationActivity extends BaseSimpleActivity {

    public static final String EXTRA_IS_MANDATORY = "EXTRA_IS_MANDATORY";
    public static final String EXTRA_IS_LOGOUT_ON_BACK = "EXTRA_LOGOUT_ON_BACK";

    private boolean canSkip = false;
    private boolean isLogoutOnBack = false;
    PhoneVerificationAnalytics analytics;

    @Inject
    UserSession userSession;

    @DeepLink({ApplinkConst.PHONE_VERIFICATION})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        Intent intent = getCallingIntent(context);
        return intent.setData(uri.build());
    }

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
        initInjector();
        initView();

        analytics = PhoneVerificationAnalytics.createInstance();
    }

    private void initInjector() {
        BaseAppComponent baseAppComponent =
                ((BaseMainApplication) this.getApplication()).getBaseAppComponent();
        PhoneVerificationComponent phoneVerificationComponent =
                DaggerPhoneVerificationComponent.
                        builder().
                        baseAppComponent(baseAppComponent).
                        build();

        phoneVerificationComponent.inject(this);
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
        PhoneVerificationActivationFragment fragmentHeader = PhoneVerificationActivationFragment
                .createInstance();
        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance
                (getPhoneVerificationListener(), canSkip);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader
                    .getClass().getSimpleName());
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else if (((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id
                .container))
                .getListener() == null) {
            ((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id
                    .container))
                    .setPhoneVerificationListener(getPhoneVerificationListener());
        }
        fragmentTransaction.commit();
    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener
    getPhoneVerificationListener() {
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
        if (analytics != null) {
            analytics.eventClickOnBackPressed();
        }

        super.onBackPressed();

    }

    private void goToSellerHome() {
        Intent intent = ((PhoneVerificationRouter) getApplicationContext()).getHomeIntent
                (getApplicationContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void goToConsumerHome() {
        Intent intent = ((PhoneVerificationRouter) getApplicationContext()).getHomeIntent
                (getApplicationContext());
        intent.putExtra(PhoneVerificationConst.EXTRA_INIT_FRAGMENT,
                PhoneVerificationConst.INIT_STATE_FRAGMENT_HOME);
        startActivity(intent);
        finish();
    }

    private void goToSellerShopCreateEdit() {
        Intent intent = ((PhoneVerificationRouter) getApplicationContext()).getIntentCreateShop
                (getApplicationContext());
        startActivity(intent);
        finish();
    }

    private boolean isHasShop() {
        return userSession.hasShop();
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
}
