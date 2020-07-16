package com.tokopedia.brandlist.brandlist_search.data.mapper

import com.tokopedia.brandlist.brandlist_page.data.model.Brand
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationUiModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultUiModel
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener

class BrandlistSearchMapper {

    companion object {

        fun mapSearchRecommendationResponseToVisitable(
                shops: List<Shop>,
                listener: BrandlistSearchTrackingListener
        ): List<BrandlistSearchRecommendationUiModel> {
            val visitables = mutableListOf<BrandlistSearchRecommendationUiModel>()

            shops.forEachIndexed { index, shop ->
                visitables.add(
                        BrandlistSearchRecommendationUiModel(
                                shop.name,
                                shop.logoUrl,
                                shop.exclusiveLogoUrl,
                                shop.id,
                                shop.url,
                                listener,
                                (index + 1).toString()
                        )
                )
            }
            return visitables
        }

        fun mapSearchResultResponseToVisitable(
                brands: List<Brand>,
                searchQuery: String,
                listener: BrandlistSearchTrackingListener
        ): List<BrandlistSearchResultUiModel> {
            val visitables = mutableListOf<BrandlistSearchResultUiModel>()

            brands.forEachIndexed { index, brand ->
                visitables.add(
                        BrandlistSearchResultUiModel(
                                brand.name, brand.logoUrl, brand.exclusiveLogoURL,
                                searchQuery, brand.appsUrl, brand.id, listener
                        )
                )
            }

            return visitables
        }
    }
}