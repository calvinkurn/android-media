package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandViewModel

class BrandlistPageAdapter(adapterTypeFactory: BrandlistPageAdapterTypeFactory) :
        BaseAdapter<BrandlistPageAdapterTypeFactory>(adapterTypeFactory) {

    fun initAdapter() {
        visitables.add(BrandlistPageMapper.FEATURED_BRAND_POSITION, FeaturedBrandViewModel(mutableListOf(), null))
        visitables.add(BrandlistPageMapper.POPULAR_BRAND_POSITION, PopularBrandViewModel(mutableListOf(), null))
        visitables.add(BrandlistPageMapper.NEW_BRAND_POSITION, NewBrandViewModel(mutableListOf(), null))
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }
}