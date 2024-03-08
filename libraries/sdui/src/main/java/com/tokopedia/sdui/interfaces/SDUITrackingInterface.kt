package com.tokopedia.sdui.interfaces

import org.json.JSONObject

interface SDUITrackingInterface {
    fun onViewClick (trackerPayload: JSONObject?)
    fun onViewVisible (trackerPayload: JSONObject?)
}
