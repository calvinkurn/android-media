package com.tokopedia.sellerapp.onboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp;

public class SellerOnboardingBridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirectToOnboarding();
    }

    void redirectToOnboarding() {
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalSellerapp.WELCOME);
        startActivity(intent);
        finish();
    }
}