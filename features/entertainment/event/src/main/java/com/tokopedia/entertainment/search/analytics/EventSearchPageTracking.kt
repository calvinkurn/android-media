package com.tokopedia.entertainment.search.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.common.util.CommonTrackingEvent
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralClick
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralClickedBundle
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralImpression
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

class EventSearchPageTracking {

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

    private object Misc{
        val SCREENNAME = "screenName"
        val CURRENTSITE = "currentSite"
        val BUSINESSUNIT = "businessUnit"
        val CATEGORY = "category"

        val CURRENTSITEDATA =  "tokopediadigitalevents"
        val BUSINESSUNITDATA = "travel & entertainment"
        val CATEGORYDATA = "events"
    }

    fun impressionCitySearchSuggestion(listsCity: SearchLocationListViewHolder.LocationSuggestion,
                                       position: Int,
                                       userId: String
    ){
        val itemBundle = Bundle().apply {
            putString(CommonTrackingEvent.Misc.CREATIVE_NAME, listsCity.city)
            putString(CommonTrackingEvent.Misc.CREATIVE_SLOT, "$position")
            putString(CommonTrackingEvent.Misc.CREATIVE_URL, listsCity.imageUrl)
            putString(CommonTrackingEvent.Misc.ITEM_ID, "${listsCity.id_city}")
            putString(CommonTrackingEvent.Misc.ITEM_NAME, listsCity.city)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "impression city result")
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(CommonTrackingEvent.Misc.PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(CommonTrackingEvent.Misc.EVENT_VALUE_VIEW_ITEM, eventDataLayer)
    }

    private fun getPromoLocationSuggestion(listsCity: SearchLocationListViewHolder.LocationSuggestion, position: Int) : Any{
        val list = mutableListOf<Any>()
            list.add(DataLayer.mapOf(
                    Promo.ID, listsCity.id_city,
                    Promo.NAME, listsCity.city,
                    Promo.CREATIVE, listsCity.city,
                    Promo.CREATIVE_URL, listsCity.imageUrl,
                    Promo.POSITION, position + 1,
                    Promo.PROMO_ID, "",
                    Promo.PROMO_CODE, ""
            ))
        return list
    }

    fun onClickLocationSuggestion(location: SearchLocationListViewHolder.LocationSuggestion,
                                  listsLocation: SearchLocationListViewHolder.LocationSuggestion,
                                  position: Int,
                                  userId: String
    ){
        val itemBundle = Bundle().apply {
            putString(CommonTrackingEvent.Misc.CREATIVE_NAME, location.city)
            putString(CommonTrackingEvent.Misc.CREATIVE_SLOT, "$position")
            putString(CommonTrackingEvent.Misc.CREATIVE_URL, location.imageUrl)
            putString(CommonTrackingEvent.Misc.ITEM_ID, "${location.id_city}")
            putString(CommonTrackingEvent.Misc.ITEM_NAME, location.city)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click product result")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", location.city, position.toString()))
            putParcelableArrayList(CommonTrackingEvent.Misc.PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(CommonTrackingEvent.Misc.EVENT_VALUE_SELECT_CONTENT, eventDataLayer)
    }

    fun impressionEventSearchSuggestion(listsEvent: SearchEventListViewHolder.KegiatanSuggestion,
                                        position: Int,
                                        userId: String
    ){
        val itemBundle = Bundle().apply {
            putString(CommonTrackingEvent.Misc.CREATIVE_NAME, listsEvent.nama_kegiatan)
            putString(CommonTrackingEvent.Misc.CREATIVE_SLOT, "$position")
            putString(CommonTrackingEvent.Misc.CREATIVE_URL, listsEvent.image_url)
            putString(CommonTrackingEvent.Misc.ITEM_ID, "${listsEvent.id}")
            putString(CommonTrackingEvent.Misc.ITEM_NAME, listsEvent.nama_kegiatan)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "impression product result")
            putString(TrackAppUtils.EVENT_LABEL, "")
            putParcelableArrayList(CommonTrackingEvent.Misc.PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(CommonTrackingEvent.Misc.EVENT_VALUE_VIEW_ITEM, eventDataLayer)
    }

    fun onClickedEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion,
                                       listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>,
                                       position : Int,
                                       userId: String
    ){
        val itemBundle = Bundle().apply {
            putString(CommonTrackingEvent.Misc.CREATIVE_NAME, event.nama_kegiatan)
            putString(CommonTrackingEvent.Misc.CREATIVE_SLOT, "$position")
            putString(CommonTrackingEvent.Misc.CREATIVE_URL, event.image_url)
            putString(CommonTrackingEvent.Misc.ITEM_ID, "${event.id}")
            putString(CommonTrackingEvent.Misc.ITEM_NAME, event.nama_kegiatan)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click product result")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", event.nama_kegiatan, position.toString()))
            putParcelableArrayList(CommonTrackingEvent.Misc.PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(CommonTrackingEvent.Misc.EVENT_VALUE_SELECT_CONTENT, eventDataLayer)
    }

    fun clickSearchBarOnSearchActivity(){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click search box",
                TrackAppUtils.EVENT_LABEL, ""
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }


    fun clickSearchBarOnKeyWordSearchActivity(keyword: String){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click search",
                TrackAppUtils.EVENT_LABEL, keyword
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    private fun getTracker() : Analytics{
        return TrackApp.getInstance().gtm
    }

    companion object{
        fun getInstance() = EventSearchPageTracking()
    }
}