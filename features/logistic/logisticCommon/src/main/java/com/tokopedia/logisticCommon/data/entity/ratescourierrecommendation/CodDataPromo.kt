package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CodDataPromo(
        @SerializedName("is_cod_available")
        val isCodAvailable: Int? = null,
        @SerializedName("cod_text")
        val codText: String? = null,
        @SerializedName("cod_price")
        val codPrice: Int? = null,
        @SerializedName("formatted_price")
        val formattedPrice: String? = null,
        @SerializedName("tnc_text")
        val tncText: String? = null,
        @SerializedName("tnc_link")
        val tncLink: String? = null
) : Parcelable