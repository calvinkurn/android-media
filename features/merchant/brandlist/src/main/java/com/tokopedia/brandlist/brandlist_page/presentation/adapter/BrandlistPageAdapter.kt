package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.ALL_BRAND_HEADER_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.FEATURED_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.NEW_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.POPULAR_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandHeaderViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.ALL_BRAND_GRID_SPAN_COUNT

class BrandlistPageAdapter(
        adapterTypeFactory: BrandlistPageAdapterTypeFactory,
        private val brandlistPageFragment: BrandlistPageFragment) :
        BaseAdapter<BrandlistPageAdapterTypeFactory>(adapterTypeFactory) {

    val spanSizeLookup: GridLayoutManager.SpanSizeLookup by lazy {
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    FEATURED_BRAND_POSITION -> ALL_BRAND_GRID_SPAN_COUNT
                    POPULAR_BRAND_POSITION -> ALL_BRAND_GRID_SPAN_COUNT
                    NEW_BRAND_POSITION -> ALL_BRAND_GRID_SPAN_COUNT
                    ALL_BRAND_HEADER_POSITION -> ALL_BRAND_GRID_SPAN_COUNT
                    else -> 1
                }
            }
        }
    }

    fun initAdapter() {
        visitables.add(FEATURED_BRAND_POSITION, FeaturedBrandViewModel(mutableListOf(), null, brandlistPageFragment))
        visitables.add(POPULAR_BRAND_POSITION, PopularBrandViewModel(mutableListOf(), null, brandlistPageFragment))
        visitables.add(NEW_BRAND_POSITION, NewBrandViewModel(mutableListOf(), null, brandlistPageFragment))
        visitables.add(ALL_BRAND_HEADER_POSITION, AllBrandHeaderViewModel("", 0, brandlistPageFragment))
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }
}