package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class PriceData(
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("formatted_price")
    var formattedPrice: String = ""
) : Parcelable
