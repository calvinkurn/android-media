package com.tokopedia.entertainment.common.util

import android.os.Bundle
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.BUSINESSUNIT
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.BUSINESSUNIT_VALUE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CATEGORY_VALUE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CURRENTSITE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CURRENTSITE_VALUE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_ATC
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_CLICK
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_SELECT_CONTENT
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_VIEW_ITEM
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.USER_ID
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.track.TrackAppUtils

object CommonTrackingEvent{

     object Misc {
        const val BUSINESSUNIT = "businessUnit"
        const val CURRENTSITE = "currentSite"
        const val BUSINESSUNIT_VALUE = "travel & entertainment"
        const val CURRENTSITE_VALUE = "tokopediadigitalEvent"
        const val CATEGORY_VALUE = "digital - event"
        const val EVENT_VALUE_CLICK = "clickDigitalEvent"
        const val EVENT_VALUE_VIEW_ITEM = "view_item"
        const val EVENT_VALUE_SELECT_CONTENT = "select_content"
        const val EVENT_VALUE_ATC = "add_to_cart"
        const val USER_ID = "userId"

         const val ITEMS = "items"
         const val ITEM_ID = "item_id"
         const val ITEM_NAME = "item_name"
         const val ITEM_BRAND = "item_brand"
         const val ITEM_CATEGORY = "item_category"
         const val ITEM_VARIANT = "item_variant"
         const val PRICE = "price"
         const val QUANTITY = "quantity"
         const val SHOP_ID = "shop_id"
         const val SHOP_NAME = "shop_name"
         const val SHOP_TYPE = "shop_type"

         const val CATEGORY_ID = "category_id"

    }

    fun MutableMap<String, Any>.addGeneralClick(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = EVENT_VALUE_CLICK
        this[TrackAppUtils.EVENT_CATEGORY] = CATEGORY_VALUE
        this[CURRENTSITE] = CURRENTSITE_VALUE
        this[BUSINESSUNIT] = BUSINESSUNIT_VALUE
        return this
    }

    fun Bundle.addGeneralImpression(userId: String): Bundle {
        this.putString(TrackAppUtils.EVENT, EVENT_VALUE_VIEW_ITEM)
        this.putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_VALUE)
        this.putString(CURRENTSITE, CURRENTSITE_VALUE)
        this.putString(BUSINESSUNIT, BUSINESSUNIT_VALUE)
        this.putString(USER_ID, userId)
        return this
    }

    fun Bundle.addGeneralClickedBundle(userId: String): Bundle {
        this.putString(TrackAppUtils.EVENT, EVENT_VALUE_SELECT_CONTENT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_VALUE)
        this.putString(CURRENTSITE, CURRENTSITE_VALUE)
        this.putString(BUSINESSUNIT, BUSINESSUNIT_VALUE)
        this.putString(USER_ID, userId)
        return this
    }

    fun Bundle.addGeneralATCBundle(userId: String): Bundle {
        this.putString(TrackAppUtils.EVENT, EVENT_VALUE_ATC)
        this.putString(TrackAppUtils.EVENT_CATEGORY, CATEGORY_VALUE)
        this.putString(CURRENTSITE, CURRENTSITE_VALUE)
        this.putString(BUSINESSUNIT, BUSINESSUNIT_VALUE)
        this.putString(USER_ID, userId)
        return this
    }
}