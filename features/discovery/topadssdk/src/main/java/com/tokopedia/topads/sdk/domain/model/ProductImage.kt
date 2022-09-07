package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import org.json.JSONObject

private const val KEY_M_URL = "m_url"
private const val KEY_S_URL = "s_url"
private const val KEY_XS_URL = "xs_url"
private const val KEY_M_ECS = "m_ecs"
private const val KEY_S_ECS = "s_ecs"
private const val KEY_XS_ECS = "xs_ecs"

data class ProductImage(
    @SerializedName(KEY_M_URL)
    var m_url: String = "",

    @SerializedName(KEY_S_URL)
    var s_url: String = "",

    @SerializedName(KEY_XS_URL)
    var xs_url: String = "",

    @SerializedName(KEY_M_ECS)
    var m_ecs: String = "",

    @SerializedName(KEY_S_ECS)
    var s_ecs: String = "",

    @SerializedName(KEY_XS_ECS)
    var xs_ecs: String = ""
) : ImpressHolder(), Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_M_URL)) {
            m_url = jSONObject.getString(KEY_M_URL)
        } else if (!jSONObject.isNull(KEY_S_URL)) {
            m_url = jSONObject.getString(KEY_S_URL)
        }
        if (!jSONObject.isNull(KEY_S_URL)) {
            s_url = jSONObject.getString(KEY_S_URL)
        }
        if (!jSONObject.isNull(KEY_XS_URL)) {
            xs_url = jSONObject.getString(KEY_XS_URL)
        }
        if (!jSONObject.isNull(KEY_M_ECS)) {
            m_ecs = jSONObject.getString(KEY_M_ECS)
        } else if (!jSONObject.isNull(KEY_S_ECS)) {
            m_ecs = jSONObject.getString(KEY_S_ECS)
        }
        if (!jSONObject.isNull(KEY_S_ECS)) {
            s_ecs = jSONObject.getString(KEY_S_ECS)
        }
        if (!jSONObject.isNull(KEY_XS_ECS)) {
            xs_ecs = jSONObject.getString(KEY_XS_ECS)
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
        dest.writeString(m_url)
        dest.writeString(s_url)
        dest.writeString(xs_url)
        dest.writeString(m_ecs)
        dest.writeString(s_ecs)
        dest.writeString(xs_ecs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductImage> = object : Parcelable.Creator<ProductImage> {
            override fun createFromParcel(parcel: Parcel): ProductImage {
                return ProductImage(parcel)
            }

            override fun newArray(size: Int): Array<ProductImage?> {
                return arrayOfNulls(size)
            }
        }
    }
}
