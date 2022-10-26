package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_NAME = "name"
private const val KEY_IS_AD = "is_ad"

data class Template(
    @SerializedName(KEY_NAME)
    var name: String = "",

    @SerializedName(KEY_IS_AD)
    var isIsAd: Boolean = false
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    ) {
    }

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_NAME)) {
            name = jSONObject.getString(KEY_NAME)
        }
        if (!jSONObject.isNull(KEY_IS_AD)) {
            isIsAd = (jSONObject.getBoolean(KEY_IS_AD))
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeByte((if (isIsAd) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Template> = object : Parcelable.Creator<Template> {
            override fun createFromParcel(parcel: Parcel): Template {
                return Template(parcel)
            }

            override fun newArray(size: Int): Array<Template?> {
                return arrayOfNulls(size)
            }
        }
    }

}
