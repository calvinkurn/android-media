package com.tokopedia.entertainment.home.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Misc.BUSINESSUNIT_VALUE
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Misc.CATEGORY_VALUE
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Misc.CURRENTSITE_VALUE
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Misc.EVENT_VALUE_CLICK
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Misc.EVENT_VALUE_SELECT_CONTENT
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Misc.EVENT_VALUE_VIEW_ITEM
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking.Promo.PROMOTION
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
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
        val USER_ID = "userId"
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
        val CREATIVE_NAME = "creative_name"
        val CREATIVE_URL = "creative_url"
        val CREATIVE_SLOT = "creative_slot"
        val ITEM_ID = "item_id"
        val ITEM_NAME = "item_name"
        val POSITION = "position"
        val CATEGORY = "category"
        val PROMO_ID = "promo_id"
        val PROMO_CODE = "promo_code"
    }

    private object Impression {
        val KEY = "impressions"
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
        val KEY = "products"
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

    private object Misc{
        val SCREENNAME = "screenName"
        val CURRENTSITE = "currentSite"
        val BUSINESSUNIT = "businessUnit"
        val CATEGORY = "category"
        const val BUSINESSUNIT_VALUE = "travel & entertainment"
        const val CURRENTSITE_VALUE = "tokopediadigitalEvent"
        const val CATEGORY_VALUE = "digital - event"
        const val EVENT_VALUE_CLICK = "clickDigitalEvent"
        const val EVENT_VALUE_VIEW_ITEM = "view_item"
        const val EVENT_VALUE_SELECT_CONTENT = "select_content"
    }

    companion object{
        const val ACTIVITY_IND = "Aktivitas"
        const val ACTIVITY_ENG = "activity"
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun MutableMap<String, Any>.addGeneralClick(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = EVENT_VALUE_CLICK
        this[TrackAppUtils.EVENT_CATEGORY] = CATEGORY_VALUE
        this[Misc.CURRENTSITE] = CURRENTSITE_VALUE
        this[Misc.BUSINESSUNIT] = BUSINESSUNIT_VALUE
        return this
    }

    private fun Bundle.addGeneralImpression(userId: String): Bundle {
        this.putString(TrackAppUtils.EVENT, EVENT_VALUE_VIEW_ITEM)
        this.putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_VALUE)
        this.putString(Misc.CURRENTSITE, CURRENTSITE_VALUE)
        this.putString(Misc.BUSINESSUNIT, BUSINESSUNIT_VALUE)
        this.putString(Event.USER_ID, userId)
        return this
    }

    private fun Bundle.addGeneralClickedBundle(userId: String): Bundle {
        this.putString(TrackAppUtils.EVENT, EVENT_VALUE_SELECT_CONTENT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_VALUE)
        this.putString(Misc.CURRENTSITE, CURRENTSITE_VALUE)
        this.putString(Misc.BUSINESSUNIT, BUSINESSUNIT_VALUE)
        this.putString(Event.USER_ID, userId)
        return this
    }

    fun openHomeEvent() {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "impression homepage",
                TrackAppUtils.EVENT_LABEL, ""
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun impressionTopEventProduct(item: EventItemModel,
                                  listItems: List<String>,
                                  position: Int,
                                  userId: String
    ) {
        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.title)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.appUrl)
            putString(Promo.ITEM_ID, "${item.produkId}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "impression top event")
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_VIEW_ITEM, eventDataLayer)
    }

    fun clickSeeAllTopEventProduct() {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click lihat semua on top event",
                Event.LABEL, "",
                Misc.SCREENNAME, "",
                Misc.CURRENTSITE, "tokopediadigitalevent",
                Misc.BUSINESSUNIT, "travel & entertainment",
                Misc.CATEGORY, "events"
        ))
    }

    fun clickTopEventProduct(item: EventItemModel,
                             listItems: List<String>,
                             position: Int,
                             userId: String
    ) {
        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.title)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.appUrl)
            putString(Promo.ITEM_ID, "${item.produkId}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click top event")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", item.title, position.toString()))
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_SELECT_CONTENT, eventDataLayer)

    }

    fun impressionSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>,
                                      title:String, position: Int, userId: String) {
        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.title)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.appUrl)
            putString(Promo.ITEM_ID, "${item.produkId}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, String.format("%s %s", "impression on curated",
                    getActivityinEnglish(title)))
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_VIEW_ITEM, eventDataLayer)
    }

    fun clickSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>,
                                 title:String, position: Int, userId: String) {

        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.title)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.appUrl)
            putString(Promo.ITEM_ID, "${item.produkId}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, String.format("%s %s", "click on curated", getActivityinEnglish(title)))
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", item.title, position.toString()))
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_SELECT_CONTENT, eventDataLayer)
    }


    fun clickSeeAllCuratedEventProduct(title: String, position: Int) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, String.format("%s %s", "click lihat semua curated", getActivityinEnglish(title)),
                TrackAppUtils.EVENT_LABEL, String.format("%s - %d", title, position)
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun impressionLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>,
                                position: Int, userId: String) {
        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.title)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.imageUrl)
            putString(Promo.ITEM_ID, "${item.id}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "impression on section international")
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_VIEW_ITEM, eventDataLayer)
    }

    fun clickLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>,
                           position: Int, userId: String) {
        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.title)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.imageUrl)
            putString(Promo.ITEM_ID, "${item.id}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click on section international")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", item.title, position.toString()))
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_SELECT_CONTENT, eventDataLayer)
    }

    fun impressionBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int,
                         userId: String) {

        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.displayName)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.appUrl)
            putString(Promo.ITEM_ID, "${item.id}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "impression banner")
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_VIEW_ITEM, eventDataLayer)
    }

    fun clickCategoryIcon(item: CategoryEventViewHolder.CategoryItemModel, position: Int) {
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click category icon",
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", item.title, position)
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun clickBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int,
                    userId: String
    ) {
        val itemBundle = Bundle().apply {
            putString(Promo.CREATIVE_NAME, item.displayName)
            putString(Promo.CREATIVE_SLOT, "$position")
            putString(Promo.CREATIVE_URL, item.appUrl)
            putString(Promo.ITEM_ID, "${item.id}")
            putString(Promo.ITEM_NAME, item.title)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click banner")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", item.displayName, position.toString()))
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_SELECT_CONTENT, eventDataLayer)
    }

    private fun getActivityinEnglish(title: String):String{
        return if(title.equals(ACTIVITY_IND)) ACTIVITY_ENG else title
    }
}