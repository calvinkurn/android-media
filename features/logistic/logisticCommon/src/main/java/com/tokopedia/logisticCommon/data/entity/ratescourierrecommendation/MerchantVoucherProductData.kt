package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantVoucherProductData(
    @SerializedName("is_mvc")
    val isMvc: Int = 0,
    @SerializedName("mvc_logo")
    val mvcLogo: String = "",
    @SerializedName("mvc_error_message")
    val mvcErrorMessage: String = ""
) : Parcelable
