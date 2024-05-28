package com.tokopedia.tkpd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;

import com.newrelic.agent.android.NewRelic;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.app.common.SplashScreen;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.navigation.DeeplinkNavigationUtil;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.fcmcommon.service.SyncFcmTokenService;
import com.tokopedia.installreferral.InstallReferral;
import com.tokopedia.installreferral.InstallReferralKt;
import com.tokopedia.installreferral.InstallReferrerInterface;
import com.tokopedia.keys.Keys;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.activity.NewMainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ricoharisin on 11/22/16.
 */

public class ConsumerSplashScreen extends SplashScreen {

    public static final String GOOGLE_ANALYTICS_DEFERRED_DEEPLINK_PREFERENCE = "google.analytics.deferred.deeplink.prefs";
    public static final String GOOGLE_DDL_DEEPLINK_KEY = "deeplink";
    public static final String TYPE_KEY = "type";
    public static final String GOOGLE_DDL_KEY = "GOOGLE_DDL";
    public static final String SPLASH_SCREEN_TYPE = "splash_screen";

    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener deepLinkListener;

    private DeeplinkNavigationUtil deeplinkNavigationUtil = new DeeplinkNavigationUtil();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpGoogleDeeplinkListener();
    }

    private void setUpGoogleDeeplinkListener() {
        preferences = getSharedPreferences(GOOGLE_ANALYTICS_DEFERRED_DEEPLINK_PREFERENCE, MODE_PRIVATE);
        deepLinkListener = (sharedPreferences, key) -> {
            if (GOOGLE_DDL_DEEPLINK_KEY.equals(key)) {
                String deeplink = sharedPreferences.getString(key, null);
                navigateToDeeplink(deeplink);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences.registerOnSharedPreferenceChangeListener(deepLinkListener);
        initiateBranchAndInstallReferrerFlow();
    }

    private void initiateBranchAndInstallReferrerFlow() {
        if (LinkerManager.getInstance().isFirstAppOpen(getApplicationContext())) {
            LinkerManager.getInstance().setDelayedSessionInitFlag();
        } else {
            getBranchDefferedDeeplink();
        }
        checkInstallReferrerInitialised();
    }

    @Override
    protected void onStop() {
        super.onStop();
        preferences.unregisterOnSharedPreferenceChangeListener(deepLinkListener);
        deepLinkListener = null;
    }

    private void navigateToDeeplink(String deeplink) {
        if (!TextUtils.isEmpty(deeplink)) {
            Intent intent = new Intent();
            String tokopediaDeeplink = deeplink;
            if (URLUtil.isNetworkUrl(deeplink)) {
                intent.setClassName(ConsumerSplashScreen.this.getPackageName(),
                        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
            } else {
                if (deeplink.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://")) {
                    tokopediaDeeplink = deeplink;
                } else {
                    tokopediaDeeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + deeplink;
                }
                intent.setClassName(ConsumerSplashScreen.this.getPackageName(),
                        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME);
            }
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put(TYPE_KEY, SPLASH_SCREEN_TYPE);
            messageMap.put(GOOGLE_DDL_DEEPLINK_KEY, tokopediaDeeplink);
            ServerLogger.log(Priority.P2, GOOGLE_DDL_KEY, messageMap);
            intent.setData(Uri.parse(tokopediaDeeplink));
            startActivity(intent);
            finish();
        }
    }

    public InstallReferrerInterface getInstallReferrerInterface() {
        return new InstallReferrerInterface() {
            @Override
            public void installReferrerDataRetrived(@NonNull String installReferrerData) {
                LinkerManager.getInstance().setDataFromInstallReferrerParams(installReferrerData);
                getBranchDefferedDeeplink();
            }
        };
    }

    private void checkInstallReferrerInitialised() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(ConsumerSplashScreen.this, InstallReferralKt.KEY_INSTALL_REF_SHARED_PREF_FILE_NAME);
        Boolean installRefInitialised = localCacheHandler.getBoolean(InstallReferralKt.KEY_INSTALL_REF_INITIALISED);
        if (!installRefInitialised) {
            localCacheHandler.applyEditor();
            InstallReferral installReferral = new InstallReferral();
            if (LinkerManager.getInstance().isFirstAppOpen(getApplicationContext())) {
                installReferral.setInstallReferrerInterface(getInstallReferrerInterface());
            }
            installReferral.initilizeInstallReferral(this.getApplicationContext());
        }
    }

    @Override
    public void finishSplashScreen() {
        // if this SplashScreenActivity is the root means this open first time, so open Home
        // if this is not root, this SplashScreen might be triggered from opening branch link.
        if (isTaskRoot()) {
            Intent homeIntent;
            if (deeplinkNavigationUtil.newHomeNavEnabled()) {
                homeIntent = new Intent(this, NewMainParentActivity.class);
            } else {
                homeIntent = new Intent(this, MainParentActivity.class);
            }
            startActivity(homeIntent);
        }
        finish();
    }
}
