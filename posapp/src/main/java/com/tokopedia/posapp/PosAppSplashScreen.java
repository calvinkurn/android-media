package com.tokopedia.posapp;

import android.content.Intent;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.view.activity.LoginActivity;
import com.tokopedia.posapp.view.activity.OutletActivity;
import com.tokopedia.posapp.view.activity.ProductListActivity;
import com.tokopedia.posapp.view.service.CacheService;

/**
 * Created by okasurya on 7/28/17.
 */

public class PosAppSplashScreen extends SplashScreen {
    @Override
    public void finishSplashScreen() {
        Intent intent;
        if(!SessionHandler.isV4Login(this)) {
           intent = LoginActivity.getPosLoginIntent(this);
        } else if(isOutletSelected()) {
            intent = new Intent(this, ProductListActivity.class);
        } else {
            intent = new Intent(this, OutletActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public boolean isOutletSelected() {
        return PosSessionHandler.getOutletId(this) != null
                && !PosSessionHandler.getOutletId(this).equals("");
    }
}
