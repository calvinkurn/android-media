package com.tokopedia.discovery.categoryrevamp.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

lateinit var categoryPageAnalytics: CategoryPageAnalytics

class CategoryPageAnalytics {

    companion object {
        val catAnalyticsInstance: CategoryPageAnalytics by lazy { CategoryPageAnalytics() }
    }


    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun eventBackButtonClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click back button",
                "eventLabel", "",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventSearchBarClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click search bar",
                "eventLabel", "",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventDisplayButtonClicked(category_id: String, displayName: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click display",
                "eventLabel", displayName,
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickProductTab(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click product tab",
                "eventLabel", "produk",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickCatalogTab(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click catalog tab",
                "eventLabel", "katalog",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSemuaThumbnail(category_id: String, displayName: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click category thumbnail",
                "eventLabel", "",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSubCategory(sub_category_id: String
                              , category_id: String,
                              id: String,
                              creative_name: String,
                              creative_url: String,
                              position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "category page",
                "eventAction", "click subcategory",
                "eventLabel", "$sub_category_id-$creative_name",
                "categoryId", category_id,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", id,
                "name", "promo slot name",
                "creative", creative_name,
                "creative_url", creative_url,
                "position", position,
                "promo_id", "",
                "promo_code", "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventSubCategoryImpression(category_id: String,
                                   id: String,
                                   creative_name: String,
                                   creative_url: String,
                                   position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "category page",
                "eventAction", "subcategory impression",
                "eventLabel", "",
                "categoryId", category_id,
                "ecommerce", DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", id,
                "name", "promo slot name",
                "creative", creative_name,
                "creative_url", creative_url,
                "position", position,
                "category", "",
                "promo_id", "",
                "promo_code", "")))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickProductList(product_id: String,
                              category_id: String,
                              productName: String,
                              price: Int,
                              position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "category page",
                "eventAction", "click product list",
                "eventLabel", product_id,
                "categoryId", category_id,
                "ecommerce", DataLayer.mapOf(
                "click", DataLayer.mapOf(
                "actionField", DataLayer.mapOf(
                "list", "category/$category_id")),
                "products", DataLayer.listOf(DataLayer.mapOf(
                "name", productName,
                "id", product_id,
                "price", price,
                "brand", "",
                "variant", "",
                "list", "category/$category_id",
                "position", "",
                "attribution", position)))
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    fun eventProductListImpression(category_id: String,
                                   product_name: String,
                                   product_id: String,
                                   price: Int,
                                   position: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "category page",
                "eventAction", "product list impression",
                "eventLabel", "",
                "categoryId", category_id,
                "ecommerce", DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", DataLayer.listOf(DataLayer.mapOf(
                "name", product_name,
                "id", product_id,
                "price", price,
                "brand", "",
                "category", "",
                "variant", "",
                "list", "category/$category_id",
                "position", position))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventWishistClicked(category_id: String, product_id: String, isWishlisted: Boolean) {
        val tracker = getTracker()
        val eventAction: String = if (isWishlisted) {
            "add"
        } else {
            "remove"
        }
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", eventAction,
                "eventLabel", product_id,
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventSortClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click sort",
                "eventLabel", "",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventFilterClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click filter",
                "eventLabel", "",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventQuickFilterClicked(category_id: String, filterName: String, filterValue: Boolean) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "quick filter",
                "eventLabel", "$filterName-$filterValue",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventFilterApplied(category_id: String, filterName: String, filterValue: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "apply filter",
                "eventLabel", "$filterName-$filterValue",
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventFilterCategoryChoosen(category_id: String, filterName: String, filterValue: Boolean) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "click$filterName:$filterValue",
                "eventLabel", filterValue,
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventSortApplied(category_id: String, sortName: String, sortValue: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                "event", "clickCategory",
                "eventCategory", "category page",
                "eventAction", "apply sort - $sortName",
                "eventLabel", sortValue,
                "categoryId", category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

}