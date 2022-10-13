package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_IS_ACTIVE = "is_active"
private const val KEY_IMG_URL = "img_url"

data class FreeOngkir(
    @SerializedName(KEY_IS_ACTIVE)
    @Expose
    var isActive: Boolean = false,

    @SerializedName(KEY_IMG_URL)
    @Expose
    var imageUrl: String = ""
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_IS_ACTIVE)) {
            isActive = jSONObject.getBoolean(KEY_IS_ACTIVE)
        }
        if (!jSONObject.isNull(KEY_IMG_URL)) {
            imageUrl = jSONObject.getString(KEY_IMG_URL)
        }
    }

    constructor(parcel: Parcel) : this() {
        isActive = parcel.readByte().toInt() != 0
        imageUrl = parcel.readString() ?: ""
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte((if (isActive) 1 else 0).toByte())
        dest.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<FreeOngkir> = object : Parcelable.Creator<FreeOngkir> {
            override fun createFromParcel(parcel: Parcel): FreeOngkir {
                return FreeOngkir(parcel)
            }

            override fun newArray(size: Int): Array<FreeOngkir?> {
                return arrayOfNulls(size)
            }
        }
    }
}
