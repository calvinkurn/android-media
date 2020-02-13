package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchViewModel

interface BrandlistSearchTypeFactory {

    fun type(brandlistSearchViewModel: BrandlistSearchViewModel): Int

}