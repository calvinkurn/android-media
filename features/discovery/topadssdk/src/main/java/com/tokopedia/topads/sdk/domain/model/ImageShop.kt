package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import org.json.JSONObject

private const val KEY_COVER = "cover"
private const val KEY_S_URL = "s_url"
private const val KEY_XS_URL = "xs_url"
private const val KEY_COVER_ECS = "cover_ecs"
private const val KEY_S_ECS = "s_ecs"
private const val KEY_XS_ECS = "xs_ecs"

data class ImageShop(
    @SerializedName(KEY_COVER)
    var cover: String = "",

    @SerializedName(KEY_S_URL)
    var sUrl: String = "",

    @SerializedName(KEY_XS_URL)
    var xsUrl: String = "",

    @SerializedName(KEY_COVER_ECS)
    var coverEcs: String = "",

    @SerializedName(KEY_S_ECS)
    var sEcs: String = "",

    @SerializedName(KEY_XS_ECS)
    var xsEcs: String = ""
) : ImpressHolder(), Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_COVER)) {
            cover = jSONObject.getString(KEY_COVER)
        }
        if (!jSONObject.isNull(KEY_S_URL)) {
            sUrl = (jSONObject.getString(KEY_S_URL))
        }
        if (!jSONObject.isNull(KEY_XS_URL)) {
            xsUrl = jSONObject.getString(KEY_XS_URL)
        }
        if (!jSONObject.isNull(KEY_COVER_ECS)) {
            coverEcs = jSONObject.getString(KEY_COVER_ECS)
        }
        if (!jSONObject.isNull(KEY_S_ECS)) {
            sEcs = (jSONObject.getString(KEY_S_ECS))
        }
        if (!jSONObject.isNull(KEY_XS_ECS)) {
            xsEcs = jSONObject.getString(KEY_XS_ECS)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(cover)
        dest.writeString(sUrl)
        dest.writeString(xsUrl)
        dest.writeString(coverEcs)
        dest.writeString(sEcs)
        dest.writeString(xsEcs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ImageShop> = object : Parcelable.Creator<ImageShop> {
            override fun createFromParcel(parcel: Parcel): ImageShop {
                return ImageShop(parcel)
            }

            override fun newArray(size: Int): Array<ImageShop?> {
                return arrayOfNulls(size)
            }
        }
    }
}
