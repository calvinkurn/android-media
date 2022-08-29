package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import android.os.Handler
import android.os.Parcelable
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

    val spanSizeLookup: GridLayoutManager.SpanSizeLookup by lazy {
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return try {
                    return when (visitables[position].type(adapterTypeFactory)) {
                        AllBrandViewHolder.LAYOUT -> ALL_BRAND_GRID_SPAN_COUNT
                        else -> BRANDLIST_GRID_SPAN_COUNT
                    }
                } catch (e: IndexOutOfBoundsException) {
                    BRANDLIST_GRID_SPAN_COUNT
                }
            }
        }
    }

    fun initAdapter(recyclerViewLastState: Parcelable?) {
        visitables.add(FEATURED_BRAND_POSITION, FeaturedBrandUiModel(mutableListOf(), null, brandlistPageFragment))
        visitables.add(POPULAR_BRAND_POSITION, PopularBrandUiModel(mutableListOf(), null, brandlistPageFragment))
        visitables.add(NEW_BRAND_POSITION, NewBrandUiModel(mutableListOf(), null, brandlistPageFragment))
        visitables.add(ALL_BRAND_GROUP_HEADER_POSITION, AllBrandGroupHeaderUiModel(brandlistPageFragment, 0, 1, 0, recyclerViewLastState))
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    override fun showLoading() {
        if (!isLoading) {
            if (isShowLoadingMore) {
                visitables.add(loadingMoreModel)
            } else {
                visitables.add(loadingModel)
            }
            recyclerView?.let {
                if (!it.isComputingLayout) {
                    it.post {
                        notifyItemInserted(visitables.size)
                    }
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
            (visitables.get(ALL_BRAND_GROUP_HEADER_POSITION) as? AllBrandGroupHeaderUiModel)?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun updateEtalaseListViewHolderData() {
        if (recyclerView?.isComputingLayout == false) {
            Handler().post {
                notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
            }
        }
    }
}
