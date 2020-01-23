package com.tokopedia.tkpd.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.analytics.debugger.TetraDebugger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.tkpd.R;
import com.tokopedia.track.TrackApp;

public class LogoutActivity extends AppCompatActivity {

    private String STICKY_LOGIN_PREF = "sticky_login_widget.pref";

    private Iris mIris = null;
    private TetraDebugger tetraDebugger = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        initIris();
        initTetraDebugger();

        doLogout();
    }

    private void initIris() {
        mIris = IrisAnalytics.Companion.getInstance(getApplicationContext());
        mIris.initialize();
    }

    private void initTetraDebugger() {
        if (com.tokopedia.config.GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = TetraDebugger.Companion.instance(getApplicationContext());
            tetraDebugger.init();
        }
    }

    private void doLogout() {
        new GlobalCacheManager().deleteAll();

        PersistentCacheManager.instance.delete();

        EtalaseUtils.clearEtalaseCache(getApplicationContext());

        TrackApp.getInstance().getMoEngage().logoutEvent();

        SessionHandler.clearUserData(getApplicationContext());

        NotificationModHandler notify = new NotificationModHandler(getApplicationContext());
        notify.dismissAllActivedNotifications();

        NotificationModHandler.clearCacheAllNotification(getApplicationContext());

        AppWidgetUtil.sendBroadcastToAppWidget(getApplicationContext());

        CMPushNotificationManager.getInstance().refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(getApplicationContext()), true);

        mIris.setUserId("");

        setTetraUserId("");

        SharedPreferences stickyPref = getApplicationContext().getSharedPreferences(STICKY_LOGIN_PREF, Context.MODE_PRIVATE);
        stickyPref.edit().clear().apply();

        Intent intent;
        if (GlobalConfig.isSellerApp()) {
            intent = MainParentActivity.start(getApplicationContext());
        } else {
            intent = RouteManager.getIntent(getApplicationContext(), ApplinkConst.HOME);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new Handler().postDelayed(() -> startActivity(intent), 10000);
    }

    private void setTetraUserId(String userId) {
        if (tetraDebugger != null) {
            tetraDebugger.setUserId(userId);
        }
    }
}
