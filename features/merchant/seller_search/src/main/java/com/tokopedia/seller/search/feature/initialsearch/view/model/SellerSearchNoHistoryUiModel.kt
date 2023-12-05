package com.tokopedia.seller.search.feature.initialsearch.view.model

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter

class SellerSearchNoHistoryUiModel : BaseInitialSearchSeller {
    override fun getUniquePosition(): Int {
        return SELLER_SEARCH_NO_HISTORY_UI_MODEL.hashCode()
    }

    override fun type(typeFactory: TypeFactoryInitialSearchAdapter): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val SELLER_SEARCH_NO_HISTORY_UI_MODEL = "SellerSearchNoHistoryUiModel"
    }
}
