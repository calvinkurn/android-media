package com.tokopedia.tkpd.utils;

import android.content.Context;

import com.tkpd.remoteresourcerequest.callback.DeferredCallback;
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.tkpd.remoteresourcerequest.task.ResourceDownloadManager.MANAGER_TAG;

public class DeferredResourceInitializer implements DeferredCallback {
    private static final String ENABLE_ASYNC_REMOTERESOURCE_INIT = "android_async_remoteresource_init";
    private static String RELATIVE_URL = "/android/res/";

    public void initializeResourceDownloadManager(final Context context) {
        WeaveInterface libInitWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return init(context);
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(libInitWeave, ENABLE_ASYNC_REMOTERESOURCE_INIT, context.getApplicationContext());
    }

    private boolean init(Context context) {
        ResourceDownloadManager
                .Companion.getManager()
                .setBaseAndRelativeUrl(AccountHomeUrl.CDN_URL, RELATIVE_URL)
                .addDeferredCallback(this)
                .initialize(context, R.raw.resources_description);
        return true;
    }

    @Override
    public void logDeferred(@NotNull String message) {
        String[] msg = message.split(",");
        if (msg.length > 2) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", msg[0]);
            messageMap.put("worker", msg[1]);
            messageMap.put("url", msg[2]);
            ServerLogger.log(Priority.P1, MANAGER_TAG, messageMap);
        }
        else {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", message);
            messageMap.put("worker", "null");
            messageMap.put("url", "null");
            ServerLogger.log(Priority.P1, MANAGER_TAG, messageMap);
        }
    }

    @Override
    public void onDownloadStateChanged(@NotNull String resUrl, boolean isFailed) {

    }

    @Override
    public void onCacheHit(@NotNull String resUrl) {

    }
}
