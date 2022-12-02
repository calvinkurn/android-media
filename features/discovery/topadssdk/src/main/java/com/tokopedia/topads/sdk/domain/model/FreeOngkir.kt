package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

private const val KEY_IS_ACTIVE = "is_active"
private const val KEY_IMG_URL = "img_url"

@Parcelize
data class FreeOngkir(
    @SerializedName(KEY_IS_ACTIVE)
    @Expose
    var isActive: Boolean = false,

    @SerializedName(KEY_IMG_URL)
    @Expose
    var imageUrl: String = ""
) : Parcelable
