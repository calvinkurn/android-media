package com.tokopedia.entertainment.search.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.track.TrackApp
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

    fun impressionCitySearchSuggestion(listsCity: SearchLocationListViewHolder.LocationSuggestion, position: Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "promoView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "impression city result",
                Event.LABEL, "",
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA,
                Misc.SCREENNAME, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_IMPRESSION, DataLayer.mapOf(
                Promo.PROMOTION, getPromoLocationSuggestion(listsCity, position)))))
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
                                  position: Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "promoClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click city result",
                Event.LABEL, String.format("%s - %s", location.city, position.toString()),
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA,
                Misc.SCREENNAME, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Promo.KEY_CLICK, DataLayer.mapOf(
                Promo.PROMOTION, getPromoLocationSuggestion(listsLocation, position)))))
    }

    fun impressionEventSearchSuggestion(listsEvent: SearchEventListViewHolder.KegiatanSuggestion, position: Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "impression product result",
                Event.LABEL, "",
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA,
                Misc.SCREENNAME, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Impression.KEY, getImpressionEventSearchList(listsEvent, position))))
    }

    private fun getImpressionEventSearchList(listsEvent: SearchEventListViewHolder.KegiatanSuggestion, position: Int) : Any?{
        val list = mutableListOf<Any>()
            list.add(DataLayer.mapOf(
                    Impression.NAME, listsEvent.nama_kegiatan,
                    Impression.ID, listsEvent.id,
                    Impression.PRICE, listsEvent.sales_price,
                    Impression.BRAND, "",
                    Impression.CATEGORY, "",
                    Impression.VARIANT, "",
                    Impression.LIST, listsEvent.nama_kegiatan,
                    Impression.POSITION, position + 1
            ))
        return list
    }

    fun onClickedEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion,
                                       listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>,
                                       position : Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click product result",
                Event.LABEL, String.format("%s - %s", event.nama_kegiatan, position.toString()),
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA,
                Misc.SCREENNAME, "",
                Ecommerce.KEY, DataLayer.mapOf(
                Click.KEY, DataLayer.mapOf(
                Click.ACTION_FIELD, DataLayer.mapOf("list", event.nama_kegiatan),
                Product.KEY, DataLayer.listOf(
                DataLayer.mapOf(
                        Product.NAME, event.nama_kegiatan,
                        Product.ID, event.id,
                        Product.PRICE, event.price,
                        Product.BRAND, "",
                        Product.CATEGORY, "",
                        Product.VARIANT, "",
                        Product.LIST, event.nama_kegiatan,
                        Product.POSITION, position
                )
        )))))
    }

    fun clickSearchBarOnSearchActivity() : Boolean{
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY,"clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click search box",
                Event.LABEL, "",
                Misc.SCREENNAME, "digital/event/search",
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA
        ))
        return true
    }


    fun clickSearchBarOnKeyWordSearchActivity(keyword: String) : Boolean{
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY,"clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click search",
                Event.LABEL, keyword,
                Misc.SCREENNAME, "",
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA
        ))
        return true
    }

    private fun getTracker() : Analytics{
        return TrackApp.getInstance().gtm
    }

    companion object{
        fun getInstance() = EventSearchPageTracking()
    }
}