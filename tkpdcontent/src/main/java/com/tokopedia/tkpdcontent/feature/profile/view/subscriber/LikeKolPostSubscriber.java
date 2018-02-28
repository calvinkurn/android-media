package com.tokopedia.tkpdcontent.feature.profile.view.subscriber;

import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;

import rx.Subscriber;

/**
 * @author by milhamj on 28/02/18.
 */

public class LikeKolPostSubscriber extends Subscriber<Boolean> {
    private final KolPostListener.View  view;
    private final int rowNumber;

    public LikeKolPostSubscriber(KolPostListener.View view, int rowNumber) {
        this.view = view;
        this.rowNumber = rowNumber;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.hideLoading();
    }

    @Override
    public void onNext(Boolean isSuccess) {
        view.hideLoading();
    }
}
