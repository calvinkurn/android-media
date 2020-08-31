package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener

class BrandlistSearchResultUiModel(
        val name: String,
        val defaultUrl: String,
        val logoUrl: String,
        val searchQuery: String,
        val appsUrl: String,
        val id: Int,
        val listener: BrandlistSearchTrackingListener
) : Visitable<BrandlistSearchAdapterTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistSearchAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

}
