package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchResultViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchViewModel

class BrandlistSearchAdapterTypeFactory (): BaseAdapterTypeFactory(), BrandlistSearchTypeFactory {

    override fun type(brandlistSearchViewModel: BrandlistSearchViewModel): Int {
        return BrandlistSearchResultViewHolder
    }
}