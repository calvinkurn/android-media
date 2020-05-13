package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchGroupHeaderViewHolder
//import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.AllBrandGroupHeaderViewHolder
//import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.StickyHeaderInterface
//import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchAllBrandLabelViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*
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
//    private val brandlistSearchAllBrandGroupHeaderViewModel: BrandlistSearchAllBrandGroupHeaderViewModel by lazy {
//        BrandlistSearchAllBrandGroupHeaderViewModel(BrandlistHeaderBrandInterface, 0, 1)
//    }

    fun updateSearchResultData(searchResultList: List<BrandlistSearchResultViewModel>) {
        visitables.clear()
        visitables.addAll(searchResultList)
        notifyDataSetChanged()
    }

    fun updateSearchRecommendationData(searchRecommendationList: List<BrandlistSearchRecommendationViewModel>) {
        visitables.clear()
        visitables.add(BrandlistSearchNotFoundViewModel())
        if(searchRecommendationList.isNotEmpty()) {
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

    fun updateHeaderChipsBrandSearch(listener: BrandlistHeaderBrandInterface, totalBrands: Int, selectedChip: Int) {
        visitables.clear()
        visitables.add(ALL_BRAND_GROUP_HEADER_POSITION, BrandlistSearchAllBrandGroupHeaderViewModel(listener, totalBrands, selectedChip))
        notifyDataSetChanged()
    }

    fun updateBrands(searchResultList: List<BrandlistSearchResultViewModel>) {
        visitables.addAll(searchResultList)
        notifyDataSetChanged()
    }

    fun showShimmering() {
        visitables.clear()
        for(i in 0 until numberOfShimmeringCards) {
            visitables.add(BrandlistSearchShimmeringViewModel())
        }
        notifyDataSetChanged()
    }

    fun getVisitables() = this.visitables

//    override fun getHeaderLayout(headerPosition: Int): Int {
//        return BrandlistSearchAllBrandLabelViewHolder.LAYOUT
//    }
//
//    override fun isHeader(itemPosition: Int): Boolean {
//        return visitables[itemPosition].type(adapterTypeFactory) == BrandlistSearchAllBrandLabelViewHolder.LAYOUT
//    }

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
