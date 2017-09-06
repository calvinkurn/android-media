package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.posapp.view.Cache;

import rx.Subscriber;

/**
 * Created by okasurya on 8/29/17.
 */

public class CachingProductSubscriber extends Subscriber<Boolean> {
    Cache.CallbackListener callbackListener;

    public CachingProductSubscriber(Cache.CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        callbackListener.onError(e);
    }

    @Override
    public void onNext(Boolean aBoolean) {
        callbackListener.onProductListStored();
    }
}
