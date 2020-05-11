package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface

class AllBrandGroupHeaderViewModel(
        val groupHeaderText: String,
        val listener: BrandlistHeaderBrandInterface
) : Visitable<BrandlistPageTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistPageTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}