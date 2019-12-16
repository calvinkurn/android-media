package com.tokopedia.discovery.categoryrevamp.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
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
    val KEY_PROMO_ID = "promo_id"
    val KEY_CURRENCY_CODE = "currencyCode"
    val KEY_IMPRESSIONS = "impressions"
    val KEY_PRICE = "price"
    val KEY_BRAND = "brand"
    val KEY_ATTRIBUTION = "attribution"
    val KEY_VARIANT = "variant"
    val KEY_PROMOTIONS = "promotions"
    val KEY_PROMOCLICK = "promoClick"
    val KEY_PRODUCTS = "products"
    val KEY_ACTIONFIELD = "actionField"
    val KEY_CLICK = "click"
    val KEY_PROMOVIEW = "promoView"
    val KEY_EVENT_BANNED_CLICK = "clickCategoryBanned"
    val KEY_EVENT_BANNED_VIEW = "viewCategoryBannedIris"

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
                KEY_PROMOCLICK, DataLayer.mapOf(
                KEY_PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, sub_category_id,
                KEY_NAME, path,
                KEY_CREATIVE, creative_name,
                KEY_CREATIVE_URL, creative_url,
                KEY_POSITION, position,
                KEY_CATEGORY, namedPath,
                KEY_PROMO_ID, sub_category_id,
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
                                   namePath: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "promoView",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "subcategory impression",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_PROMOVIEW, DataLayer.mapOf(
                KEY_PROMOTIONS, DataLayer.listOf(DataLayer.mapOf(
                KEY_ID, sub_id,
                KEY_NAME, path,
                KEY_CREATIVE, creative_name,
                KEY_CREATIVE_URL, creative_url,
                KEY_POSITION, position,
                KEY_CATEGORY, namePath,
                KEY_PROMO_ID, sub_id,
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
                              categoryNamePath: String,
                              pathList: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, "productClick",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "click product list",
                KEY_EVENT_LABEL, product_id,
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CLICK, DataLayer.mapOf(
                KEY_ACTIONFIELD, DataLayer.mapOf(
                KEY_LIST, pathList),
                KEY_PRODUCTS, DataLayer.listOf(DataLayer.mapOf(
                KEY_NAME, productName,
                KEY_ID, product_id,
                KEY_PRICE, price,
                KEY_BRAND, "",
                KEY_CATEGORY, categoryNamePath,
                KEY_VARIANT, "",
                KEY_LIST, pathList,
                KEY_POSITION, position,
                KEY_ATTRIBUTION, ""))))
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }


    // 12

    fun eventProductListImpression(category_id: String,
                                   departmentId: String,
                                   viewedProductList: List<Visitable<Any>>,
                                   viewedTopAdsList: List<Visitable<Any>>) {

        val tracker = getTracker()
        val list = ArrayList<Map<String, Any>>()
        val itemList = ArrayList<Visitable<Any>>()

        itemList.addAll(viewedProductList)
        itemList.addAll(viewedTopAdsList)

        for (element in itemList) {
            val item = element as ProductsItem
            val map = HashMap<String, Any>()
            map[KEY_NAME] = item.name
            map[KEY_ID] = item.id.toString()
            map[KEY_PRICE] = CurrencyFormatHelper.convertRupiahToInt(item.price)
            map[KEY_BRAND] = ""
            map[KEY_CATEGORY] = item.categoryBreadcrumb ?: ""
            map[KEY_VARIANT] = ""
            map[KEY_LIST] = getProductItemPath(item.categoryBreadcrumb ?: "", departmentId)
            map[KEY_POSITION] = item.adapter_position
            list.add(map)
        }

        val map = DataLayer.mapOf(
                KEY_EVENT, "productView",
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "product list impression",
                KEY_EVENT_LABEL, "",
                KEY_CATEGORY_ID, category_id,
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CURRENCY_CODE, "IDR",
                KEY_IMPRESSIONS, DataLayer.listOf(list)
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    // 13

    fun eventWishistClicked(category_id: String,
                            product_id: String,
                            isWishlisted: Boolean,
                            isLoggedIn: Boolean,
                            isTopAds: Boolean) {
        val tracker = getTracker()
        var eventAction: String = if (isWishlisted) {
            "add wishlist - other - "
        } else {
            "remove wishlist - other - "
        }
        eventAction += getLoginType(isLoggedIn)
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_NAME_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, product_id + " - " + getProductType(isTopAds),
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


    private fun getProductItemPath(path: String, id: String): String {
        if (path.isNotEmpty()) {
            return "category/$path - $id"
        }
        return ""
    }

    fun eventBukaClick(destinationUrl: String, categoryId: String) {
        getTracker().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        KEY_EVENT, KEY_EVENT_BANNED_CLICK,
                        KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                        KEY_EVENT_ACTION, "click buka mobile web button",
                        KEY_EVENT_LABEL, destinationUrl,
                        KEY_CATEGORY_ID, categoryId
                )
        )
    }

    fun eventBukaView(destinationUrl: String, categoryId: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_EVENT_BANNED_VIEW,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, "buka mobile web button impression",
                KEY_EVENT_LABEL, destinationUrl,
                KEY_CATEGORY_ID, categoryId
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    private fun getLoginType(isUserLoggedIn: Boolean): String {
        return if (isUserLoggedIn) {
            "login"
        } else {
            "nonlogin"
        }
    }

    private fun getProductType(isTopAds: Boolean): String {
        return if (isTopAds) {
            "topads"
        } else {
            "general"
        }
    }
}