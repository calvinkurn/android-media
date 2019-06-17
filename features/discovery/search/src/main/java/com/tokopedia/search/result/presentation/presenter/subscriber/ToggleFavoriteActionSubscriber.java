package com.tokopedia.search.result.presentation.presenter.subscriber;

import com.tokopedia.search.result.presentation.view.listener.FavoriteActionListener;

import rx.Subscriber;

public class ToggleFavoriteActionSubscriber extends Subscriber<Boolean> {

    private final FavoriteActionListener viewListener;
    private int adapterPosition;
    private boolean targetFavoritedStatus;

    public ToggleFavoriteActionSubscriber(FavoriteActionListener viewListener,
                                          int adapterPosition,
                                          boolean targetFavoritedStatus) {
        this.viewListener = viewListener;
        this.adapterPosition = adapterPosition;
        this.targetFavoritedStatus = targetFavoritedStatus;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorToggleFavorite(e, adapterPosition);
    }

    @Override
    public void onNext(Boolean isSuccess) {
        if (isSuccess)
            viewListener.onSuccessToggleFavorite(adapterPosition, targetFavoritedStatus);
        else
            viewListener.onErrorToggleFavorite(adapterPosition);
    }
}