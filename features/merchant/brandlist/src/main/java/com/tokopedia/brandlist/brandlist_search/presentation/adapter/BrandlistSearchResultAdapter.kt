package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import android.os.Handler
import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.brandlist.brandlist_page.data.model.Brand
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchGroupHeaderViewHolder
//import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.AllBrandGroupHeaderViewHolder
//import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.StickyHeaderInterface
//import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchAllBrandLabelViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.common.LoadAllBrandState
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener
import com.tokopedia.brandlist.common.widget.OnStickySingleHeaderListener
import com.tokopedia.brandlist.common.widget.StickySingleHeaderView

class BrandlistSearchResultAdapter(
        private val adapterTypeFactory: BrandlistSearchAdapterTypeFactory) :
        BaseAdapter<BrandlistSearchAdapterTypeFactory>(adapterTypeFactory), StickySingleHeaderView.OnStickySingleHeaderAdapter {

    companion object {
        private const val numberOfShimmeringCards = 5
        private const val ALL_BRAND_GROUP_HEADER_POSITION = 0
    }

    private var recyclerView: RecyclerView? = null
    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }

    fun updateSearchResultData(searchResultList: List<BrandlistSearchResultViewModel>) {
        visitables.clear()
        visitables.addAll(searchResultList)
        notifyDataSetChanged()
    }

    fun updateSearchRecommendationData(searchRecommendationList: List<BrandlistSearchRecommendationViewModel>) {
        visitables.clear()
        visitables.add(BrandlistSearchNotFoundViewModel())
        if (searchRecommendationList.isNotEmpty()) {
            visitables.add(BrandlistSearchHeaderViewModel(BrandlistSearchHeaderViewModel.RECOMMENDATION_HEADER, null))
            visitables.addAll(searchRecommendationList)
        }
        notifyDataSetChanged()
    }

    fun updateAllBrandsValue(totalBrands: Int) {
        visitables.clear()
        visitables.add(BrandlistSearchHeaderViewModel(BrandlistSearchHeaderViewModel.TOTAL_BRANDS_HEADER, totalBrands))
        notifyDataSetChanged()
    }

    fun updateHeaderChipsBrandSearch(listener: BrandlistHeaderBrandInterface, totalBrands: Int, selectedChip: Int, recyclerViewLastState: Parcelable?) {
        val filteredList = getVisitables().filterIsInstance<BrandlistSearchResultViewModel>()
        if (filteredList.size == 0) {
            visitables.add(ALL_BRAND_GROUP_HEADER_POSITION, BrandlistSearchAllBrandGroupHeaderViewModel(listener, totalBrands, selectedChip, recyclerViewLastState))
            notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
        } else if (filteredList.size > 0) {
            visitables.set(ALL_BRAND_GROUP_HEADER_POSITION, BrandlistSearchAllBrandGroupHeaderViewModel(listener, totalBrands, selectedChip, recyclerViewLastState))
            notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)

//            BrandlistSearchAllBrandGroupHeaderViewModel(listener, totalBrands, selectedChip)
//            notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
        }
    }

    fun updateBrands(
            searchResultList: List<BrandlistSearchResultViewModel>,
            stateLoadBrands: String,
            isLoadMore: Boolean
    ) {
//        val totalData: Int = getVisitables().size ?: 0
//        val _list = getVisitables().filterIsInstance<BrandlistSearchResultViewModel>()
//        val _startIndex = getVisitables().indexOf(list.first())
//        val _lastIndex = getVisitables().indexOf(list.last())
        val _totalUnusedData = getVisitables().size - 1
        val _startIndex = 1

        if (stateLoadBrands == LoadAllBrandState.LOAD_BRAND_PER_ALPHABET) {
            if (!isLoadMore) {
                getVisitables().subList(_startIndex, _totalUnusedData).clear()
                visitables.addAll(_startIndex, searchResultList)
                notifyItemRangeRemoved(_startIndex, _totalUnusedData)
                notifyItemRangeInserted(_startIndex, searchResultList.size)
            } else {
                visitables.addAll(lastIndex, searchResultList)
                notifyItemRangeInserted(lastIndex, searchResultList.size)
            }

        } else if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND) {
            if (!isLoadMore) {
                getVisitables().subList(_startIndex, _totalUnusedData).clear()
                visitables.addAll(_startIndex, searchResultList)
                notifyItemRangeRemoved(_startIndex, _totalUnusedData)
                notifyItemRangeInserted(_startIndex, searchResultList.size)
            } else {
                visitables.addAll(lastIndex, searchResultList)
                notifyItemRangeInserted(lastIndex, searchResultList.size)
            }

        } else if (stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) {
            getVisitables().addAll(searchResultList)
            notifyItemRangeInserted(lastIndex, searchResultList.size)
        }
    }

    fun showShimmering() {
        visitables.clear()
        for (i in 0 until numberOfShimmeringCards) {
            visitables.add(BrandlistSearchShimmeringViewModel())
        }
        notifyDataSetChanged()
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

//    val loadingModelPosition = this.visitables.indexOf(loadingMoreModel as Visitable<CatalogTypeFactory>)

    override val stickyHeaderPosition: Int
        get() = visitables.indexOf(visitables.get(ALL_BRAND_GROUP_HEADER_POSITION))

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return adapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is BrandlistSearchGroupHeaderViewHolder) {
            (visitables.get(ALL_BRAND_GROUP_HEADER_POSITION) as? BrandlistSearchAllBrandGroupHeaderViewModel)?.let {
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
