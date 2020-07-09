package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.fcmcommon.service.SyncFcmTokenService;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.utils.timber.TimberWrapper;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.selleronboarding.activity.SellerOnboardingActivity;
import com.tokopedia.selleronboarding.utils.OnboardingPreference;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import static com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.OPEN_SHOP;

/**
 * Created by normansyahputa on 11/29/16.
 */

public class SplashScreenActivity extends SplashScreen {

    private boolean isApkTempered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isApkTempered = false;
        try {
            getResources().getDrawable(R.drawable.launch_screen);
        } catch (Exception e) {
            isApkTempered = true;
            setTheme(R.style.Theme_Tokopedia3_PlainGreen);
        }
        super.onCreate(savedInstanceState);
        if (isApkTempered) {
            startActivity(new Intent(this, FallbackActivity.class));
            finish();
        }
        syncFcmToken();
    }

    private void syncFcmToken() {
        SyncFcmTokenService.Companion.startService(this);
    }

    @Override
    public void finishSplashScreen() {
        if (isApkTempered) {
            return;
        }
        UserSessionInterface userSession = new UserSession(this);
        if (userSession.hasShop()) {
            if (getIntent().hasExtra(Constants.EXTRA_APPLINK)) {
                String applinkUrl = getIntent().getStringExtra(Constants.EXTRA_APPLINK);
                DeepLinkDelegate delegate = DeepLinkHandlerActivity.getDelegateInstance();
                if (delegate.supportsUri(applinkUrl)) {
                    Intent intent = getIntent();
                    intent.setData(Uri.parse(applinkUrl));
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, true);
                    intent.putExtras(bundle);
                    delegate.dispatchFrom(this, intent);
                } else {
                    startActivity(SellerHomeActivity.createIntent(this));
                }
            } else {
                // Means it is a Seller
                startActivity(SellerHomeActivity.createIntent(this));
            }
        } else if (!TextUtils.isEmpty(userSession.getUserId())) {
            Intent intent = moveToCreateShop(this);
            startActivity(intent);
        } else {
            boolean hasOnboarding = new OnboardingPreference(this)
                    .getBoolean(OnboardingPreference.HAS_OPEN_ONBOARDING, false);
            Intent intent;
            if (hasOnboarding) {
                intent = LoginActivity.DeepLinkIntents.getCallingIntent(this);
            } else {
                intent = new Intent(this, SellerOnboardingActivity.class);
            }
            startActivity(intent);
        }
        finish();
    }

    @NonNull
    public static Intent moveToCreateShop(Context context) {
        Intent intent = RouteManager.getIntent(context, OPEN_SHOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected RemoteConfig.Listener getRemoteConfigListener() {
        return new RemoteConfig.Listener() {
            @Override
            public void onComplete(RemoteConfig remoteConfig) {
                TimberWrapper.initByRemoteConfig(getApplication(), remoteConfig);
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }
}
