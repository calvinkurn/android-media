package com.tokopedia.posapp;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.posapp.PosAppRouter;
import com.tokopedia.posapp.auth.login.view.activity.PosLoginActivity;
import com.tokopedia.posapp.cache.PosCacheHandler;
import com.tokopedia.posapp.di.component.DaggerSplashScreenComponent;
import com.tokopedia.posapp.di.component.SplashScreenComponent;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/28/17.
 */

public class PosAppSplashScreen extends SplashScreen {
    @Inject
    UserSession userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        if (getIntent().getBooleanExtra(PosAppRouter.IS_LOGOUT, false)) {
            PosSessionHandler.clearPosUserData(this);
            PosCacheHandler.clearUserData(this);
        }
    }

    private void initInjector() {
        SplashScreenComponent splashScreenComponent = DaggerSplashScreenComponent.builder()
                .posAppComponent(((PosApplication) getApplicationContext()).getPosAppComponent())
                .build();

        splashScreenComponent.inject(this);
    }

    @Override
    public void finishSplashScreen() {
        Intent intent;
        if (!userSession.isLoggedIn()) {
            intent = PosLoginActivity.getPosLoginIntent(this);
        } else {
            intent = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
        }
        startActivity(intent);
        finish();
    }
}
