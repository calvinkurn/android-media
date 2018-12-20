package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.singleton.ContainerHolderSingleton;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NewGTMSuicide extends ContextAnalytics {
    private static final String IS_EXCEPTION_ENABLED = "is_exception_enabled";
    private static final String IS_USING_HTTP_2 = "is_using_http_2";
    private static final String STR_GTM_EXCEPTION_ENABLED = "GTM is exception enabled";
    public static final String CLIENT_ID = "client_id";
    private static final String TAG = NewGTMSuicide.class.getSimpleName();
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = 7200000;

    // have status that describe pending.

    public NewGTMSuicide(Context context) {
        super(context);
    }

    public TagManager getTagManager() {
        return TagManager.getInstance(getContext());
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = getTagManager();
            PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));

            pResult.setResultCallback(cHolder -> {
                ContainerHolderSingleton.setContainerHolder(cHolder);
                if (isAllowRefreshDefault(cHolder)) {
                    Log.i("GTM TKPD", "Refreshed Container ");
                    cHolder.refresh();
                    //setExpiryRefresh();
                }

                validateGTM(cHolder);
            }, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            eventError(getContext().getClass().toString(), e.toString());
        }
    }

    public void eventError(String screenName, String errorDesc) {
        if (getString(IS_EXCEPTION_ENABLED).equals("true")) {
            Log.d(TAG, "Sending Push Event Error");
            pushEvent("trackException", DataLayer.mapOf(
                    "screenName", screenName,
                    "exception.description", errorDesc,
                    "exception.isFatal", "true"));
        } else {
            Log.d(TAG, "Sending Push Event Error disabled");
        }
    }

    public void sendScreen(String screenName) {
        Log.i("Tag Manager", "UA-9801603-15: Send Screen Event");
        pushEvent("openScreen", DataLayer.mapOf("screenName", screenName));
    }

    public void pushEvent(String eventName, Map<String, Object> values) {
        Log.i("GAv4", "UA-9801603-15: Send Event");

        log(getContext(), eventName, values);

        getTagManager().getDataLayer().pushEvent(eventName, values);
    }

    private static void log(Context context, String eventName, Map<String, Object> values) {
        String name = eventName == null ? (String) values.get("event") : eventName;
        GtmLogger.getInstance().save(context, name, values);
    }

    private void validateGTM(ContainerHolder containerHolder) {
        if (containerHolder.getStatus().isSuccess()) {
            Log.i(TAG, STR_GTM_EXCEPTION_ENABLED + getString(IS_EXCEPTION_ENABLED));
        } else {
            Log.e("GTMContainer", "failure loading container");
        }
    }

    public static Container getContainer() {
        return ContainerHolderSingleton.getContainerHolder().getContainer();
    }

    public String getString(String key) {
        if (ContainerHolderSingleton.isContainerHolderAvailable()) {
            return getContainer().getString(key);
        }
        return "";
    }

    private Boolean isAllowRefreshDefault(ContainerHolder containerHolder) {
        long lastRefresh = 0;
        if (containerHolder.getContainer() != null) {
            lastRefresh = containerHolder.getContainer().getLastRefreshTime();
        }
        Log.i("GTM TKPD", "Last refresh " + CommonUtils.getDate(lastRefresh));
        return System.currentTimeMillis() - lastRefresh > EXPIRE_CONTAINER_TIME_DEFAULT;
    }

    private static class GTMBody {
        Map<String, Object> values;
        String eventName;
    }
}
