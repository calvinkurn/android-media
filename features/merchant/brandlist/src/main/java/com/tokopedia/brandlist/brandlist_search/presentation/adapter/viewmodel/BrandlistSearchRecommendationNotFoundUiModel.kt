package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory

class BrandlistSearchRecommendationNotFoundUiModel : Visitable<BrandlistSearchAdapterTypeFactory> {

    override fun type(typeFactory: BrandlistSearchAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}