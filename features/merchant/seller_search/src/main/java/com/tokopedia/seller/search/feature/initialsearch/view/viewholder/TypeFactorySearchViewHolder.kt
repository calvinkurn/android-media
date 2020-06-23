package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel

interface TypeFactoryInitialSearchAdapter {
    fun type(initialSearchUiModel: InitialSearchUiModel): Int
    fun type(sellerSearchMinCharUiModel: SellerSearchMinCharUiModel): Int
    fun type(sellerSearchNoHistoryUiModel: SellerSearchNoHistoryUiModel): Int
}

interface TypeFactorySuggestionSearchAdapter {
    fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int
    fun type(sellerSearchUiModel: SellerSearchUiModel): Int
    fun type(loadingModel: LoadingSearchModel): Int
}