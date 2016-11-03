package com.tokopedia.tkpd.rxjava;

import android.util.Log;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxUtils {

    private static final String TAG = RxUtils.class.getSimpleName();

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            Log.d(TAG, "unsubscribeIfNotNull");
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
