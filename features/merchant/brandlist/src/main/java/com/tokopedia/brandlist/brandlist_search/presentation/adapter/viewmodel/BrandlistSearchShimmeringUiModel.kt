package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory

class BrandlistSearchShimmeringUiModel : Visitable<BrandlistSearchAdapterTypeFactory> {

    override fun type(adapterTypeFactory: BrandlistSearchAdapterTypeFactory): Int {
        return adapterTypeFactory.type(this)
    }

}