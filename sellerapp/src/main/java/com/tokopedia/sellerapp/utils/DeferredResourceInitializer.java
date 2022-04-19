package com.tokopedia.sellerapp.utils;

import android.content.Context;

import com.tkpd.remoteresourcerequest.callback.DeferredCallback;
import com.tkpd.remoteresourcerequest.task.ResourceDownloadManager;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.sellerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.tkpd.remoteresourcerequest.task.ResourceDownloadManager.MANAGER_TAG;

public class DeferredResourceInitializer implements DeferredCallback {
    private static String RELATIVE_URL = "/android/res/";
    private static String CDN_URL = "https://ecs7.tokopedia.net";
    public void initializeResourceDownloadManager(Context context){
        Observable.fromCallable(() -> {

            ResourceDownloadManager
                    .Companion.getManager()
                    .setBaseAndRelativeUrl(CDN_URL, RELATIVE_URL)
                    .addDeferredCallback(this)
                    .initialize(context, R.raw.resources_description);
            return true;
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
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
