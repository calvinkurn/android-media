package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OntimeDeliveryGuarantee(
    @SerializedName("available")
    val available: Boolean = false,

    @SerializedName("text_label")
    val textLabel: String = "",

    @SerializedName("text_detail")
    val textDetail: String = "",

    @SerializedName("url_detail")
    val urlDetail: String = "",

    @SerializedName("value")
    val value: Int = 0,

    @SerializedName("icon_url")
    val iconUrl: String = "",

    @SerializedName("url_text")
    val urlText: String = ""
) : Parcelable
