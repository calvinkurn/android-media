package com.tokopedia.search.result.presentation.view.listener;


import com.tokopedia.search.result.presentation.model.ShopViewModel;

public interface ShopListener {

    void onItemClicked(ShopViewModel.ShopViewItem shopItem, int adapterPosition);

    void onFavoriteButtonClicked(ShopViewModel.ShopViewItem shopItem,
                                 int adapterPosition);
}