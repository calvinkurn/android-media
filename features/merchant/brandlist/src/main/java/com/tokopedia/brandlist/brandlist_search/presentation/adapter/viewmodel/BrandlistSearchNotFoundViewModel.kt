package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchAdapterTypeFactory

class BrandlistSearchNotFoundViewModel : Visitable<BrandlistSearchAdapterTypeFactory> {

    override fun type(adaptertypeFactory: BrandlistSearchAdapterTypeFactory): Int {
        return adaptertypeFactory.type(this)
    }

}