package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class ServiceTextData(
    @SerializedName("text_range_price")
    @Expose
    var textRangePrice: String = "",

    @SerializedName("text_etd")
    @Expose
    val textEtd: String = "",

    @SerializedName("text_service_desc")
    @Expose
    val textServiceDesc: String = "",

    @SerializedName("text_eta_summarize")
    @Expose
    val textEtaSummarize: String = "",

    @SerializedName("error_code")
    @Expose
    val errorCode: Int = 0
) : Parcelable
