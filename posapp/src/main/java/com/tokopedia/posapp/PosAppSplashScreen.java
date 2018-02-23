package com.tokopedia.posapp;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.posapp.PosAppRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.view.activity.PosLoginActivity;

/**
 * Created by okasurya on 7/28/17.
 */

public class PosAppSplashScreen extends SplashScreen {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(PosAppRouter.IS_LOGOUT, false)) {
            PosSessionHandler.clearPosUserData(this);
            PosCacheHandler.clearUserData(this);
        }
    }

    @Override
    public void finishSplashScreen() {
        Intent intent;
        if (!SessionHandler.isV4Login(this)) {
            intent = PosLoginActivity.getPosLoginIntent(this);
        } else {
            intent = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
        }
        startActivity(intent);
        finish();
    }
}
