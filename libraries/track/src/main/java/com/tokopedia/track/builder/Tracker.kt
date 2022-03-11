package com.tokopedia.track.builder

import com.tokopedia.track.TrackApp

class Tracker private constructor(private val builder : Builder){

    class Builder {
        val params = mutableMapOf<String, Any?>()

        fun setEvent(event: String) = apply {
            params["event"] = event
        }

        fun setEventAction(eventAction: String) = apply {
            params["eventAction"] = eventAction
        }

        fun setEventCategory(eventCategory: String) = apply {
            params["eventCategory"] = eventCategory
        }

        fun setEventLabel(eventLabel: String) = apply {
            params["eventLabel"] = eventLabel
        }

        fun setBusinessUnit(businessUnit: String) = apply {
            params["businessUnit"] = businessUnit
        }

        fun setCurrentSite(currentSite: String) = apply {
            params["currentSite"] = currentSite
        }

        fun setShopId(value: String) = apply {
            params["shopId"] = value
        }

        fun setUserId(value: String) = apply {
            params["userId"] = value
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