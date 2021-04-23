package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp;
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst;
import com.tokopedia.applink.sellermigration.SellerMigrationRedirectionUtil;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.fcmcommon.service.SyncFcmTokenService;
import com.tokopedia.graphql.util.LoggingUtils;
import com.tokopedia.logger.LogManager;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.keys.Keys.NEW_RELIC;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;
import com.tokopedia.sellerapp.utils.SellerOnboardingPreference;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import com.newrelic.agent.android.NewRelic;

import java.util.ArrayList;

import static com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LANDING_SHOP_CREATION;

/**
 * Created by normansyahputa on 11/29/16.
 */

public class SplashScreenActivity extends SplashScreen {

    private boolean isApkTempered;
    private static String KEY_AUTO_LOGIN = "is_auto_login";
    private static String KEY_CONFIG_RESPONSE_SIZE_LOG = "android_resp_size_log_threshold";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean defaultInteraction = false;
        NewRelic
                .withApplicationToken(NEW_RELIC)
                .withDefaultInteractions(defaultInteraction)
                .start(this.getApplication());
        
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
        CMPushNotificationManager.getInstance()
                .refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(this.getApplicationContext()), false);

        syncFcmToken();
    }

    /**
     * handle/forward app link redirection from customer app to seller app
     */
    private boolean handleAppLink(UserSessionInterface userSession) {
        Uri uri = getIntent().getData();
        if (null != uri) {
            boolean isFromMainApp = uri.getBooleanQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, false);
            boolean isAutoLogin = uri.getBooleanQueryParameter(KEY_AUTO_LOGIN, false);
            if (isFromMainApp) {
                if (isAutoLogin && userSession.getUserId().isEmpty()) {
                    ArrayList<String> remainingAppLinks = getIntent().getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA);
                    seamlessLogin(true, remainingAppLinks);
                    return true;
                }
                ArrayList<String> remainingAppLinks = getIntent().getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA);
                if (remainingAppLinks == null || remainingAppLinks.size() == 0) {
                    Intent intent = RouteManager.getIntent(this, uri.toString());
                    if (intent!= null &&intent.resolveActivity(this.getPackageManager()) != null) {
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
                new SellerMigrationRedirectionUtil().startRedirectionActivities(this, remainingAppLinks);
                return true;
            }
            return false;
        }
        return false;
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
        if (handleAppLink(userSession)) {
            finish();
            return;
        }

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
            boolean isAutoLoginSeamless = getIntent().getBooleanExtra(KEY_AUTO_LOGIN, false);
            seamlessLogin(isAutoLoginSeamless, null);
        }
        finish();
    }

    private void seamlessLogin(boolean isAutoLoginSeamless, ArrayList<String> remainingApplinks) {
        boolean hasOnboarding = new SellerOnboardingPreference(this)
                .getBoolean(SellerOnboardingPreference.HAS_OPEN_ONBOARDING);
        Intent intent;
        if (isAutoLoginSeamless){
            intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.SEAMLESS_LOGIN);
            Bundle b = new Bundle();
            b.putBoolean(KEY_AUTO_LOGIN, true);
            if (remainingApplinks != null && !remainingApplinks.isEmpty()) {
                intent.putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, remainingApplinks);
            }
            String featureName = getIntent().getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME);
            if (featureName != null && !featureName.isEmpty()) {
                intent.putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, featureName);
            }
            intent.putExtras(b);
        } else if (hasOnboarding) {
            intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.SEAMLESS_LOGIN);
        } else {
            intent = RouteManager.getIntent(this, ApplinkConstInternalSellerapp.WELCOME);
        }
        startActivity(intent);
    }

    @NonNull
    public static Intent moveToCreateShop(Context context) {
        Intent intent = RouteManager.getIntent(context, LANDING_SHOP_CREATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected RemoteConfig.Listener getRemoteConfigListener() {
        return new RemoteConfig.Listener() {
            @Override
            public void onComplete(RemoteConfig remoteConfig) {
                LogManager logManager = LogManager.instance;
                if(logManager!= null) {
                    logManager.refreshConfig();
                }
                LoggingUtils.setResponseSize(remoteConfig.getLong(KEY_CONFIG_RESPONSE_SIZE_LOG));
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }
}
