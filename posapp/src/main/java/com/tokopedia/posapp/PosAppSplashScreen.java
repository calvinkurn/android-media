package com.tokopedia.posapp;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.view.activity.LoginActivity;
import com.tokopedia.posapp.view.activity.OTPActivity;
import com.tokopedia.posapp.view.activity.OutletActivity;
import com.tokopedia.posapp.view.activity.ProductListActivity;
import com.tokopedia.posapp.view.service.CacheService;
import com.tokopedia.posapp.view.service.SchedulerService;

/**
 * Created by okasurya on 7/28/17.
 */

public class PosAppSplashScreen extends SplashScreen {
    @Override
    public void finishSplashScreen() {
        Intent intent;
        if(!SessionHandler.isV4Login(this)) {
           intent = LoginActivity.getPosLoginIntent(this);
        } else {
            intent = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
        }
        startActivity(intent);
        finish();
    }
}
