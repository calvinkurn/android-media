package com.tokopedia.entertainment.search.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

class EventCategoryPageTracking {
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

    fun onClickGridViewProduct(event: EventGridAdapter.EventGrid,
                               listsEvent: List<EventGridAdapter.EventGrid>,
                               position: Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click page product",
                Event.LABEL, String.format("%s - %s", event.nama_event, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Click.KEY, DataLayer.mapOf(Click.ACTION_FIELD, DataLayer.mapOf("list", event.nama_event),
                Product.KEY, DataLayer.listOf(
                DataLayer.mapOf(
                        Product.NAME, event.nama_event,
                        Product.ID, event.id,
                        Product.PRICE, event.harga_now,
                        Product.BRAND, "",
                        Product.CATEGORY, "",
                        Product.VARIANT, "",
                        Product.LIST, event.nama_event,
                        Product.POSITION, position
                )
        ))
        )))
    }

    fun onClickCategoryBubble(category: CategoryTextBubbleAdapter.CategoryTextBubble){
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click filter",
                Event.LABEL, String.format("%s", category.category)
        ))
    }

    fun impressionGridViewProduct(event: EventGridAdapter.EventGrid,
                                  listsEvent: List<EventGridAdapter.EventGrid>,
                                  position: Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productView",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "impression page product",
                Event.LABEL, String.format("%s - %s", event.nama_event, position.toString()),
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Impression.KEY, listOf(DataLayer.mapOf(
                Impression.NAME, event.nama_event,
                Impression.ID, event.id,
                Impression.PRICE, event.harga_now,
                Impression.BRAND, "",
                Impression.CATEGORY, "",
                Impression.VARIANT, "",
                Impression.LIST, listsEvent,
                Impression.POSITION, position
        ))
        )))
    }

    private fun getTracker() : Analytics {
        return TrackApp.getInstance().gtm
    }

    companion object{
        fun getInstance() = EventCategoryPageTracking()
    }
}