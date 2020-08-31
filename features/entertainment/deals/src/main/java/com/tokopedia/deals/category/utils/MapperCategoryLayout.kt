package com.tokopedia.deals.category.utils

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.deals.R
import com.tokopedia.deals.brand.model.DealsEmptyDataView
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.*
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.common.utils.DealsUtils.getLabelColor
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.deals.search.model.response.CuratedData
import javax.inject.Inject

class MapperCategoryLayout @Inject constructor(@ApplicationContext private val context: Context) {
    val layout = mutableListOf<DealsBaseItemDataView>()

    fun mapCategoryLayout(
        brandProduct: SearchData, page: Int
    ): List<DealsBaseItemDataView> {
        layout.clear()
        mapBrandtoLayout(brandProduct)
        mapProducttoLayout(brandProduct,page)
        return layout
    }

    fun getEmptyLayout(
            isFilter:Boolean
    ): List<DealsBaseItemDataView> {
        layout.clear()
        layout.add(DealsEmptyDataView(getString(EMPTY_TITLE),
                if(isFilter) getString(EMPTY_DESC_FILTER)  else  getString(EMPTY_DESC),
                isFilter
        ))
        return layout
    }

    private fun mapBrandtoLayout(searchData: SearchData) {
        var maxValue = 0
        if(searchData.eventSearch.brands.size >= MAX_BRAND_SHOWING) maxValue = MAX_BRAND_SHOWING
        else maxValue = searchData.eventSearch.brands.size
        val brandLayout = searchData.eventSearch.brands.subList(0, maxValue).map { brand ->
            DealsBrandsDataView.Brand(
                brand.id,
                brand.title,
                brand.featuredThumbnailImage,
                    UriUtil.buildUri(ApplinkConst.DEALS_BRAND_DETAIL, brand.seoUrl)
            )
        }
        val brandsDataView = DealsBrandsDataView(
            title = getString(BRAND_POPULAR_TITLE),
            seeAllText = getString(BRAND_SEE_ALL_TEXT),
            brands = brandLayout,
            oneRow = true
        )

        brandsDataView.isLoadedAndSuccess()
        layout.add(brandsDataView)
    }

    fun mapProducttoLayout(searchData: SearchData, page:Int): ProductListDataView {
        val productsLayout = searchData.eventSearch.products.map {
            ProductCardDataView(
                    id = it.id,
                    imageUrl = it.thumbnailApp,
                    title = it.displayName,
                    oldPrice = DealsUtils.convertToCurrencyString(it.mrp.toLong()),
                    price = DealsUtils.convertToCurrencyString(it.salesPrice.toLong()),
                    priceNonCurrency = it.salesPrice,
                    brand = it.brand.title,
                    categoryName = it.category.firstOrNull()?.title ?: "",
                    appUrl = it.appUrl,
                    discount = it.savingPercentage,
                    shop = it.brand.title,
                    productCategory = getLabelColor(context, it.displayTags)
                    )
        }

        val productListDataView = ProductListDataView(productsLayout.toMutableList(), page)
        productListDataView.isLoadedAndSuccess()
        layout.add(productListDataView)

        return productListDataView
    }

     fun mapCategoryToChips(categories: List<Category>): List<ChipDataView> {
        val listChipData = mutableListOf<ChipDataView>()
        categories.forEach {
            if (it.isCard == 1 && it.isHidden == 0) listChipData.add(ChipDataView(it.title, it.id))
        }
        return listChipData
    }

    private fun DealsBaseItemDataView.isLoadedAndSuccess() {
        this.isLoaded = true
        this.isSuccess = true
    }

    private fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    companion object {
        private const val MAX_BRAND_SHOWING = 4
        private const val MAX_SIZE_CHIP = 5
        private val BRAND_POPULAR_TITLE = R.string.deals_homepage_brand_popular_section_title
        private val BRAND_SEE_ALL_TEXT = R.string.deals_homepage_banner_see_all

        private val EMPTY_TITLE = R.string.deals_category_empty_title
        private val EMPTY_DESC =  R.string.deals_category_empty_description
        private val EMPTY_DESC_FILTER = R.string.deals_category_empty_desc_filter
    }

}