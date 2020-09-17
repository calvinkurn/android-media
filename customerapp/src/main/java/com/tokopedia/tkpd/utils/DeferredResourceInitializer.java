package com.tokopedia.tkpd.utils;

import android.content.Context;

import com.tkpd.remoteresourcerequest.callback.DeferredCallback;
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.tkpd.R;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

import static com.tkpd.remoteresourcerequest.task.ResourceDownloadManager.MANAGER_TAG;

public class DeferredResourceInitializer implements DeferredCallback {
    private static String RELATIVE_URL = "/android/res/";
    private static final String ENABLE_ASYNC_REMOTERESOURCE_INIT = "android_async_remoteresource_init";

    public void initializeResourceDownloadManager(Context context) {
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
        if (msg.length > 2)
            Timber.w("P1#%s#%s;worker=%s;url=%s", MANAGER_TAG, msg[0], msg[1], msg[2]);
        else
            Timber.w("P1#%s#%s;worker=%s;url=%s", MANAGER_TAG, message, null, null);

    }

    @Override
    public void onDownloadStateChanged(@NotNull String resUrl, boolean isFailed) {

    }

    @Override
    public void onCacheHit(@NotNull String resUrl) {

    }
}
