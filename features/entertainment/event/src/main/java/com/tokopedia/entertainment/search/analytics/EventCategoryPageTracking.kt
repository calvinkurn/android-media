package com.tokopedia.entertainment.search.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.common.util.CommonTrackingEvent
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CREATIVE_NAME
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CREATIVE_SLOT
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CREATIVE_URL
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_CHECKOUT_PROGRESS
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_SELECT_CONTENT
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.INDEX
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEMS
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_BRAND
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_CATEGORY
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_ID
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_LIST
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_NAME
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_VARIANT
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.PRICE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.PROMOTION
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralCheckoutProgress
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralClick
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralClickedBundle
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralImpression
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
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

    private object Misc{
        val SCREENNAME = "screenName"
        val CURRENTSITE = "currentSite"
        val BUSINESSUNIT = "businessUnit"
        val CATEGORY = "category"

        val CURRENTSITEDATA =  "tokopediadigitalevents"
        val BUSINESSUNITDATA = "travel & entertainment"
        val CATEGORYDATA = "events"
    }

    fun onClickGridViewProduct(event: EventGridAdapter.EventGrid,
                               listsEvent: List<EventGridAdapter.EventGrid>,
                               position: Int,
                               userId: String
    ){
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click page product")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", event.nama_event, position.toString()))
            putString(ITEM_LIST, "")
            putParcelableArrayList(ITEMS, getProductFromMetaData(event, position))
        }
        eventDataLayer.addGeneralClickedBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_SELECT_CONTENT, eventDataLayer)
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "productClick",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click page product",
                Event.LABEL, String.format("%s - %s", event.nama_event, position.toString()),
                Misc.CURRENTSITE, Misc.CURRENTSITEDATA,
                Misc.BUSINESSUNIT, Misc.BUSINESSUNITDATA,
                Misc.CATEGORY, Misc.CATEGORYDATA,
                Misc.SCREENNAME, "",
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
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click filter",
                TrackAppUtils.EVENT_LABEL, String.format("%s", category.category)
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun impressionGridViewProduct(event: EventGridAdapter.EventGrid,
                                  listsEvent: List<EventGridAdapter.EventGrid>,
                                  position: Int,
                                  userId: String
    ){
        val itemBundle = Bundle().apply {
            putString(CREATIVE_NAME, event.nama_event)
            putString(CREATIVE_SLOT, "$position")
            putString(CREATIVE_URL, event.image_url)
            putString(ITEM_ID, "${event.id}")
            putString(ITEM_NAME, event.nama_event)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "impression page product")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s - %s", event.nama_event, position.toString()))
            putParcelableArrayList(PROMOTION, arrayListOf(itemBundle))
        }
        eventDataLayer.addGeneralImpression(userId)
        getTracker().sendEnhanceEcommerceEvent(CommonTrackingEvent.Misc.EVENT_VALUE_VIEW_ITEM, eventDataLayer)

    }

    private fun getProductFromMetaData(event: EventGridAdapter.EventGrid, position: Int): ArrayList<Bundle>{
        var list = arrayListOf<Bundle>()
        event.let { it ->
            val itemBundle = Bundle().apply {
                putString(INDEX, "${position}")
                putString(ITEM_BRAND, "")
                putString(ITEM_CATEGORY, "")
                putString(ITEM_ID, it.id)
                putString(ITEM_NAME, it.nama_event)
                putString(ITEM_VARIANT, it.id)
                putString(PRICE, it.harga_now)
            }
            list.add(itemBundle)
        }
        return list
    }

    private fun getTracker() : Analytics {
        return TrackApp.getInstance().gtm
    }

    companion object{
        fun getInstance() = EventCategoryPageTracking()
    }
}