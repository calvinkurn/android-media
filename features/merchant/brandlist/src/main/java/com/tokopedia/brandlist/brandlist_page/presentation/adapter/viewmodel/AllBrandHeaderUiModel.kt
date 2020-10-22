package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class AllBrandHeaderUiModel(
        val title: String?,
        val totalBrands: Int?,
        val listener: BrandlistPageTrackingListener) : Visitable<BrandlistPageTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistPageTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}