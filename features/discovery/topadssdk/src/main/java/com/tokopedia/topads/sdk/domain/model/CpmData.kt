package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class CpmData(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("ad_ref_key")
    var adRefKey: String = "",

    @SerializedName("redirect")
    var redirect: String = "",

    @SerializedName("ad_click_url")
    var adClickUrl: String = "",

    @SerializedName("headline")
    var cpm: Cpm? = null,

    @SerializedName("applinks")
    var applinks: String = ""
) : Parcelable {

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull("id")) {
            id = jSONObject.getString("id")
        }
        if (!jSONObject.isNull("ad_ref_key")) {
            adRefKey = jSONObject.getString("ad_ref_key")
        }
        if (!jSONObject.isNull("redirect")) {
            redirect = jSONObject.getString("redirect")
        }
        if (!jSONObject.isNull("ad_click_url")) {
            adClickUrl = jSONObject.getString("ad_click_url")
        }
        if (!jSONObject.isNull("headline")) {
            cpm = Cpm(jSONObject.getJSONObject("headline"))
        }
        if (!jSONObject.isNull("applinks")) {
            applinks = jSONObject.getString("applinks")
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Cpm::class.java.classLoader),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(adRefKey)
        parcel.writeString(redirect)
        parcel.writeString(adClickUrl)
        parcel.writeParcelable(cpm, flags)
        parcel.writeString(applinks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CpmData> {
        override fun createFromParcel(parcel: Parcel): CpmData {
            return CpmData(parcel)
        }

        override fun newArray(size: Int): Array<CpmData?> {
            return arrayOfNulls(size)
        }
    }
}
