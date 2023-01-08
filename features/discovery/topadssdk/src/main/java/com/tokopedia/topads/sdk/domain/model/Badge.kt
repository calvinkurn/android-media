package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class Badge(
    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("image_url")
    @Expose
    var imageUrl: String = "",

    @SerializedName("show")
    @Expose
    var isShow: Boolean = false
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull("title")) {
            title = jSONObject.getString("title")
        }
        if (!jSONObject.isNull("image_url")) {
            imageUrl = jSONObject.getString("image_url")
        }
        if (!jSONObject.isNull("show")) {
            isShow = jSONObject.getBoolean("show")
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (isShow) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Badge> {
        override fun createFromParcel(parcel: Parcel): Badge {
            return Badge(parcel)
        }

        override fun newArray(size: Int): Array<Badge?> {
            return arrayOfNulls(size)
        }
    }

}
