package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.ALL_BRAND_HEADER_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.FEATURED_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.NEW_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.POPULAR_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandGroupHeaderViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandHeaderViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.StickyHeaderInterface
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.ALL_BRAND_GRID_SPAN_COUNT
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.BRANDLIST_GRID_SPAN_COUNT

class BrandlistPageAdapter(
        private val adapterTypeFactory: BrandlistPageAdapterTypeFactory,
        private val brandlistPageFragment: BrandlistPageFragment) :
        BaseAdapter<BrandlistPageAdapterTypeFactory>(adapterTypeFactory), StickyHeaderInterface {

    private var recyclerView: RecyclerView? = null

    val spanSizeLookup: GridLayoutManager.SpanSizeLookup by lazy {
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (visitables[position].type(adapterTypeFactory)) {
                    AllBrandViewHolder.LAYOUT -> ALL_BRAND_GRID_SPAN_COUNT
                    else -> BRANDLIST_GRID_SPAN_COUNT
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

    override fun getHeaderLayout(headerPosition: Int): Int {
        return AllBrandGroupHeaderViewHolder.LAYOUT
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return visitables[itemPosition].type(adapterTypeFactory) == AllBrandGroupHeaderViewHolder.LAYOUT
    }

    override fun showLoading() {
        if (!isLoading) {
            if (isShowLoadingMore) {
                visitables.add(loadingMoreModel)
            } else {
                visitables.add(loadingModel)
            }
            recyclerView?.let {
                it.post {
                    notifyItemInserted(visitables.size)
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }
}
