package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_POSITION = "position"
private const val KEY_TYPE = "type"
private const val KEY_TITLE = "title"
private const val KEY_URL = "url"

data class LabelGroup(
    @SerializedName(KEY_POSITION)
    @Expose
    var position: String = "",

    @SerializedName(KEY_TYPE)
    @Expose
    var type: String = "",

    @SerializedName(KEY_TITLE)
    @Expose
    var title: String = "",

    @SerializedName(KEY_URL)
    @Expose
    var imageUrl: String = ""
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_POSITION)) {
            position = jSONObject.getString(KEY_POSITION)
        }
        if (!jSONObject.isNull(KEY_TYPE)) {
            type = jSONObject.getString(KEY_TYPE)
        }
        if (!jSONObject.isNull(KEY_TITLE)) {
            title = jSONObject.getString(KEY_TITLE)
        }
        if (!jSONObject.isNull(KEY_URL)) {
            imageUrl = jSONObject.getString(KEY_URL)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(position)
        dest.writeString(type)
        dest.writeString(title)
        dest.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<LabelGroup> = object : Parcelable.Creator<LabelGroup> {
            override fun createFromParcel(parcel: Parcel): LabelGroup {
                return LabelGroup(parcel)
            }

            override fun newArray(size: Int): Array<LabelGroup?> {
                return arrayOfNulls(size)
            }
        }
    }
}
