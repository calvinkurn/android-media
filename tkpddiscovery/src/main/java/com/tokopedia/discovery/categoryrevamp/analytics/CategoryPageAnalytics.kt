package com.tokopedia.discovery.categoryrevamp.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.filter.common.data.Option
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics


class CategoryPageAnalytics {

    val KEY_EVENT = "event"
    val KEY_EVENT_CATEGORY = "eventCategory"
    val KEY_EVENT_ACTION = "eventAction"
    val KEY_EVENT_LABEL = "eventLabel"
    val KEY_CATEGORY_ID = "categoryId"
    val KEY_POSITION = "position"
    val KEY_CREATIVE_URL = "creative_url"
    val KEY_ECOMMERCE = "ecommerce"
    val KEY_NAME = "name"
    val KEY_ID = "id"
    val KEY_LIST = "list"
    val KEY_PROMO_CODE = "promo_code"
    val KEY_CATEGORY = "category"
    val KEY_CREATIVE = "creative"

    val EVENT_NAME_VALUE = "clickCategory"
    val EVENT_CATEGORY_VALUE = "category page"

    companion object {
        val catAnalyticsInstance: CategoryPageAnalytics by lazy { CategoryPageAnalytics() }
    }


    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    // 3

    fun eventBackButtonClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click back button",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 4

    fun eventSearchBarClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click search bar",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //5

    fun eventDisplayButtonClicked(category_id: String, displayName: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click display",
                KEY_EVENT_LABEL, displayName,
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 6

    fun eventClickProductTab(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click product tab",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 7

    fun eventClickCatalogTab(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click catalog tab",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 8

    fun eventClickSemuaThumbnail(category_id: String, displayName: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click category thumbnail",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 9

    fun eventClickSubCategory(sub_category_id: String,
                              category_id: String,
                              creative_name: String,
                              creative_url: String,
                              position: Int,
                              path: String,
                              namedPath: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "promoClick",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click subcategory",
                KEY_EVENT_LABEL, "$sub_category_id - $creative_name",
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, sub_category_id,
                KEY_NAME, path,
                KEY_CREATIVE, creative_name,
                KEY_CREATIVE_URL, creative_url,
                KEY_POSITION, position,
                KEY_CATEGORY, namedPath,
                "promo_id", sub_category_id,
                KEY_PROMO_CODE, sub_category_id)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    //10

    fun eventSubCategoryImpression(category_id: String,
                                   sub_id: String,
                                   creative_name: String,
                                   creative_url: String,
                                   position: Int,
                                   path: String,
                                   namePath:String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "promoView",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "subcategory impression",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, sub_id,
                KEY_NAME, path,
                KEY_CREATIVE, creative_name,
                KEY_CREATIVE_URL, creative_url,
                KEY_POSITION, position,
                KEY_CATEGORY, namePath,
                "promo_id", sub_id,
                KEY_PROMO_CODE, sub_id)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 11

    fun eventClickProductList(product_id: String,
                              category_id: String,
                              productName: String,
                              price: Int,
                              position: Int,
                              categoryNamePath:String,
                              pathList: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "productClick",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click product list",
                KEY_EVENT_LABEL, product_id,
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                "click", DataLayer.mapOf(
                "actionField", DataLayer.mapOf(
                KEY_LIST, pathList)),
                "products", DataLayer.listOf(DataLayer.mapOf(
                KEY_NAME, productName,
                KEY_ID, product_id,
                "price", price,
                "brand", "",
                "category",categoryNamePath,
                "variant", "",
                KEY_LIST, pathList,
                KEY_POSITION, position,
                "attribution", "")))
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 12

    fun eventProductListImpression(category_id: String,
                                   product_name: String,
                                   product_id: String,
                                   price: Int,
                                   position: Int,
                                   pathList: String,
                                   categoryPath: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "productView",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "product list impression",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", DataLayer.listOf(DataLayer.mapOf(
                KEY_NAME, product_name,
                KEY_ID, product_id,
                "price", price,
                "brand", "",
                KEY_CATEGORY, categoryPath,
                "variant", "",
                KEY_LIST, pathList,
                KEY_POSITION, position))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 13

    fun eventWishistClicked(category_id: String, product_id: String, isWishlisted: Boolean) {
        val tracker = getTracker()
        val eventAction: String = if (isWishlisted) {
            "add wishlist"
        } else {
            "remove wishlist"
        }
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, product_id,
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 14

    fun eventSortClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click sort",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 15

    fun eventFilterClicked(category_id: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click filter",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 16

    fun eventQuickFilterClicked(category_id: String, option: Option, filterValue: Boolean) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "quick filter",
                KEY_EVENT_LABEL, "${option.key} - ${option.value}" + " - " + filterValue.toString(),
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //19

    fun eventFilterApplied(category_id: String, filterName: String, filterValue: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "apply filter",
                KEY_EVENT_LABEL, "$filterName-$filterValue",
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    //20

    fun eventFilterCategoryChoosen(category_id: String, filterName: String?, filterValue: String?, isInsideDetail: Boolean, isActive: Boolean) {
        val tracker = getTracker()

        var eventAction = "click-$filterName:$filterValue-"
        eventAction = if (isInsideDetail) {
            eventAction + "inside lihat semua"
        } else {
            eventAction + "outside lihat semua"
        }

        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, isActive.toString(),
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 21

    fun eventSortApplied(category_id: String, sortName: String, sortValue: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "apply sort - $sortName",
                KEY_EVENT_LABEL, sortValue,
                KEY_CATEGORY_ID, category_id
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

}