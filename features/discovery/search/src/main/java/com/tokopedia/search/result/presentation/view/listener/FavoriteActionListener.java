package com.tokopedia.search.result.presentation.view.listener;

public interface FavoriteActionListener {

    void onErrorToggleFavorite(Throwable throwable, int adapterPosition);

    void onErrorToggleFavorite(int adapterPosition);

    void onSuccessToggleFavorite(int adapterPosition, boolean targetFavoritedStatus);
}
