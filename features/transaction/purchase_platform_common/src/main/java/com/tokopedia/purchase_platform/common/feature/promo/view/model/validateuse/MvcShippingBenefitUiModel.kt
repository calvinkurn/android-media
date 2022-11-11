package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MvcShippingBenefitUiModel(
        @SerializedName("benefit_amount")
        var benefitAmount: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        var spId: Int = 0
) : Parcelable
