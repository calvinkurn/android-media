package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.StickyHeaderInterface
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder.BrandlistSearchAllBrandLabelViewHolder
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*

class BrandlistSearchResultAdapter(
        private val adapterTypeFactory: BrandlistSearchAdapterTypeFactory) :
        BaseAdapter<BrandlistSearchAdapterTypeFactory>(adapterTypeFactory), StickyHeaderInterface {

    companion object {
        private const val numberOfShimmeringCards = 5
    }

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

    override fun getHeaderLayout(headerPosition: Int): Int {
        return BrandlistSearchAllBrandLabelViewHolder.LAYOUT
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return visitables[itemPosition].type(adapterTypeFactory) == BrandlistSearchAllBrandLabelViewHolder.LAYOUT
    }
}
