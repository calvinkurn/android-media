package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener

class BrandlistSearchRecommendationUiModel(
        val name: String,
        val logoUrl: String,
        val exclusiveLogoUrl: String,
        val id: Int,
        val url: String,
        val listener: BrandlistSearchTrackingListener,
        val position: String
) : Visitable<BrandlistSearchAdapterTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistSearchAdapterTypeFactory): Int {
       return adapterTypeFactory.type(this)
    }

}