package com.tokopedia.sellerapp.home.utils;

import com.tokopedia.core.rxjava.RxUtils;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by normansyahputa on 9/28/16.
 */

public abstract class BaseController {
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public void subscribe(){
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    public void unSubscribe(){
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        compositeSubscription = null;
    }
}
