package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class ProductTextData(
    @SerializedName("text_etd")
    val textEtd: String = "",

    @SerializedName("text_price")
    val textPrice: String = ""
) : Parcelable
