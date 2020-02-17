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

    fun impressionTopEventProduct(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "impression top event",
                Event.LABEL, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Impression.KEY, DataLayer.listOf(DataLayer.mapOf(
                Impression.NAME, item.displayName,
                Impression.ID, item.id,
                Impression.PRICE, item.price,
                Impression.BRAND, "none",
                Impression.CATEGORY, "hiburan",
                Impression.VARIANT, "none",
                Impression.LIST, String.format("%s - %s - %s", "hiburan", position.toString(), item.displayName),
                Impression.POSITION, position)))
        ))
    }

    fun clickSeeAllTopEventProduct() {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click lihat semua on top event",
                Event.LABEL, ""
        ))
    }

    fun clickTopEventProduct(item: EventItemModel, position: Int) {
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
                Product.LIST, item.title,
                Product.POSITION, position)))))
        ))
    }

    fun clickRecomendationEventProduct(item: EventItemModel, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click on event recomendation",
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
                Product.LIST, item.title,
                Product.POSITION, position)))))
        ))
    }

    fun clickLocationEventProduct(item: EventItemLocationModel, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click on section " + item.locationType,
                Event.LABEL, String.format("%s - %s", item.title, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_CLICK, DataLayer.mapOf(
                Promo.PROMOTION, DataLayer.listOf(DataLayer.mapOf(
                Promo.ID, item.id,
                Promo.NAME, item.title,
                Promo.CREATIVE, item.title,
                Promo.CREATIVE_URL, item.imageUrl,
                Promo.POSITION, position,
                Promo.PROMO_ID, "none",
                Promo.PROMO_CODE, "none"
        ))))))
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
                Event.LABEL, item.title, position
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