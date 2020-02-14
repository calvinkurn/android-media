package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*

class BrandlistSearchResultAdapter(adapterTypeFactory: BrandlistSearchAdapterTypeFactory) :
        BaseAdapter<BrandlistSearchAdapterTypeFactory>(adapterTypeFactory) {

    private val numberOfShimmeringCards = 5

    fun updateSearchResultData(searchResultList: List<BrandlistSearchResultViewModel>) {
        visitables.clear()
        visitables.addAll(searchResultList)
        notifyDataSetChanged()
    }

    fun updateSearchRecommendationData(searchRecommendationList: List<BrandlistSearchRecommendationViewModel>) {
        visitables.clear()
        visitables.add(BrandlistSearchNotFoundViewModel())
        if(searchRecommendationList.isNotEmpty()) {
            visitables.add(BrandlistSearchRecommendationTextViewModel())
            visitables.addAll(searchRecommendationList)
        }
        notifyDataSetChanged()
    }

    fun showShimmering() {
        visitables.clear()
        for(i in 0 until numberOfShimmeringCards) {
            visitables.add(BrandlistSearchShimmeringViewModel())
        }
        notifyDataSetChanged()
    }
}
