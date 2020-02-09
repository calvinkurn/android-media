package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.data.model.Header
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory

class AllBrandViewModel(val allBrands: List<Shop>, val header: Header?) : Visitable<BrandlistPageTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistPageTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}