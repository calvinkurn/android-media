package com.tokopedia.posapp;

import android.content.Intent;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.view.activity.LoginActivity;
import com.tokopedia.posapp.view.activity.OutletActivity;
import com.tokopedia.posapp.view.service.CacheService;

/**
 * Created by okasurya on 7/28/17.
 */

public class PosAppSplashScreen extends SplashScreen {
    @Override
    public void finishSplashScreen() {
        if(!SessionHandler.isV4Login(this)) {
            startActivity(LoginActivity.getPosLoginIntent(this));
        } else {
            startActivity(new Intent(this, OutletActivity.class));
            startService(CacheService.getServiceIntent(this));
        }
        finish();
    }
}
