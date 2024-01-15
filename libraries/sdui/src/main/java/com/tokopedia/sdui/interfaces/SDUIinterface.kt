package com.tokopedia.sdui.interfaces

import android.content.Context
import android.view.View
import org.json.JSONObject
import org.json.JSONArray

interface SDUIinterface {
    fun initSDUI(context: Context)
    fun createView(context: Context, templateJson: JSONObject,
                   viewType:String = "divKit", cardsJsonObject: JSONObject) : View?
    fun createView(
        context: Context, templateJson: JSONObject,
        viewType: String = "divKit", cardsJsonArray: JSONArray) : View?

}
