package com.tokopedia.brandlist.brandlist_page.data.mapper

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
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
        const val ALL_BRAND_GROUP_HEADER_POSITION = 3
        const val ALL_BRAND_POSITION = 4

        fun mappingFeaturedBrand(featuredBrand: OfficialStoreFeaturedShop, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(FEATURED_BRAND_POSITION, FeaturedBrandUiModel(featuredBrand.shops, featuredBrand.header, listener), adapter)
        }

        fun mappingPopularBrand(popularBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(POPULAR_BRAND_POSITION, PopularBrandUiModel(popularBrand.shops, popularBrand.header, listener), adapter)
        }

        fun mappingNewBrand(newBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(NEW_BRAND_POSITION, NewBrandUiModel(newBrand.shops, newBrand.header, listener), adapter)
        }

        fun mappingAllBrandGroupHeader(
                adapter: BrandlistPageAdapter?,
                listener: BrandlistHeaderBrandInterface,
                totalBrands: Int,
                selectedChip: Int,
                lastTimeClicked: Long,
                recyclerViewLastState: Parcelable?
        ) {
            adapter?.let {
                it.getVisitables().set(ALL_BRAND_GROUP_HEADER_POSITION, AllBrandGroupHeaderUiModel(
                        listener, totalBrands, selectedChip, lastTimeClicked, recyclerViewLastState))
                it.notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
                it.refreshSticky()
            }
        }

        fun mappingLoadingBrandRecomm(adapter: BrandlistPageAdapter?) {
            adapter?.let {
                it.getVisitables().add(ALL_BRAND_POSITION, LoadingModel())
                it.notifyItemChanged(ALL_BRAND_POSITION, LoadingModel())
            }
        }

        fun mappingRemoveBrandRecom(adapter: BrandlistPageAdapter?) {
            val _totalAllBrandViewModel: Int = adapter?.getVisitables()?.filterIsInstance<AllBrandUiModel>()?.size ?: 0
            if (_totalAllBrandViewModel > 0) {
                val _totalUnusedData: Int = adapter?.getVisitables()?.size ?: 0
                adapter?.let {
                    it.getVisitables().subList(ALL_BRAND_POSITION, _totalUnusedData).clear()
                    it.notifyItemRangeRemoved(ALL_BRAND_POSITION, _totalAllBrandViewModel)
                }
            }
        }

        fun resetAllBrandData(adapter: BrandlistPageAdapter?) {
            adapter?.let {
                if (it.lastIndex >= ALL_BRAND_POSITION) {
                    it.getVisitables().subList((ALL_BRAND_POSITION-1), adapter.lastIndex).clear()
                    it.notifyItemRangeRemoved(ALL_BRAND_POSITION, adapter.lastIndex)
                }
            }
        }

        fun mappingBrandNotFound(
                allBrand: OfficialStoreAllBrands,
                isLoadMore: Boolean,
                adapter: BrandlistPageAdapter?
        ) {
            val totalBrands: Int = allBrand.brands.size
            adapter?.hideLoading()
            if (!isLoadMore && totalBrands == 0 && adapter?.getVisitables()?.find { it is AllbrandNotFoundUiModel } == null) {
                adapter?.getVisitables()?.add(ALL_BRAND_POSITION, AllbrandNotFoundUiModel())
                adapter?.notifyItemChanged(ALL_BRAND_POSITION, AllbrandNotFoundUiModel())
            }
        }

        fun mappingAllBrand(
                allBrand: OfficialStoreAllBrands,
                adapter: BrandlistPageAdapter?,
                listener: BrandlistPageTrackingListener,
                stateLoadBrands: String,
                isLoadMore: Boolean
        ) {
            if (stateLoadBrands == LoadAllBrandState.LOAD_BRAND_PER_ALPHABET) {
                if (!isLoadMore) {
                    adapter?.let {
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(ALL_BRAND_POSITION, AllBrandUiModel(index, brand, listener))
                        }
                        it.notifyItemChanged(ALL_BRAND_POSITION)
                    }
                } else {
                    adapter?.let {
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(AllBrandUiModel(index, brand, listener))
                        }
                        it.notifyItemRangeInserted(it.lastIndex, allBrand.brands.size)
                    }
                }

            } else if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND) {
                if (!isLoadMore) {
                    adapter?.let {
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(ALL_BRAND_POSITION, AllBrandUiModel(index, brand, listener))
                        }
                        it.notifyItemChanged(ALL_BRAND_POSITION)
                    }
                } else {
                    adapter?.let {
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(AllBrandUiModel(index, brand, listener))
                        }
                        it.notifyItemRangeInserted(it.lastIndex, allBrand.brands.size)
                    }
                }

            } else if (stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) {
                getLoadMoreData(allBrand, listener, adapter)
            }
        }

        private fun getLoadMoreData(allBrand: OfficialStoreAllBrands,
                                    listener: BrandlistPageTrackingListener, adapter: BrandlistPageAdapter?) {
            adapter?.let {
                allBrand.brands.forEachIndexed { index, brand ->
                    it.getVisitables().add(AllBrandUiModel(index, brand, listener))
                }
                it.notifyItemRangeInserted(adapter.lastIndex, allBrand.brands.size)
            }
        }

        private fun notifyElement(position: Int, element: Visitable<*>, adapter: BrandlistPageAdapter?) {
            adapter?.getVisitables()?.set(position, element)
            adapter?.notifyItemChanged(position)
        }
    }
}