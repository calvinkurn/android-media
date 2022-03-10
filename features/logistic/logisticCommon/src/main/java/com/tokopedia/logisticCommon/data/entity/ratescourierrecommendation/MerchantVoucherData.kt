package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantVoucherData (
        @SerializedName("is_mvc")
        @Expose
        val isMvc: Int,
        @SerializedName("mvc_title")
        @Expose
        val mvcTitle: String,
        @SerializedName("mvc_logo")
        @Expose
        val mvcLogo: String,
        @SerializedName("mvc_error_message")
        @Expose
        val mvcErrorMessage: String
) : Parcelable