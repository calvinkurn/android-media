package com.tokopedia.brandlist.brandlist_page.data.mapper

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.AllBrandNotFoundViewHolder
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
            notifyElement(FEATURED_BRAND_POSITION, FeaturedBrandViewModel(featuredBrand.shops, featuredBrand.header, listener), adapter)
        }

        fun mappingPopularBrand(popularBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(POPULAR_BRAND_POSITION, PopularBrandViewModel(popularBrand.shops, popularBrand.header, listener), adapter)
        }

        fun mappingNewBrand(newBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?, listener: BrandlistPageTrackingListener) {
            notifyElement(NEW_BRAND_POSITION, NewBrandViewModel(newBrand.shops, newBrand.header, listener), adapter)
        }

        fun mappingAllBrandGroupHeader(adapter: BrandlistPageAdapter?, listener: BrandlistHeaderBrandInterface, totalBrands: Int, selectedChip: Int, recyclerViewLastState: Parcelable?) {
            adapter?.getVisitables()?.set(ALL_BRAND_GROUP_HEADER_POSITION, AllBrandGroupHeaderViewModel(listener, totalBrands, selectedChip, recyclerViewLastState))
            adapter?.notifyItemChanged(ALL_BRAND_GROUP_HEADER_POSITION)
        }

        fun mappingLoadingBrandRecomm(adapter: BrandlistPageAdapter?) {
            adapter?.getVisitables()?.add(ALL_BRAND_POSITION, LoadingModel())
            adapter?.notifyItemChanged(ALL_BRAND_POSITION, LoadingModel())

            val _totalAllBrandViewModel: Int = adapter?.getVisitables()?.filterIsInstance<AllBrandViewModel>()?.size
                    ?: 0
            if (_totalAllBrandViewModel > 0) {
                val _totalUnusedData: Int = adapter?.getVisitables()?.size ?: 0
                adapter?.let {
                    it.getVisitables().subList(ALL_BRAND_POSITION + 2, _totalUnusedData).clear()
                    it.notifyItemRangeRemoved(ALL_BRAND_POSITION + 2, _totalAllBrandViewModel)
                }
            }
        }

        fun mappingRemoveLoadingBrandRecomm(adapter: BrandlistPageAdapter?) {
            val _totalLoadingViewModel: Int = adapter?.getVisitables()?.filterIsInstance<LoadingModel>()?.size
                    ?: 0
            if (_totalLoadingViewModel > 0) {
                adapter?.let {
                    it.getVisitables().subList(ALL_BRAND_POSITION, ALL_BRAND_POSITION + 1).clear()
                    it.notifyItemRangeRemoved(ALL_BRAND_POSITION, _totalLoadingViewModel)
                }
            }
        }

        fun mappingBrandNotFound(
                allBrand: OfficialStoreAllBrands,
                isLoadMore: Boolean,
                adapter: BrandlistPageAdapter?
        ) {
            val totalBrands: Int = allBrand.brands.size

            if (!isLoadMore && totalBrands == 0) {
                adapter?.getVisitables()?.add(ALL_BRAND_POSITION, AllbrandNotFoundViewModel())
                adapter?.notifyItemChanged(ALL_BRAND_POSITION, AllbrandNotFoundViewModel())

                adapter?.let {
                    val _totalLoadingViewModel: Int = it.getVisitables().filterIsInstance<LoadingModel>().size ?: 0
                    val _totalAllBrandViewModel: Int = it.getVisitables().filterIsInstance<AllBrandViewModel>().size ?: 0

                    if (_totalLoadingViewModel > 0 && it.getVisitables().size > ALL_BRAND_POSITION) {
                        val _totalData: Int = it.getVisitables().size ?: 0
                        val _totalUpdated = _totalData - (ALL_BRAND_POSITION + 1)
                        it.getVisitables().subList(ALL_BRAND_POSITION + 1, _totalData).clear()
                        it.notifyItemRangeRemoved(ALL_BRAND_POSITION + 1, _totalUpdated)
                    }

                    if (_totalAllBrandViewModel > 0 && it.getVisitables().size > ALL_BRAND_POSITION) {
                        val _totalData: Int = it.getVisitables().size ?: 0
                        val _totalUpdated = _totalData - (ALL_BRAND_POSITION + 1)
                        it.getVisitables().subList(ALL_BRAND_POSITION + 1, _totalData).clear()
                        it.notifyItemRangeRemoved(ALL_BRAND_POSITION + 1, _totalUpdated)
                    }
                }
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
                        it.getVisitables().subList(ALL_BRAND_POSITION, it.getVisitables().size).clear()
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(ALL_BRAND_POSITION, AllBrandViewModel(index, brand, listener))
                        }
                        it.notifyItemRangeRemoved(ALL_BRAND_POSITION, it.getVisitables().size)
                        it.notifyItemChanged(ALL_BRAND_POSITION)

                        val _totalLoadingViewModel: Int = it.getVisitables().filterIsInstance<LoadingModel>().size ?: 0
                        if (_totalLoadingViewModel > 0 && it.getVisitables().size > ALL_BRAND_POSITION) {
                            val _position = it.getVisitables().indexOf(LoadingModel())
                            it.getVisitables().remove(LoadingModel())
                            it.notifyItemRangeRemoved(_position, _totalLoadingViewModel)
                        }
                    }
                } else {
                    adapter?.let {
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(AllBrandViewModel(index, brand, listener))
                        }
                        it.notifyItemRangeInserted(it.lastIndex, allBrand.brands.size)
                    }
                }

            } else if (stateLoadBrands == LoadAllBrandState.LOAD_ALL_BRAND) {
                if (!isLoadMore) {
                    adapter?.let {
                        it.getVisitables().subList(ALL_BRAND_POSITION, it.getVisitables().size).clear()
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(ALL_BRAND_POSITION, AllBrandViewModel(index, brand, listener))
                        }
                        it.notifyItemRangeRemoved(ALL_BRAND_POSITION, it.getVisitables().size)
                        it.notifyItemRangeInserted(ALL_BRAND_POSITION, allBrand.brands.size)

                        val _totalLoadingViewModel: Int = it.getVisitables().filterIsInstance<LoadingModel>().size ?: 0
                        if (_totalLoadingViewModel > 0 && it.getVisitables().size > ALL_BRAND_POSITION) {
                            val _position = it.getVisitables().indexOf(LoadingModel())
                            it.getVisitables().remove(LoadingModel())
                            it.notifyItemRangeRemoved(_position, _totalLoadingViewModel)
                        }
                    }
                } else {
                    adapter?.let {
                        allBrand.brands.forEachIndexed { index, brand ->
                            it.getVisitables().add(AllBrandViewModel(index, brand, listener))
                        }
                        it.notifyItemRangeInserted(it.lastIndex, allBrand.brands.size)
                    }
                }

            } else if (stateLoadBrands == LoadAllBrandState.LOAD_INITIAL_ALL_BRAND) {
                getLoadMoreData(allBrand, listener, adapter)
            }

//            adapter?.let {
//                val _totalLoadingViewModel: Int = it.getVisitables().filterIsInstance<LoadingModel>().size ?: 0
//                if (_totalLoadingViewModel > 0 && it.getVisitables().size > ALL_BRAND_POSITION) {
//                    val _position = it.getVisitables().indexOf(LoadingModel())
//                    it.getVisitables().remove(LoadingModel())
//                    it.notifyItemRangeRemoved(_position, _totalLoadingViewModel)
//                }
//            }
        }

        private fun getLoadMoreData(allBrand: OfficialStoreAllBrands,
                                    listener: BrandlistPageTrackingListener, adapter: BrandlistPageAdapter?) {
            adapter?.let {
                allBrand.brands.forEachIndexed { index, brand ->
                    it.getVisitables().add(AllBrandViewModel(index, brand, listener))
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