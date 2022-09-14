package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import org.json.JSONObject

data class CpmImage(
    @SerializedName("full_url")
    var fullUrl: String = "",

    @SerializedName("full_ecs")
    var fullEcs: String = "",

    @SerializedName("illustration_url")
    var ilustrationUrl: String = ""
) : ImpressHolder(), Parcelable {

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull("full_url")) {
            fullUrl = jSONObject.getString("full_url")
        }
        if (!jSONObject.isNull("full_ecs")) {
            fullEcs = jSONObject.getString("full_ecs")
        }
        if (!jSONObject.isNull("illustration_url")) {
            ilustrationUrl = jSONObject.getString("illustration_url")
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(fullUrl)
        dest.writeString(fullEcs)
        dest.writeString(ilustrationUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CpmImage> {
        override fun createFromParcel(parcel: Parcel): CpmImage {
            return CpmImage(parcel)
        }

        override fun newArray(size: Int): Array<CpmImage?> {
            return arrayOfNulls(size)
        }
    }
}
