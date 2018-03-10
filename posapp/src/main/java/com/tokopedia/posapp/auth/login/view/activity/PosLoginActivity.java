package com.tokopedia.posapp.auth.login.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.auth.login.view.fragment.PosLoginFragment;

/**
 * Created by okasurya on 8/1/17.
 */

public class PosLoginActivity extends BaseSimpleActivity implements HasComponent {
    public static Intent getPosLoginIntent(Context context) {
        Intent callingIntent = new Intent(context, PosLoginActivity.class);
//        callingIntent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
//        callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected Fragment getNewFragment() {
        return PosLoginFragment.createInstance();
    }

    @Override
    protected String getTagFragment() {
        return PosLoginFragment.class.getSimpleName();
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplicationContext()).getAppComponent();
    }
}
