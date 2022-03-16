package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.google.gson.Gson
import java.io.Serializable

data class ActionButton(
    @SerializedName("body")
    @Expose
    var body: Body = Body(),

    @SerializedName("label")
    @Expose
    var label: String = "",

    @SerializedName("uri")
    @Expose
    var uri:String = "",

    @SerializedName("control")
    @Expose
    var control: String = "",

    @SerializedName("name")
    @Expose
    var name: String = "",

    @SerializedName("buttonType")
    @Expose
    val buttonType: String = "",

    @SerializedName("header")
    @Expose
    val header: String = "",

    @SerializedName("color")
    @Expose
    val actionColor: ActionColor = ActionColor()
) : Serializable {

    val headerObject: Header
        get() {
            val gson = Gson()
            return gson.fromJson(header, Header::class.java)
        }

    companion object {
        const val PRIMARY_BUTTON = "primaryButton"
        const val SECONDARY_BUTTON = "secondaryButton"
    }
}