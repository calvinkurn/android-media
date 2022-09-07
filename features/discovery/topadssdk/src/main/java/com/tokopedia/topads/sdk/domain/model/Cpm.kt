package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_TEMPLATE_ID = "template_id"
private const val KEY_NAME = "name"
private const val KEY_CPM_IMAGE = "image"
private const val KEY_BADGES = "badges"
private const val KEY_PROMOTED_TEXT = "promoted_text"
private const val KEY_DESCRIPTION = "description"
private const val KEY_URI = "uri"
private const val KEY_SHOP = "shop"
private const val KEY_CTA_TEXT = "button_text"
private const val KEY_LAYOUT = "layout"
private const val KEY_POSITION = "position"
private const val KEY_WIDGET_TITLE = "widget_title"
private const val KEY_WIDGET_IMAGE_URL = "widget_image_url"

data class Cpm(
    @SerializedName(KEY_TEMPLATE_ID)
    var templateId: Int = 0,

    @SerializedName(KEY_NAME)
    var name: String = "",

    @SerializedName(KEY_CPM_IMAGE)
    var cpmImage: CpmImage = CpmImage(),

    @SerializedName(KEY_BADGES)
    var badges: MutableList<Badge>? = ArrayList(),

    @SerializedName(KEY_PROMOTED_TEXT)
    var promotedText: String = "",

    @SerializedName(KEY_URI)
    var uri: String = "",

    @SerializedName(KEY_DESCRIPTION)
    var decription: String = "",

    @SerializedName(KEY_SHOP)
    var cpmShop: CpmShop = CpmShop(),

    @SerializedName(KEY_CTA_TEXT)
    var cta: String = "",

    @SerializedName(KEY_LAYOUT)
    var layout: Int = 0,

    @SerializedName(KEY_POSITION)
    var position: Int = 0,

    @SerializedName(KEY_WIDGET_TITLE)
    var widgetTitle: String = "",

    @SerializedName(KEY_WIDGET_IMAGE_URL)
    var widgetImageUrl: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readParcelable(CpmImage::class.java.classLoader) ?: CpmImage(),
        parcel.createTypedArrayList(Badge.CREATOR),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(CpmShop::class.java.classLoader) ?: CpmShop(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_TEMPLATE_ID)) {
            templateId = jSONObject.getInt(KEY_TEMPLATE_ID)
        }
        if (!jSONObject.isNull(KEY_NAME)) {
            name = jSONObject.getString(KEY_NAME)
        }
        if (!jSONObject.isNull(KEY_CPM_IMAGE)) {
            cpmImage = CpmImage(jSONObject.getJSONObject(KEY_CPM_IMAGE))
        }
        if (!jSONObject.isNull(KEY_BADGES)) {
            val badgeArray = jSONObject.getJSONArray(KEY_BADGES)
            for (i in 0 until badgeArray.length()) {
                badges!!.add(Badge(badgeArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_PROMOTED_TEXT)) {
            promotedText = jSONObject.getString(KEY_PROMOTED_TEXT)
        }
        if (!jSONObject.isNull(KEY_URI)) {
            uri = jSONObject.getString(KEY_URI)
        }
        if (!jSONObject.isNull(KEY_DESCRIPTION)) {
            decription = jSONObject.getString(KEY_DESCRIPTION)
        }
        if (!jSONObject.isNull(KEY_SHOP)) {
            cpmShop = CpmShop(jSONObject.getJSONObject(KEY_SHOP))
        }
        if (!jSONObject.isNull(KEY_CTA_TEXT)) {
            cta = jSONObject.getString(KEY_CTA_TEXT)
        }
        if (!jSONObject.isNull(KEY_LAYOUT)) {
            layout = jSONObject.getInt(KEY_LAYOUT)
        }
        if (!jSONObject.isNull(KEY_POSITION)) {
            position = jSONObject.getInt(KEY_POSITION)
        }
        if (!jSONObject.isNull(KEY_WIDGET_TITLE)) {
            widgetTitle = jSONObject.getString(KEY_WIDGET_TITLE)
        }
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(templateId)
        parcel.writeString(name)
        parcel.writeParcelable(cpmImage, flags)
        parcel.writeString(promotedText)
        parcel.writeString(uri)
        parcel.writeString(decription)
        parcel.writeParcelable(cpmShop, flags)
        parcel.writeString(cta)
        parcel.writeInt(layout)
        parcel.writeInt(position)
        parcel.writeString(widgetTitle)
        parcel.writeTypedList(badges)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cpm> {
        override fun createFromParcel(parcel: Parcel): Cpm {
            return Cpm(parcel)
        }

        override fun newArray(size: Int): Array<Cpm?> {
            return arrayOfNulls(size)
        }
    }

}