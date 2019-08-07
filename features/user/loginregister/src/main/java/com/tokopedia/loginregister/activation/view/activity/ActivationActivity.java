package com.tokopedia.loginregister.activation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.loginregister.activation.di.DaggerActivationComponent;
import com.tokopedia.loginregister.activation.view.fragment.ActivationFragment;
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics;
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;

import javax.inject.Inject;

/**
 * Created by nisie on 2/1/17.
 */

public class ActivationActivity extends BaseSimpleActivity implements HasComponent {

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        bundle.putAll(getIntent().getExtras());
        return ActivationFragment.createInstance(bundle);
    }

    public static final String INTENT_EXTRA_PARAM_EMAIL = "email";
    public static final String INTENT_EXTRA_PARAM_PW = "pw";

    @Inject
    LoginRegisterAnalytics analytics;

    @Inject
    RegisterAnalytics registerAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();

    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setPadding(0, 0, 30, 0);
    }

    private void initInjector() {
        DaggerActivationComponent daggetActivationComponent = (DaggerActivationComponent)
                DaggerActivationComponent
                        .builder().loginRegisterComponent(getComponent())
                        .build();

        daggetActivationComponent.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        analytics.trackScreen(this, LoginRegisterAnalytics.Companion.getSCREEN_ACCOUNT_ACTIVATION());
    }

    public static Intent getCallingIntent(Context context, String email, String pw) {
        Intent callingIntent = new Intent(context, ActivationActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_PW, pw);
        return callingIntent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registerAnalytics.trackClickOnBackButtonActivation();
    }

    @Override
    public LoginRegisterComponent getComponent() {
        return DaggerLoginRegisterComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }
}
