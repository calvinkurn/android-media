package com.tokopedia.sellerapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author by milhamj on 2019-08-22.
 */
public class LogoutActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof SellerRouterApplication) {
            ((SellerRouterApplication) getApplication()).logoutToHome(this);
        }
        finish();
    }
}
