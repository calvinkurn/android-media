package com.tokopedia.entertainment.home.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

/**
 * Author errysuprayogi on 17,February,2020
 */
class EventHomePageTracking {

    private object Event {
        val KEY = "event"
        val CATEGORY = "eventCategory"
        val ACTION = "eventAction"
        val LABEL = "eventLabel"
    }

    private object Ecommerce {
        val KEY = "ecommerce"
        val CURRENCY_CODE = "currencyCode"
    }

    private object Promo {
        val KEY_IMPRESSION = "promoView"
        val KEY_CLICK = "promoClick"
        val PROMOTION = "promotions"
        val ID = "id"
        val NAME = "name"
        val CREATIVE = "creative"
        val CREATIVE_URL = "creative_url"
        val POSITION = "position"
        val CATEGORY = "category"
        val PROMO_ID = "promo_id"
        val PROMO_CODE = "promo_code"
    }

    private object Impression {
        val KEY = "impression"
        val NAME = "name"
        val ID = "id"
        val PRICE = "price"
        val BRAND = "brand"
        val CATEGORY = "category"
        val VARIANT = "variant"
        val LIST = "list"
        val POSITION = "position"
    }

    private object Click {
        val KEY = "click"
        val ACTION_FIELD = "actionField"
    }

    private object Product {
        val KEY = "product"
        val NAME = "name"
        val ID = "id"
        val PRICE = "price"
        val BRAND = "brand"
        val CATEGORY = "category"
        val VARIANT = "variant"
        val LIST = "list"
        val POSITION = "position"
        val ATTRIBUTION = "attribution"
    }

    companion object {
        fun getInstance(): EventHomePageTracking {
            return EventHomePageTracking()
        }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun openHomeEvent() {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, "",
                Event.CATEGORY, "",
                Event.ACTION, "",
                Event.LABEL, ""
        ))
    }

    fun impressionTopEventProduct(item: EventItemModel,
                                  listItems: List<String>,
                                  position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "impression top event",
                Event.LABEL, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Impression.KEY, DataLayer.listOf(DataLayer.mapOf(
                Impression.NAME, item.title,
                Impression.ID, item.produkId,
                Impression.PRICE, item.price,
                Impression.BRAND, "none",
                Impression.CATEGORY, "hiburan",
                Impression.VARIANT, "none",
                Impression.LIST, listItems,
                Impression.POSITION, position)))
        ))
    }

    fun clickSeeAllTopEventProduct() {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click lihat semua on top event",
                Event.LABEL, ""
        ))
    }

    fun clickTopEventProduct(item: EventItemModel,
                             listItems: List<String>,
                             position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click top event",
                Event.LABEL, String.format("%s - %s", item.title, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Click.KEY, DataLayer.listOf(DataLayer.mapOf(
                Click.ACTION_FIELD, DataLayer.mapOf("list", item.title),
                Product.KEY, DataLayer.listOf(DataLayer.mapOf(
                Product.NAME, item.title,
                Product.ID, item.produkId,
                Product.PRICE, item.price,
                Product.BRAND, "none",
                Product.CATEGORY, "hiburan",
                Product.VARIANT, "none",
                Product.LIST, listItems,
                Product.POSITION, position)))))
        ))
    }

    fun impressionSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, String.format("%s %s", "impression on curated ", item.title),
                Event.LABEL, String.format("%s - %s", item.title, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Impression.KEY, getProductList(listItems)
        )))
    }

    fun clickSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, String.format("%s %s", "click on curated ", item.title),
                Event.LABEL, String.format("%s - %s", item.title, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Click.KEY, DataLayer.mapOf(Click.ACTION_FIELD, DataLayer.mapOf("list", item.title),
                Product.KEY, getProductList(listItems))
        )))
    }


    fun clickSeeAllCuratedEventProduct(title: String, position: Int) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, String.format("%s %s", "click lihat semua curated", title),
                Event.LABEL, String.format("%s - %d", title, position)
        ))
    }

    private fun getLocationList(items: List<EventItemLocationModel>): Any? {
        var list = mutableListOf<Any>()
        items.forEachIndexed { index, it ->
            list.add(DataLayer.mapOf(
                    Promo.ID, it.id,
                    Promo.NAME, it.title,
                    Promo.CREATIVE, it.title,
                    Promo.CREATIVE_URL, it.imageUrl,
                    Promo.POSITION, index + 1,
                    Promo.PROMO_ID, "none",
                    Promo.PROMO_CODE, ""
            ))
        }
        return list
    }

    private fun getProductList(items: List<EventItemModel>): Any? {
        var list = mutableListOf<Any>()
        items.forEachIndexed { index, it ->
            list.add(DataLayer.mapOf(
                    Product.NAME, it.title,
                    Product.ID, it.produkId,
                    Product.PRICE, it.price,
                    Product.BRAND, "none",
                    Product.CATEGORY, "none",
                    Product.VARIANT, "none",
                    Product.LIST, it.title,
                    Product.POSITION, index + 1
            ))
        }
        return list
    }

    private fun getPromotionList(items: List<EventItemModel>): Any? {
        var list = mutableListOf<Any>()
        items.forEachIndexed { index, it ->
            list.add(DataLayer.mapOf(
                    Promo.ID, it.produkId,
                    Promo.NAME, it.title,
                    Promo.CREATIVE, it.title,
                    Promo.CREATIVE_URL, it.imageUrl,
                    Promo.POSITION, index + 1,
                    Promo.PROMO_ID, "none",
                    Promo.PROMO_CODE, ""
            ))
        }
        return list
    }

    fun impressionLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "promoView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click on section international",
                Event.LABEL, String.format("%s - %s", item.title, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_CLICK, DataLayer.mapOf(
                Promo.PROMOTION, DataLayer.listOf(getLocationList(listItems))
        ))))
    }

    fun clickLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "promoClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click on section international",
                Event.LABEL, String.format("%s - %s", item.title, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_CLICK, DataLayer.mapOf(
                Promo.PROMOTION, DataLayer.listOf(getLocationList(listItems))
        ))))
    }

    fun impressionBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "promoView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "impression banner",
                Event.LABEL, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_IMPRESSION, DataLayer.mapOf(
                Promo.PROMOTION, DataLayer.listOf(DataLayer.mapOf(
                Promo.ID, item.id,
                Promo.NAME, item.displayName),
                Promo.CREATIVE, item.displayName,
                Promo.CREATIVE_URL, item.url,
                Promo.POSITION, position,
                Promo.CATEGORY, "hiburan",
                Promo.PROMO_ID, item.id,
                Promo.PROMO_CODE, "none"
        )))))
    }

    fun clickCategoryIcon(item: CategoryEventViewHolder.CategoryItemModel, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click category icon",
                Event.LABEL, String.format("%s - %s", item.title, position)
        ))
    }

    fun clickBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "promoClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click banner",
                Event.LABEL, String.format("%s - %s", item.displayName, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_CLICK, DataLayer.mapOf(
                Promo.PROMOTION, DataLayer.listOf(DataLayer.mapOf(
                Promo.ID, item.id,
                Promo.NAME, item.displayName,
                Promo.CREATIVE, item.displayName,
                Promo.CREATIVE_URL, item.url,
                Promo.POSITION, position,
                Promo.CATEGORY, "hiburan",
                Promo.PROMO_ID, item.id,
                Promo.PROMO_CODE, "none"
        ))))))
    }
}