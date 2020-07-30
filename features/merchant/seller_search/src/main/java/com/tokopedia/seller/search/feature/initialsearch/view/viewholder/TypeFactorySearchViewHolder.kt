package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel

interface TypeFactoryInitialSearchAdapter {
    fun type(initialSearchUiModel: ItemInitialSearchUiModel): Int
    fun type(sellerSearchMinCharUiModel: SellerSearchMinCharUiModel): Int
    fun type(sellerSearchNoHistoryUiModel: SellerSearchNoHistoryUiModel): Int
}

interface TypeFactorySuggestionSearchAdapter {
    fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int
    fun type(sellerSearchUiModel: SellerSearchUiModel): Int
    fun type(loadingModel: LoadingSearchModel): Int
}