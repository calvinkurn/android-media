package com.tokopedia.topads.dashboard.domain.interactor;

import rx.functions.Action1;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class SubscribeOnError implements Action1<Throwable> {

    private ListenerInteractor listener;

    public SubscribeOnError(ListenerInteractor listener) {
        this.listener = listener;
    }

    @Override
    public void call(Throwable throwable) {
        listener.onError(throwable);
    }
}
