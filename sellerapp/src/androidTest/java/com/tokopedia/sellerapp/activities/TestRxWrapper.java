package com.tokopedia.sellerapp.activities;

import com.tokopedia.sellerapp.utils.RxWrapper;

import rx.Observable;
import rx.Subscription;

/**
 * Created by normansyahputa on 8/17/16.
 */

public class TestRxWrapper<T> extends RxWrapper<T> {

    @Override
    public Subscription run(Observable<T> on, RxWrapperListener<T> listener) {
        T data = on.toBlocking().first();
        listener.onNext(data);
        return null;
    }
}

