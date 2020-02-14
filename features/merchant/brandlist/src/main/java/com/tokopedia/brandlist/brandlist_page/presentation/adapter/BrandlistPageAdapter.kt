package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.ALL_BRAND_HEADER_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.FEATURED_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.NEW_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.POPULAR_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandHeaderViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.ALL_BRAND_GRID_SPAN_COUNT
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.BRANDLIST_GRID_SPAN_COUNT

class BrandlistPageAdapter(adapterTypeFactory: BrandlistPageAdapterTypeFactory) :
        BaseAdapter<BrandlistPageAdapterTypeFactory>(adapterTypeFactory) {

    val spanSizeLookup: GridLayoutManager.SpanSizeLookup by lazy {
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (visitables[position].type(adapterTypeFactory)) {
                    FeaturedBrandViewHolder.LAYOUT -> BRANDLIST_GRID_SPAN_COUNT
                    PopularBrandViewHolder.LAYOUT -> BRANDLIST_GRID_SPAN_COUNT
                    NewBrandViewHolder.LAYOUT -> BRANDLIST_GRID_SPAN_COUNT
                    AllBrandHeaderViewHolder.LAYOUT -> BRANDLIST_GRID_SPAN_COUNT
                    AllBrandGroupHeaderViewHolder.LAYOUT -> BRANDLIST_GRID_SPAN_COUNT
                    else -> ALL_BRAND_GRID_SPAN_COUNT
                }
            }
        }
    }

    fun initAdapter() {
        visitables.add(FEATURED_BRAND_POSITION, FeaturedBrandViewModel(mutableListOf(), null))
        visitables.add(POPULAR_BRAND_POSITION, PopularBrandViewModel(mutableListOf(), null))
        visitables.add(NEW_BRAND_POSITION, NewBrandViewModel(mutableListOf(), null))
        visitables.add(ALL_BRAND_HEADER_POSITION, AllBrandHeaderViewModel("", 0))
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }
}