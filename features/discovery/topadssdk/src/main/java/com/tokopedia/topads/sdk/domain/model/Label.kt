package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_TITLE = "title"
const val KEY_COLOR = "color"

data class Label(
    @SerializedName(KEY_TITLE)
    @Expose
    var title: String = "",

    @SerializedName(KEY_COLOR)
    @Expose
    var color: String? = ""
) : Parcelable {

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_TITLE)) {
            title = jSONObject.getString(KEY_TITLE)
        }
        if (!jSONObject.isNull(KEY_COLOR)) {
            color = jSONObject.getString(KEY_COLOR)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Label> = object : Parcelable.Creator<Label> {
            override fun createFromParcel(parcel: Parcel): Label {
                return Label(parcel)
            }

            override fun newArray(size: Int): Array<Label?> {
                return arrayOfNulls(size)
            }
        }
    }
}
