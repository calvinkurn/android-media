package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class EstimatedTimeDeliveryData(
    @SerializedName("min_etd")
    val minEtd: Int = 0,

    @SerializedName("max_etd")
    var maxEtd: Int = 0
) : Parcelable
