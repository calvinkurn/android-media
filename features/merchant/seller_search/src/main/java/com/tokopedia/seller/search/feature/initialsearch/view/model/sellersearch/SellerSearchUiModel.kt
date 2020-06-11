package com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch

import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseGlobalSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.InitialSearchAdapterTypeFactory

data class SellerSearchUiModel(
        var id: String? = "",
        var hasMore: Boolean = false,
        var title: String? = "",
        var count: Int? = 0,
        var sellerSearchList: List<ItemSellerSearchUiModel> = listOf()
): BaseGlobalSearchSeller {
    override fun type(typeFactory: InitialSearchAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
