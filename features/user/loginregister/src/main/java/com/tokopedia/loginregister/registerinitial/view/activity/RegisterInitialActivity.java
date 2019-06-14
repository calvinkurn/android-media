package com.tokopedia.loginregister.registerinitial.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.di.DaggerLoginRegisterComponent;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment;
import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 10/2/18.
 */
public class RegisterInitialActivity extends BaseSimpleActivity implements HasComponent{

    @Override
    protected Fragment getNewFragment() {
        return RegisterInitialFragment.createInstance();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterInitialActivity.class);
    }

    @DeepLink({ApplinkConst.REGISTER})
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
    public LoginRegisterComponent getComponent() {
        return DaggerLoginRegisterComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                RegisterInitialContract.View) {
            ((RegisterInitialContract.View) getSupportFragmentManager().findFragmentById(R.id
                    .parent_view)).onBackPressed();
        }

        super.onBackPressed();
    }
}
