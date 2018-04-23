package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;

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
        if (view != null) {
            view.onLikeKolError(
                    ErrorHandler.getErrorMessage(view.getContext(), e)
            );
        }
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (view != null) {
            if (isSuccess) {
                view.onLikeKolSuccess(rowNumber);
            } else {
                view.onLikeKolError(null);
            }
        }
    }
}
