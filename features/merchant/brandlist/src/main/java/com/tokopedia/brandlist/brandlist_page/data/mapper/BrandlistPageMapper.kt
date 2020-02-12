package com.tokopedia.brandlist.brandlist_page.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreFeaturedShop
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*

class BrandlistPageMapper {

    companion object {

        const val FEATURED_BRAND_POSITION = 0
        const val POPULAR_BRAND_POSITION = 1
        const val NEW_BRAND_POSITION = 2
        const val ALL_BRAND_HEADER_POSITION = 3

        fun mappingFeaturedBrand(featuredBrand: OfficialStoreFeaturedShop, adapter: BrandlistPageAdapter?) {
            notifyElement(FEATURED_BRAND_POSITION, FeaturedBrandViewModel(featuredBrand.shops, featuredBrand.header), adapter)
        }

        fun mappingPopularBrand(popularBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?) {
            notifyElement(POPULAR_BRAND_POSITION, PopularBrandViewModel(popularBrand.shops, popularBrand.header), adapter)
        }

        fun mappingNewBrand(newBrand: OfficialStoreBrandsRecommendation, adapter: BrandlistPageAdapter?) {
            notifyElement(NEW_BRAND_POSITION, NewBrandViewModel(newBrand.shops, newBrand.header), adapter)
        }

        fun mappingAllBrandHeader(title: String, totalBrands: Int, adapter: BrandlistPageAdapter?) {
            notifyElement(ALL_BRAND_HEADER_POSITION, AllBrandHeaderViewModel(title, totalBrands), adapter)
        }

        fun mappingAllBrand(allBrand: OfficialStoreAllBrands, adapter: BrandlistPageAdapter?) {
            allBrand.brands.forEach {
                adapter?.getVisitables()?.add(AllBrandViewModel(it))
            }
            adapter?.notifyItemRangeInserted(adapter.lastIndex, allBrand.brands.size)
        }

        private fun notifyElement(position: Int, element: Visitable<*>, adapter: BrandlistPageAdapter?) {
            adapter?.getVisitables()?.set(position, element)
            adapter?.notifyItemChanged(position)
        }
    }
}