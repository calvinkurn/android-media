package com.tokopedia.salam.umrah.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.ACTION_FIELD
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.ADD
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.ADD_TO_CART
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.BULAN_KEBERANGKATAN
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CATEGORY
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CATEGORY_ID
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_BACK
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_FILTER
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_LANJUTKAN_BELI_PAKET
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_PRODUCT
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_SORT
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CLICK_UMROH
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.CURRENCY_CODE
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.DIMENSION110
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.DURASI_PERJALANAN
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.ECOMMERCE
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.EMPTY
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.FILTER
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.HARGA_PAKET
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.ID
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.IDR
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.IMPRESSIONS
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.IMPRESSION_PRODUCT
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.KOTA_KEBERANGKATAN
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.LIST
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.NAME
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.POSITION
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.PRICE
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.PRODUCTS
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.PRODUCT_CLICK
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.PRODUCT_DETAIL_PAGE_UMROH
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.PRODUCT_VIEW
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.QUANTITY
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.SHOP_ID
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.SHOP_NAME
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.SORT
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingConstant.VARIANT
import com.tokopedia.salam.umrah.pdp.data.ParamPurchase
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.util.SearchOrCategory
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*

/**
 * @author by furqan on 08/10/2019
 */
class UmrahTrackingUtil {
    private fun getSearchNCategoryProductMap(product: UmrahSearchProduct, position: Int,sort: String,filter: ParamFilter): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[NAME] = product.title
        map[ID] = product.id
        map[PRICE] = product.originalPrice
        map[CATEGORY] = product.slugName
        map[POSITION] = position
        map[DIMENSION110] = "$SORT: ${sort}_" +
                "$FILTER: " +
                getFilter(filter)
        return map
    }

    private fun getFilter(filter: ParamFilter): Any? {
        return "$BULAN_KEBERANGKATAN - ${filter.departurePeriod}_" +
                "$KOTA_KEBERANGKATAN - ${filter.departureCity}_" +
                "$DURASI_PERJALANAN - ${filter.durationDaysMinimum}-${filter.durationDaysMaximum}_" +
                "$HARGA_PAKET - ${filter.priceMinimum}-${filter.priceMaximum}"
    }

    private fun getUmrahSearchNCategoryProductLists(products: List<UmrahSearchProduct>, sort: String, filter: ParamFilter, searchOrCategory: SearchOrCategory): Any? {
        val list = ArrayList<Map<String, Any>>()
        for ((index,product) in products.withIndex()) {
            val map = getSearchNCategoryProductMap(product, index, sort, filter)
            map[LIST] = searchOrCategory.getEventCategory()
            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun umrahSearchNCategoryProductListImpression(products:List<UmrahSearchProduct>,sort: String, filter: ParamFilter, searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_VIEW
        map[EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[EVENT_ACTION] = IMPRESSION_PRODUCT
        map[EVENT_LABEL] = EMPTY
        map[ECOMMERCE] = DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, getUmrahSearchNCategoryProductLists(products, sort, filter, searchOrCategory)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getSearchNCategoryClickedProduct(product: UmrahSearchProduct, position: Int, sort: String, filter: ParamFilter): Any? {
        val list = ArrayList<Map<String, Any>>()
        val map = getSearchNCategoryProductMap(product, position, sort, filter)
        list.add(map)
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun umrahSearchNCategoryProductClick(product: UmrahSearchProduct, position: Int, sort: String, filter: ParamFilter, searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_CLICK
        map[EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[EVENT_ACTION] = CLICK_PRODUCT
        map[EVENT_LABEL] = EMPTY
        map[ECOMMERCE] = DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                    ACTION_FIELD, DataLayer.mapOf(LIST, searchOrCategory.getEventCategory()),
                    PRODUCTS, getSearchNCategoryClickedProduct(product, position, sort, filter))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahSearchNCategorySortClick(sort: String, searchOrCategory: SearchOrCategory){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CLICK_UMROH
        map[EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[EVENT_ACTION] = CLICK_SORT
        map[EVENT_LABEL] = sort
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    @Suppress("UNUSED_PARAMETER")
    fun umrahSearchNCategoryFilterClick(paramFilter: ParamFilter,searchOrCategory: SearchOrCategory){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CLICK_UMROH
        map[EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[EVENT_ACTION] = CLICK_FILTER
        map[EVENT_LABEL] = getFilter(paramFilter)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahSearchNCategoryBackClick(searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CLICK_UMROH
        map[EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[EVENT_ACTION] = CLICK_BACK
        map[EVENT_LABEL] = EMPTY
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahPdpAllClick(userAction: UmrahPdpTrackingUserAction){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CLICK_UMROH
        map[EVENT_CATEGORY] = PRODUCT_DETAIL_PAGE_UMROH
        map[EVENT_ACTION] = userAction.getEventAction()
        map[EVENT_LABEL] = EMPTY
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getPurchasedProductsNext(paramPurchase: ParamPurchase): Any? {
        val list = ArrayList<Map<String, Any>>()
        val map = HashMap<String, Any>()
        map[NAME] = paramPurchase.name
        map[ID] = paramPurchase.id
        map[PRICE] = paramPurchase.price
        map[CATEGORY] = paramPurchase.slugName
        map[VARIANT] = paramPurchase.variant
        map[QUANTITY] = paramPurchase.totalPassenger
        map[SHOP_ID] = paramPurchase.shopId
        map[SHOP_NAME] = paramPurchase.shopName
        map[CATEGORY_ID] = paramPurchase.categoryId
        list.add(map)
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun umrahPdpPurchaseProductsNext(paramPurchase: ParamPurchase){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = PRODUCT_DETAIL_PAGE_UMROH
        map[EVENT_ACTION] = CLICK_LANJUTKAN_BELI_PAKET
        map[EVENT_LABEL] = EMPTY
        map[ECOMMERCE] = DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                ADD, DataLayer.mapOf(
                    PRODUCTS, getPurchasedProductsNext(paramPurchase))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}