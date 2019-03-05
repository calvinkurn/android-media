package com.tokopedia.loginregister.login.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment;
import com.tokopedia.loginregister.login.view.fragment.LoginFragment;
import com.tokopedia.loginregister.login.view.listener.LoginEmailPhoneContract;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 10/1/18.
 */
public class LoginActivity extends BaseSimpleActivity implements HasComponent {

    public static final int METHOD_FACEBOOK = 111;
    public static final int METHOD_GOOGLE = 222;
    public static final int METHOD_WEBVIEW = 333;
    public static final int METHOD_EMAIL = 444;

    public static final String AUTO_WEBVIEW_NAME = "webview_name";
    public static final String AUTO_WEBVIEW_URL = "webview_url";

    @DeepLink({ApplinkConst.LOGIN})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        UserSessionInterface userSession = new UserSession(context);
        if (userSession.isLoggedIn()) {
            if (context.getApplicationContext() instanceof ApplinkRouter) {
                return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent
                        (context, ApplinkConst.HOME);
            } else {
                throw new RuntimeException("Applinks intent unsufficient");
            }
        } else {
            Intent intent = getCallingIntent(context);
            return intent.setData(uri.build());
        }
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setPadding(0, 0, 30, 0);
    }

    @Override
    protected Fragment getNewFragment() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getApplicationContext());

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

//        if (GlobalConfig.isSellerApp() || !remoteConfig.getBoolean(RemoteConfigKey.LOGIN_REVAMP_UI, true)) {
//            return LoginFragment.createInstance(bundle);
//        } else {
            return LoginEmailPhoneFragment.Companion.createInstance(bundle);
//        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent getAutoLoginGoogle(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_GOOGLE);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAutoLoginFacebook(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_FACEBOOK);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAutoLoginWebview(Context context, String name, String url) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_WEBVIEW);
        bundle.putString(AUTO_WEBVIEW_NAME, name);
        bundle.putString(AUTO_WEBVIEW_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getIntentLoginFromRegister(Context context, String email) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_FROM_REGISTER, true);
        bundle.putBoolean(LoginFragment.IS_AUTO_FILL, true);
        bundle.putString(LoginFragment.AUTO_FILL_EMAIL, email);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAutomaticLogin(Context context, String email, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putBoolean(LoginFragment.IS_FROM_REGISTER, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_EMAIL);
        bundle.putString(LoginFragment.AUTO_LOGIN_EMAIL, email);
        bundle.putString(LoginFragment.AUTO_LOGIN_PASS, password);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public LoginRegisterComponent getComponent() {
        return DaggerLoginRegisterComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                LoginEmailPhoneContract.View) {
            ((LoginEmailPhoneContract.View) getSupportFragmentManager().findFragmentById(R.id
                    .parent_view)).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
