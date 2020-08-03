package com.tokopedia.deals.brand.mapper

import com.tokopedia.deals.brand.ui.fragment.DealsBrandFragment
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView

object DealsBrandMapper {

    private const val POPULAR = "Terpopuler"

    fun mapBrandToBaseItemViewModel(brands: List<Brand>, page: Int) : DealsBrandsDataView {
        val dealsBrandDataView = DealsBrandsDataView()
        dealsBrandDataView.title = POPULAR
        dealsBrandDataView.brands = mapBrandToDataView(brands, page)
        dealsBrandDataView.isLoadedAndSuccess()
        return dealsBrandDataView
    }

    private fun mapBrandToDataView(brands: List<Brand>, page: Int): List<DealsBrandsDataView.Brand> {
        val resultMap = arrayListOf<DealsBrandsDataView.Brand>()
        for(i in brands.indices) {
            val brandView = DealsBrandsDataView.Brand()
            brandView.id = brands[i].id
            brandView.title = brands[i].title
            brandView.image = brands[i].featuredImage
            brandView.brandUrl = brands[i].seoUrl
            brandView.position = i + (DealsBrandFragment.DEFAULT_MIN_ITEMS * (page - 1))
            resultMap.add(brandView)
        }
        return resultMap
    }

    private fun DealsBaseItemDataView.isLoadedAndSuccess() {
        this.isLoaded = true
        this.isSuccess = true
    }

    fun mapBrandListToBaseItemView(brands: List<DealsBrandsDataView.Brand>, showTitle: Boolean) : List<DealsBrandsDataView>{
        val renderLayout = arrayListOf<DealsBrandsDataView>()
        val dealsBrandDataView = DealsBrandsDataView()
        if(showTitle) {
            dealsBrandDataView.title = POPULAR
        }
        dealsBrandDataView.isLoadedAndSuccess()
        renderLayout.add(dealsBrandDataView)
        dealsBrandDataView.brands = brands
        return renderLayout
    }
}