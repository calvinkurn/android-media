package com.tokopedia.seller.search.feature.initialsearch.view.viewholder

import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel

interface TypeFactoryInitialSearchViewHolder {
    fun type(sellerSearchUiModel: SellerSearchUiModel): Int
}