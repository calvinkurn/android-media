package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.ALL_BRAND_GROUP_HEADER_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.FEATURED_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.NEW_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.data.mapper.BrandlistPageMapper.Companion.POPULAR_BRAND_POSITION
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandGroupHeaderViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandViewHolder
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.ALL_BRAND_GRID_SPAN_COUNT
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.BRANDLIST_GRID_SPAN_COUNT
import com.tokopedia.brandlist.common.widget.OnStickySingleHeaderListener
import com.tokopedia.brandlist.common.widget.StickySingleHeaderView

class BrandlistPageAdapter(
        private val adapterTypeFactory: BrandlistPageAdapterTypeFactory,
        private val brandlistPageFragment: BrandlistPageFragment) :
        BaseAdapter<BrandlistPageAdapterTypeFactory>(adapterTypeFactory), StickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var recyclerView: RecyclerView? = null
    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
//    var selectedChipsId: Int = 1
//    var selectedEtalaseId: String = ""

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
//        visitables.add(ALL_BRAND_HEADER_POSITION, AllBrandHeaderViewModel("", 0, brandlistPageFragment))
        visitables.add(ALL_BRAND_GROUP_HEADER_POSITION, AllBrandGroupHeaderViewModel(brandlistPageFragment, 0, 1))
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
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

    override val stickyHeaderPosition: Int
        get() = visitables.indexOf(visitables.get(ALL_BRAND_GROUP_HEADER_POSITION))


    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return adapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is AllBrandGroupHeaderViewHolder) {
            (visitables.get(ALL_BRAND_GROUP_HEADER_POSITION) as? AllBrandGroupHeaderViewModel)?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun updateEtalaseListViewHolderData() {
        Handler().post {
            notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
        }
    }
}
