package com.tokopedia.brandlist.brandlist_page.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistHeaderBrandInterface
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.brandlist.common.LoadAllBrandState
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class BrandlistPageMapper {

    companion object {

        const val FEATURED_BRAND_POSITION = 0
        const val POPULAR_BRAND_POSITION = 1
        const val NEW_BRAND_POSITION = 2
//        const val ALL_BRAND_HEADER_POSITION = 3
        const val ALL_BRAND_GROUP_HEADER_POSITION = 3
        const val ALL_BRAND_POSITION = 4

        fun mappingFeaturedBrand(featuredBrand: OfficialStoreFeaturedShop, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(FEATURED_BRAND_POSITION, FeaturedBrandViewModel(featuredBrand.shops, featuredBrand.header, listener), adapter)
        }

        fun mappingPopularBrand(popularBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(POPULAR_BRAND_POSITION, PopularBrandViewModel(popularBrand.shops, popularBrand.header, listener), adapter)
        }

        fun mappingNewBrand(newBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(NEW_BRAND_POSITION, NewBrandViewModel(newBrand.shops, newBrand.header, listener), adapter)
        }

//        fun mappingAllBrandHeader(title: String, totalBrands: Int, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
//            notifyElement(ALL_BRAND_HEADER_POSITION, AllBrandHeaderViewModel(title, totalBrands, listener), adapter)
//        }

        fun mappingAllBrandGroupHeader(adapter: BrandlistPageAdapter?, listener: BrandlistHeaderBrandInterface, totalBrands: Int, selectedChip: Int) {
            adapter?.getVisitables()?.set(ALL_BRAND_GROUP_HEADER_POSITION, AllBrandGroupHeaderViewModel(listener, totalBrands, selectedChip))
            adapter?.notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
            // adapter?.getVisitables()?.add(AllBrandGroupHeaderViewModel(groupHeaderText, listener, totalBrands, totalBrandsPerAlphabet))
            // adapter?.notifyItemRangeInserted(adapter.lastIndex, 1)
        }

        fun mappingAllBrand(
                allBrand: OfficialStoreAllBrands,
                adapter: BrandlistPageAdapter?,
                listener: BrandlistPageTrackingListener,
                stateLoadBrands: String,
                isLoadMore: Boolean
        ) {

            val totalData: Int = adapter?.getVisitables()?.size ?: 0
            if (stateLoadBrands == LoadAllBrandState.LOAD_BRAND_PER_ALPHABET) {
                if (!isLoadMore) {
                    adapter?.getVisitables()?.subList(ALL_BRAND_POSITION, totalData)?.clear()
                    allBrand.brands.forEachIndexed { index, brand ->
                        adapter?.getVisitables()?.add(ALL_BRAND_POSITION, AllBrandViewModel(index, brand, listener))
                    }
                    adapter?.notifyItemRangeRemoved(ALL_BRAND_POSITION, totalData)
                    adapter?.notifyItemChanged(ALL_BRAND_POSITION)
                } else  {
                    allBrand.brands.forEachIndexed { index, brand ->
                        adapter?.getVisitables()?.add(AllBrandViewModel(index, brand, listener))
                    }
                    adapter?.notifyItemRangeInserted(adapter.lastIndex, allBrand.brands.size)
                }

            } else if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND) {
                if (!isLoadMore) {
                    adapter?.getVisitables()?.subList(ALL_BRAND_POSITION, totalData)?.clear()
                    allBrand.brands.forEachIndexed { index, brand ->
                        adapter?.getVisitables()?.add(ALL_BRAND_POSITION, AllBrandViewModel(index, brand, listener))
                    }
                    adapter?.notifyItemRangeRemoved(ALL_BRAND_POSITION, totalData)
                    adapter?.notifyItemRangeInserted(ALL_BRAND_POSITION, allBrand.brands.size)
                } else  {
                    allBrand.brands.forEachIndexed { index, brand ->
                        adapter?.getVisitables()?.add(AllBrandViewModel(index, brand, listener))
                    }
                    adapter?.notifyItemRangeInserted(adapter.lastIndex, allBrand.brands.size)
                }

            } else if (stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) {
                getLoadMoreData(allBrand, listener, adapter)
            }
        }

        private fun getLoadMoreData(allBrand: OfficialStoreAllBrands,
                listener: BrandlistPageTrackingListener, adapter: BrandlistPageAdapter?) {
            allBrand.brands.forEachIndexed { index, brand ->
                adapter?.getVisitables()?.add(AllBrandViewModel(index, brand, listener))
            }
            adapter?.notifyItemRangeInserted(adapter.lastIndex, allBrand.brands.size)
        }

        private fun notifyElement(position: Int, element: Visitable<*>, adapter: BrandlistPageAdapter?) {
            adapter?.getVisitables()?.set(position, element)
            adapter?.notifyItemChanged(position)
        }
    }
}