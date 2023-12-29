package com.tokopedia.buyerorder.detail.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ActionButton(
    @SerializedName("body")
    @Expose
    var body: Body = Body(),

    @SerializedName("label")
    @Expose
    val label: String = "",

    @SerializedName("uri")
    @Expose
    val uri:String = "",

    @SerializedName("control")
    @Expose
    var control: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("buttonType")
    @Expose
    val buttonType: String = "",

    @SerializedName("header")
    @Expose
    val header: String = "",

    @SerializedName("color")
    @Expose
    val actionColor: ActionColor = ActionColor()
) : Serializable, Parcelable {

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
