package com.tokopedia.tkpd;

import android.content.Context;

import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tkpd.remoteresourcerequest.utils.DeferredCallback;
import com.tokopedia.home.account.AccountHomeUrl;

import org.jetbrains.annotations.NotNull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class DeferredResourceInitializer implements DeferredCallback{
    private static String RELATIVE_URL = "/android/res/";
    void initializeResourceDownloadManager(Context context){
        Observable.fromCallable(() -> {

            ResourceDownloadManager
                    .Companion.getManager()
                    .setBaseAndRelativeUrl(AccountHomeUrl.CDN_URL, RELATIVE_URL)
                    .addDeferredCallback(this)
                    .initialize(context, R.raw.url_list);
            return true;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    @Override
    public void logDeferred(@NotNull String message) {
        Timber.w(ResourceDownloadManager.MANAGER_TAG+": "+message);
    }

    @Override
    public void onDownloadStateChanged(@NotNull String resUrl, boolean isFailed) {

    }

    @Override
    public void onCacheHit(@NotNull String resUrl) {

    }
}
