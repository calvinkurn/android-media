package com.tokopedia.core.rxjava;

import rx.Subscription;
import timber.log.Timber;

public class RxUtils {

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            Timber.d("unsubscribeIfNotNull");
            subscription.unsubscribe();
        }
    }
}
