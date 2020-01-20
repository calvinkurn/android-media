package com.tokopedia.discovery.catalogrevamp.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.filter.common.data.Option
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CatalogDetailPageAnalytics {

    companion object {
        private const val EVENT_CATALOG = "clickCatalog"
        private const val CATALOG_PAGE = "catalog page"
        private const val CLICK_FILTER = "click filter"
        private const val CLICK_SORT = "click sort"
        private const val ADD_WISHLIST = "add wishlist"
        private const val REMOVE_WISHLIST = "remove wishlist"
        private const val CLICK_QUICK_FILTER = "click quick filter"
        private const val APPLY_FILTER = "apply filter"
        private const val CLICK_SPECIFICATIONS = "click spesifikasi"
        private const val CLICK_SOCIAL_SHARE = "click social share"
        private const val CLICK_INDEX_PICTURE = "click index picture"
        private const val SWIPE_INDEX_PICTURE = "swipe index picture"
        private const val CLICK_CATALOG_PICTURE = "click catalog picture"
        private const val SWIPE_CATALOG_PICTURE = "swipe catalog picture"
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val PRODUCT_VIEW = "productView"
        private const val PRODUCT_CLICK = "productClick"
        private const val KEY_ECOMMERCE = "ecommerce"

        //1
        @JvmStatic
        fun trackEventClickFilter() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_FILTER,
                    ""
            ))
        }

        //2
        @JvmStatic
        fun trackEventClickSort() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_SORT,
                    ""
            ))
        }

        //3
        @JvmStatic
        fun eventProductListImpression(
                pathList: String,
                elements: List<Visitable<Any>>,
                isTopAds: Boolean) {
            val tracker = TrackApp.getInstance().gtm


            val list = ArrayList<Map<String, Any>>()
            for (element in elements) {
                val item = element as ProductsItem
                val map = HashMap<String, Any>()
                map["name"] = item.name
                map["id"] = item.id.toString()
                map["price"] = CurrencyFormatHelper.convertRupiahToInt(item.price)
                map["brand"] = ""
                map["category"] = item.categoryBreadcrumb ?: ""
                map["variant"] = ""
                map["list"] = pathList
                map["position"] = item.adapter_position
                list.add(map)
            }


            val map = DataLayer.mapOf(
                    KEY_EVENT, PRODUCT_VIEW,
                    KEY_EVENT_CATEGORY, CATALOG_PAGE,
                    KEY_EVENT_ACTION, if (isTopAds) "impression product - topads" else "product list impression",
                    KEY_EVENT_LABEL, if (isTopAds) "" else "catalog product list",
                    KEY_ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", DataLayer.listOf(list)
            ))
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //3
        @JvmStatic
        fun eventProductListClick(product_name: String,
                                  product_id: String,
                                  price: Int,
                                  position: Int,
                                  pathList: String,
                                  categoryPath: String,
                                  isTopAds: Boolean) {
            val tracker = TrackApp.getInstance().gtm
            val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                    "click", DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", pathList),
                    "products", DataLayer.listOf(DataLayer.mapOf(
                    "name", product_name,
                    "id", product_id,
                    "price", price,
                    "brand", "",
                    "category", categoryPath,
                    "variant", "",
                    "list", pathList,
                    "position", position,
                    "attribution", ""))))
            val map = DataLayer.mapOf(
                    KEY_EVENT, PRODUCT_CLICK,
                    KEY_EVENT_CATEGORY, CATALOG_PAGE,
                    KEY_EVENT_ACTION, if (isTopAds) "click product - topads" else "click product list",
                    KEY_EVENT_LABEL, if (isTopAds) "" else "catalog product list",
                    KEY_ECOMMERCE, ecommerce)
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //4 & 5
        @JvmStatic
        fun trackEventWishlist(isAdded: Boolean, productId: String) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    when {
                        isAdded -> ADD_WISHLIST
                        else -> REMOVE_WISHLIST
                    },
                    productId
            ))
        }

        //6
        @JvmStatic
        fun trackEventClickSpecification() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_SPECIFICATIONS,
                    ""
            ))
        }

        //7
        @JvmStatic
        fun trackEventClickSocialShare() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_SOCIAL_SHARE,
                    ""
            ))
        }

        //8
        @JvmStatic
        fun trackEventClickIndexPicture(position: Int) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_INDEX_PICTURE,
                    position.toString()
            ))
        }

        //9
        @JvmStatic
        fun trackEventSwipeIndexPicture(direction: String) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    SWIPE_INDEX_PICTURE,
                    direction
            ))
        }

        //11
        @JvmStatic
        fun trackEvenClickQuickFilter(option: Option, filterValue: Boolean) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_QUICK_FILTER,
                    "Penawaran-${option.name}" + "-" + filterValue.toString()
            ))
        }

        //12
        @JvmStatic
        fun trackEventClickCatalogPicture() {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    CLICK_CATALOG_PICTURE,
                    ""
            ))
        }

        //13
        @JvmStatic
        fun trackEventSwipeCatalogPicture(direction: String) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    SWIPE_CATALOG_PICTURE,
                    direction
            ))
        }

        //14
        @JvmStatic
        fun trackEvenFilterApplied(title: String, filterName: String, filterValue: String) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    APPLY_FILTER,
                    "$title-$filterName-$filterValue"
            ))
        }

        //15
        @JvmStatic
        fun trackEvenSortApplied(sortName: String, sortValue: Int) {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CATALOG,
                    CATALOG_PAGE,
                    "apply sort - $sortName",
                    sortValue.toString()
            ))
        }
    }
}
