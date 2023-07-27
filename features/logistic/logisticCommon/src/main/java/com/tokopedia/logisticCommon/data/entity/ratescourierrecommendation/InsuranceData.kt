package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class InsuranceData(
    @SerializedName("insurance_price")
    val insurancePrice: Double = 0.0,

    @SerializedName("insurance_type")
    val insuranceType: Int = 0,

    @SerializedName("insurance_type_info")
    val insuranceTypeInfo: String = "",

    @SerializedName("insurance_used_type")
    val insuranceUsedType: Int = 0,

    @SerializedName("insurance_used_info")
    val insuranceUsedInfo: String = "",

    @SerializedName("insurance_used_default")
    val insuranceUsedDefault: Int = 0
) : Parcelable
