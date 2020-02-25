package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory

class AllBrandGroupHeaderViewModel(val groupHeaderText: String) : Visitable<BrandlistPageTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistPageTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }
}