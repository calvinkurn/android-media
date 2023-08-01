package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class RangePriceData(
    @SerializedName("min_price")
    val minPrice: Double = 0.0,

    @SerializedName("max_price")
    val maxPrice: Double = 0.0
) : Parcelable
