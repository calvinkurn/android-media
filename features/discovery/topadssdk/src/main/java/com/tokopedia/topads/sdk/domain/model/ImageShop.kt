package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

private const val KEY_COVER = "cover"
private const val KEY_S_URL = "s_url"
private const val KEY_XS_URL = "xs_url"
private const val KEY_COVER_ECS = "cover_ecs"
private const val KEY_S_ECS = "s_ecs"
private const val KEY_XS_ECS = "xs_ecs"

@Parcelize
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
) : ImpressHolder(), Parcelable
