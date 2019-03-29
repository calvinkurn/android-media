package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolPostSubscriber extends Subscriber<Boolean> {
    private final FeedPlus.View view;
    private final FeedPlus.View.Kol kolListener;
    private final int rowNumber;

    public LikeKolPostSubscriber(int rowNumber, FeedPlus.View view, FeedPlus.View.Kol kolListener) {
        this.view = view;
        this.kolListener = kolListener;
        this.rowNumber = rowNumber;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        kolListener.onErrorLikeDislikeKolPost(ErrorHandler.getErrorMessage(view.getContext(), e));

    }

    @Override
    public void onNext(Boolean likeSuccess) {
        if (likeSuccess) {
            kolListener.onSuccessLikeDislikeKolPost(rowNumber);
        } else {
            kolListener.onErrorLikeDislikeKolPost(view.getContext().getString(R
                    .string.default_request_error_unknown));
        }
    }
}
