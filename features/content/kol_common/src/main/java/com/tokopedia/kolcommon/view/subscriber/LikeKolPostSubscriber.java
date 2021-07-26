package com.tokopedia.kolcommon.view.subscriber;

import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener;
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.network.exception.MessageErrorException;
import com.tokopedia.network.utils.ErrorHandler;

import rx.Subscriber;

/**
 * @author by milhamj on 28/02/18.
 */

public class LikeKolPostSubscriber extends Subscriber<Boolean> {
    private final KolPostLikeListener view;
    private final int rowNumber;
    private final LikeKolPostUseCase.LikeKolPostAction action;

    public LikeKolPostSubscriber(KolPostLikeListener view, int rowNumber, LikeKolPostUseCase.LikeKolPostAction action) {
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
                    ErrorHandler.getErrorMessage(view.getAndroidContext(), e)
            );
        }
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (view != null) {
            if (isSuccess) {
                view.onLikeKolSuccess(rowNumber, action);
            } else {
                view.onLikeKolError(ErrorHandler.getErrorMessage(view.getAndroidContext(), new MessageErrorException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)));
            }
        }
    }
}
