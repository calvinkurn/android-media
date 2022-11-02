package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

private const val KEY_M_URL = "m_url"
private const val KEY_S_URL = "s_url"
private const val KEY_XS_URL = "xs_url"
private const val KEY_M_ECS = "m_ecs"
private const val KEY_S_ECS = "s_ecs"
private const val KEY_XS_ECS = "xs_ecs"

@Parcelize
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
) : ImpressHolder(), Parcelable
