package com.tokopedia.track.builder

import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.SHOP_ID
import com.tokopedia.track.constant.TrackerConstant.USERID

class Tracker private constructor(private val builder : Builder){

    class Builder {
        val params = mutableMapOf<String, Any?>()

        fun setEvent(event: String) = apply {
            params[EVENT] = event
        }

        fun setEventAction(eventAction: String) = apply {
            params[EVENT_ACTION] = eventAction
        }

        fun setEventCategory(eventCategory: String) = apply {
            params[EVENT_CATEGORY] = eventCategory
        }

        fun setEventLabel(eventLabel: String) = apply {
            params[EVENT_LABEL] = eventLabel
        }

        fun setBusinessUnit(businessUnit: String) = apply {
            params[BUSINESS_UNIT] = businessUnit
        }

        fun setCurrentSite(currentSite: String) = apply {
            params[CURRENT_SITE] = currentSite
        }

        fun setShopId(value: String) = apply {
            params[SHOP_ID] = value
        }

        fun setUserId(value: String) = apply {
            params[USERID] = value
        }

        fun setCustomProperty(key: String, value: Any) = apply {
            params[key] = value
        }

        fun build() = Tracker(this)
    }

    fun send() {
        TrackApp.getInstance().gtm.sendGeneralEvent(builder.params)
    }


}