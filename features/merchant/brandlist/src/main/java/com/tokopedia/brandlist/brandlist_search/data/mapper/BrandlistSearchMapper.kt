package com.tokopedia.brandlist.brandlist_search.data.mapper

import com.tokopedia.brandlist.brandlist_page.data.model.Brand
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.BrandlistSearchResultAdapter
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultViewModel
import com.tokopedia.brandlist.common.LoadAllBrandState
import com.tokopedia.brandlist.common.listener.BrandlistSearchTrackingListener

class BrandlistSearchMapper {

    companion object {

        fun mapSearchRecommendationResponseToVisitable(
                shops: List<Shop>,
                listener: BrandlistSearchTrackingListener,
                adapter: BrandlistSearchResultAdapter?,
                stateLoadBrands: String,
                isLoadMore: Boolean
        ): List<BrandlistSearchRecommendationViewModel> {
            val visitables = mutableListOf<BrandlistSearchRecommendationViewModel>()

            shops.forEachIndexed { index, shop ->
                visitables.add(
                        BrandlistSearchRecommendationViewModel(
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
                listener: BrandlistSearchTrackingListener,
                adapter: BrandlistSearchResultAdapter?,
                stateLoadBrands: String,
                isLoadMore: Boolean
        ): List<BrandlistSearchResultViewModel> {
            val visitables = mutableListOf<BrandlistSearchResultViewModel>()

            brands.forEachIndexed { index, brand ->
                visitables.add(
                        BrandlistSearchResultViewModel(
                                brand.name, brand.logoUrl, brand.exclusiveLogoURL,
                                searchQuery, brand.appsUrl, brand.id, listener
                        )
                )
            }

            return visitables

//            val totalData: Int = adapter?.getVisitables()?.size ?: 0
//
//            val list = adapter?.getVisitables()?.filterIsInstance<BrandlistSearchResultViewModel>()
////            val startIndex = adapter?.getVisitables()?.indexOf(list?.first())
////            val lastIndex = adapter?.getVisitables()?.indexOf(list?.last())
//
////            adapter?.getVisitables()?.indexOf(
////                    list?.first()
////            )
//
//            if (stateLoadBrands == LoadAllBrandState.LOAD_BRAND_PER_ALPHABET) {
//                if (!isLoadMore) {
//
//                } else {
//                    for (brand in brands) {
//                        visitables.add(BrandlistSearchResultViewModel(
//                                brand.name, brand.logoUrl, brand.exclusiveLogoURL,
//                                searchQuery, brand.appsUrl, brand.id, listener)
//                        )
//                    }
////                    adapter?.notifyItemRangeInserted(adapter.lastIndex, brands.size)
//                }
//
//            } else if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND) {
//                if (!isLoadMore) {
//
//                } else {
//                    for (brand in brands) {
//                        visitables.add(BrandlistSearchResultViewModel(
//                                brand.name, brand.logoUrl, brand.exclusiveLogoURL,
//                                searchQuery, brand.appsUrl, brand.id, listener)
//                        )
//                    }
//                }
//
//            } else if (stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) {
//                brands.forEachIndexed { index, brand ->
//                    visitables.add(
//                            BrandlistSearchResultViewModel(
//                                    brand.name, brand.logoUrl, brand.exclusiveLogoURL,
//                                    searchQuery, brand.appsUrl, brand.id, listener
//                            )
//                    )
//                }
//            }

//            return visitables
        }
    }
}