package com.tokopedia.common.travel.domain.subscriber;

import rx.Subscriber;

public class AutoCompleteKeywordSubscriber extends Subscriber<String> {
    private AutoCompleteKeywordListener listener;

    public AutoCompleteKeywordSubscriber(AutoCompleteKeywordListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        listener.onError(e);
    }

    @Override
    public void onNext(String keyword) {
        if (!isUnsubscribed()) {
            listener.onTextReceive(keyword.trim());
        }
    }
}