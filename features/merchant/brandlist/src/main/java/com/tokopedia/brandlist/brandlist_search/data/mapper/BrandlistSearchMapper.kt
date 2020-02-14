package com.tokopedia.brandlist.brandlist_search.data.mapper

import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.brandlist_search.data.model.Brand
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultViewModel
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener

class BrandlistSearchMapper {

    companion object {

        fun mapSearchRecommendationResponseToVisitable(
                shops: List<Shop>,
                listener: BrandlistSearchTrackingListener): List<BrandlistSearchRecommendationViewModel> {
            val visitables = mutableListOf<BrandlistSearchRecommendationViewModel>()
            for (shop in shops) {
                visitables.add(
                        BrandlistSearchRecommendationViewModel(
                                shop.name,
                                shop.logoUrl,
                                shop.imageUrl,
                                listener
                        )
                )
            }
            return visitables
        }

        fun mapSearchResultResponseToVisitable(
                brands: List<Brand>, searchQuery: String,
                listener: BrandlistSearchTrackingListener): List<BrandlistSearchResultViewModel> {
            val visitables = mutableListOf<BrandlistSearchResultViewModel>()
            for (brand in brands) {
                visitables.add(
                        BrandlistSearchResultViewModel(
                                brand.name,
                                brand.defaultUrl,
                                brand.logoUrl,
                                searchQuery,
                                listener
                        )
                )
            }
            return visitables
        }
    }
}