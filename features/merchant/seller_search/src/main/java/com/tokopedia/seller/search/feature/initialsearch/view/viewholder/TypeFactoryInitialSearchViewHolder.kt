package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel

interface TypeFactoryInitialSearchViewHolder {
    fun type(sellerSearchUiModel: SellerSearchUiModel): Int
    fun type(sellerSearchMinCharUiModel: SellerSearchMinCharUiModel): Int
    fun type(sellerSearchNoHistoryUiModel: SellerSearchNoHistoryUiModel): Int
    fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int
}