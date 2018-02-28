package com.tokopedia.tkpdcontent.feature.profile.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;

import rx.Subscriber;

/**
 * @author by milhamj on 28/02/18.
 */

public class FollowKolPostSubscriber extends Subscriber<Boolean> {
    private final KolPostListener.View view;
    private final int rowNumber;

    public FollowKolPostSubscriber(KolPostListener.View view, int rowNumber) {
        this.view = view;
        this.rowNumber = rowNumber;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {

        }
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (view != null) {

        }
    }
}
