package com.tokopedia.core.rxjava;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class RxUtils {

    private static final String TAG = RxUtils.class.getSimpleName();

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            Timber.d("unsubscribeIfNotNull");
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }
}
