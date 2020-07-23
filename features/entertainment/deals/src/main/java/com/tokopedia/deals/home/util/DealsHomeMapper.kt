package com.tokopedia.deals.home.util

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.deals.R
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.ui.dataview.*
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.home.data.EventHomeLayout
import com.tokopedia.deals.home.ui.dataview.*
import com.tokopedia.deals.location_picker.model.response.Location
import javax.inject.Inject
import kotlin.math.min

/**
 * @author by jessica on 19/06/20
 */

class DealsHomeMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun mapLayoutToBaseItemViewModel(homeLayout: List<EventHomeLayout>, brands: List<Brand>,
                                     nearestLocations: List<Location>)
            : List<DealsBaseItemDataView> {

        val layouts = mutableListOf<DealsBaseItemDataView>()

        var bannersDataView = BannersDataView()
        val categoriesDataView = CategoriesDataView()
        val curatedProductCategoryDataViews: MutableList<CuratedProductCategoryDataView> = mutableListOf()
        val brandsDataView: DealsBrandsDataView
        val popularDataView: VoucherPlacePopularDataView

        val foodSection = FavouritePlacesDataView(title = getString(FOOD_SECTION_TITLE), subtitle = getString(FOOD_SECTION_SUBTITLE))

        homeLayout.forEach {
            if (it.title.equals(getString(TYPE_CAROUSEL), true)) {
                val banners = it.productDetails.map { item ->
                    val banner = BannersDataView.BannerDataView(item.id, item.title, item.appUrl, item.imageApp)
                    banner
                }
                bannersDataView = BannersDataView(list = banners, seeAllUrl = ApplinkConst.PROMO_LIST)

            } else if (it.isCard == 0 && it.isHidden == 0) {
                val category = DealsCategoryDataView(id = it.id, imageUrl = it.mediaUrl, title = it.title, appUrl = it.appUrl)
                categoriesDataView.list.add(category)

            } else if (it.isCard == 1) {
                if (curatedProductCategoryDataViews.size < MAX_CURATED_PRODUCT_SECTION) {

                    val isPopular = curatedProductCategoryDataViews.size == 0

                    val productCategoryDataView = if (isPopular) {
                        ProductCategoryDataView(getString(R.string.deals_homepage_popular_tag),
                                com.tokopedia.unifyprinciples.R.color.Yellow_Y400)
                    } else {
                        ProductCategoryDataView(getString(R.string.deals_homepage_new_deals_tag),
                                com.tokopedia.unifyprinciples.R.color.Blue_B500)
                    }

                    val items = it.productDetails.subList(0,
                            min(MAX_ITEM_ON_CURATED_SECTION_ITEMS, it.productDetails.size)).map { product ->
                        ProductCardDataView(
                                id = product.id,
                                imageUrl = product.imageApp,
                                title = product.title,
                                discount = product.savingPercentage,
                                oldPrice = DealsUtils.convertToCurrencyString(product.mrp.toLong()),
                                price = DealsUtils.convertToCurrencyString(product.salesPrice.toLong()),
                                shop = product.brand.title,
                                appUrl = product.appUrl,
                                productCategory = productCategoryDataView
                        )
                    }

                    curatedProductCategoryDataViews.add(CuratedProductCategoryDataView(
                            title = it.title,
                            subtitle = it.description,
                            productCards = items,
                            hasSeeAllButton = items.size >= 4,
                            seeAllUrl = it.appUrl
                    ))

                } else if (!it.url.equals(getString(TYPE_TOPDEALS), true) && it.isHidden == 0) {
                    if (foodSection.places.size <= 5) {
                        foodSection.places.add(FavouritePlacesDataView.Place(name = it.title, imageUrl = it.mediaUrl, url = it.appUrl))
                    }
                }
            }
        }

        val brandLayout = brands.map { brand ->
            DealsBrandsDataView.Brand(brand.id.toString(), brand.title, brand.featuredThumbnailImage, brand.seoUrl)
        }
        brandsDataView = DealsBrandsDataView(title = getString(BRAND_POPULAR_SECTION_TITLE),
                seeAllText = getString(DEALS_SEE_ALL), brands = brandLayout)

        val popularPlaces = nearestLocations.map { location ->
            VoucherPlaceCardDataView(
                    id = location.id.toString(),
                    imageUrl = location.imageApp,
                    name = location.name,
                    location = location
            )
        }
        popularDataView = VoucherPlacePopularDataView(title = getString(PLACE_POPULAR_SECTION_TITLE),
                subtitle = getString(PLACE_POPULAR_SECTION_SUBTITLE),
                voucherPlaceCards = popularPlaces.toMutableList())

        if (bannersDataView.list.isNotEmpty()) {
            bannersDataView.isLoadedAndSuccess()
            layouts.add(bannersDataView)
        }

        if (categoriesDataView.list.isNotEmpty()) {
            categoriesDataView.isLoadedAndSuccess()
            layouts.add(categoriesDataView)
        }

        if (brandsDataView.brands.isNotEmpty()) {
            brandsDataView.isLoadedAndSuccess()
            layouts.add(brandsDataView)
        }

        curatedProductCategoryDataViews.forEach {
            if (it.productCards.isNotEmpty()) {
                it.isLoadedAndSuccess()
                layouts.add(it)
            }
        }

        if (popularDataView.voucherPlaceCards.isNotEmpty()) {
            popularDataView.isLoadedAndSuccess()
            layouts.add(popularDataView)
        }

        if (foodSection.places.isNotEmpty()) {
            foodSection.isLoadedAndSuccess()
            layouts.add(foodSection)
        }

        return layouts
    }

    private fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    private fun DealsBaseItemDataView.isLoadedAndSuccess() {
        this.isLoaded = true
        this.isSuccess = true
    }

    companion object {
        private val TYPE_CAROUSEL = R.string.deals_homepage_banner_carousel_tag
        private val TYPE_TOPDEALS = R.string.deals_homepage_layout_topdeals_tag

        private val FOOD_SECTION_TITLE = R.string.deals_homepage_food_section_title
        private val FOOD_SECTION_SUBTITLE = R.string.deals_homepage_food_section_subtitle
        private val PLACE_POPULAR_SECTION_TITLE = R.string.deals_homepage_place_popular_section_title
        private val PLACE_POPULAR_SECTION_SUBTITLE = R.string.deals_homepage_place_popular_section_subtitle
        private val BRAND_POPULAR_SECTION_TITLE = R.string.deals_homepage_brand_popular_section_title
        private val DEALS_SEE_ALL = R.string.deals_homepage_section_see_all

        private const val MAX_ITEM_ON_CURATED_SECTION_ITEMS = 4
        private const val MAX_CURATED_PRODUCT_SECTION = 2
    }
}