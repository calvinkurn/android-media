package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageTypeFactory

class AllbrandNotFoundUiModel: Visitable<BrandlistPageTypeFactory> {

    override fun type(typeFactory: BrandlistPageTypeFactory): Int {
        return typeFactory.type(this)
    }
}