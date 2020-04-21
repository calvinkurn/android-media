package com.tokopedia.sellerapp.utils;

import android.content.Context;

import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tkpd.remoteresourcerequest.utils.DeferredCallback;
import com.tokopedia.sellerapp.R;

import org.jetbrains.annotations.NotNull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DeferredResourceInitializer implements DeferredCallback{
    private static String RELATIVE_URL = "/android/res/";
    private static String CDN_URL = "https://ecs7.tokopedia.net";
    public void initializeResourceDownloadManager(Context context){
        Observable.fromCallable(() -> {

            ResourceDownloadManager
                    .Companion.getManager()
                    .setBaseAndRelativeUrl(CDN_URL, RELATIVE_URL)
                    .addDeferredCallback(this)
                    .initialize(context, R.raw.url_list);
            return true;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    @Override
    public void logDeferred(@NotNull String message) {
        Timber.tag(ResourceDownloadManager.MANAGER_TAG).d(message);
    }

    @Override
    public void onDownloadStateChanged(@NotNull String resUrl, boolean isFailed) {

    }

    @Override
    public void onCacheHit(@NotNull String resUrl) {

    }
}
