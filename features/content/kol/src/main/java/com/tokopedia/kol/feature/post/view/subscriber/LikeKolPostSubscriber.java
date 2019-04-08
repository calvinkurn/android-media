package com.tokopedia.kol.feature.post.view.subscriber;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.kol.common.network.GraphqlErrorHandler;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;

import rx.Subscriber;

/**
 * @author by milhamj on 28/02/18.
 */

public class LikeKolPostSubscriber extends Subscriber<Boolean> {
    private final KolPostListener.View.Like  view;
    private final int rowNumber;
    private final int action;

    public LikeKolPostSubscriber(KolPostListener.View.Like view, int rowNumber, int action) {
        this.view = view;
        this.rowNumber = rowNumber;
        this.action = action;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.onLikeKolError(
                    GraphqlErrorHandler.getErrorMessage(view.getContext(), e)
            );
        }
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (view != null) {
            if (isSuccess) {
                view.onLikeKolSuccess(rowNumber, action);
            } else {
                view.onLikeKolError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        }
    }
}
