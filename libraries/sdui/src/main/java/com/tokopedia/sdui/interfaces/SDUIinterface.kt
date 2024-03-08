package com.tokopedia.sdui.interfaces

import android.content.Context
import android.view.View
import com.tokopedia.sdui.extention.CustomActionInterface
import org.json.JSONObject
import org.json.JSONArray

interface SDUIinterface {
    fun initSDUI(context: Context,
                 sduiTrackingInterface: SDUITrackingInterface? = null,
                 customActionInterface: CustomActionInterface? = null)
    fun createView(
        context: Context, templateJson: JSONObject,
        cardsJsonObject: JSONObject, viewType: String = "divKit"
    ) : View?
    fun createView(
        context: Context, templateJson: JSONObject,
        cardsJsonArray: JSONArray, viewType: String = "divKit"
    ) : View?

}
