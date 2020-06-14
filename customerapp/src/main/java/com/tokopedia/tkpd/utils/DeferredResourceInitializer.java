package com.tokopedia.tkpd.utils;

import android.content.Context;

import com.tkpd.remoteresourcerequest.callback.DeferredCallback;
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class DeferredResourceInitializer implements DeferredCallback {
    private static String RELATIVE_URL = "/android/res/";

    public void initializeResourceDownloadManager(Context context) {
        WeaveInterface libInitWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return init(context);
            }
        };
        Weaver.Companion.executeWeaveCoRoutineNow(libInitWeave);
    }

    private boolean init(Context context) {
        ResourceDownloadManager
                .Companion.getManager()
                .setBaseAndRelativeUrl(AccountHomeUrl.CDN_URL, RELATIVE_URL)
                .addDeferredCallback(this)
                .initialize(context, R.raw.url_list, BuildConfig.VERSION_NAME);
        return true;
    }

    @Override
    public void logDeferred(@NotNull String message) {
        Timber.w("P1%s#%s", ResourceDownloadManager.MANAGER_TAG, message);
    }

    @Override
    public void onDownloadStateChanged(@NotNull String resUrl, boolean isFailed) {

    }

    @Override
    public void onCacheHit(@NotNull String resUrl) {

    }
}
