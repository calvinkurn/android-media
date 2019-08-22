package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;

/**
 * @author by milhamj on 2019-08-22.
 */
public class LogoutActivity extends AppCompatActivity {

    @DeepLink("sellerapp://logout")
    public static Intent getIntent(Context context, Bundle bundle) {
        return new Intent(context, LogoutActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof SellerRouterApplication) {
            ((SellerRouterApplication) getApplication()).logoutToHome(this);
        }
        finish();
    }
}
