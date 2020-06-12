package com.tokopedia.seller.search.feature.initialsearch.view.model

import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.InitialSearchAdapterTypeFactory

class SellerSearchMinCharUiModel: BaseGlobalSearchSeller {
    override fun type(typeFactory: InitialSearchAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}