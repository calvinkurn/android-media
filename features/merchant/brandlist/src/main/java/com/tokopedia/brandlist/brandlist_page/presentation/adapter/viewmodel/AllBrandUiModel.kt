package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.data.model.Brand
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class AllBrandUiModel(
        val index: Int,
        val brand: Brand,
        val listener: BrandlistPageTrackingListener) : Visitable<BrandlistPageTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistPageTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}