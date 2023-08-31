package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class ServiceTextData(
    @SerializedName("text_range_price")
    var textRangePrice: String = "",

    @SerializedName("text_etd")
    val textEtd: String = "",

    @SerializedName("text_service_desc")
    val textServiceDesc: String = "",

    @SerializedName("text_eta_summarize")
    val textEtaSummarize: String = "",

    @SerializedName("error_code")
    val errorCode: Int = 0,

    @SerializedName("text_service_ticker")
    val textServiceTicker: String = ""
) : Parcelable
