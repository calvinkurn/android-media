package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by fajarnuha on 18/12/18.
 */
@Parcelize
data class CodProductData(
    @SerializedName("is_cod_available")
    val isCodAvailable: Int = 0,

    @SerializedName("cod_text")
    val codText: String = "",

    @SerializedName("cod_price")
    val codPrice: Double = 0.0,

    @SerializedName("formatted_price")
    val formattedPrice: String = "",

    @SerializedName("tnc_text")
    val tncText: String = "",

    @SerializedName("tnc_link")
    val tncLink: String = ""
) : Parcelable
