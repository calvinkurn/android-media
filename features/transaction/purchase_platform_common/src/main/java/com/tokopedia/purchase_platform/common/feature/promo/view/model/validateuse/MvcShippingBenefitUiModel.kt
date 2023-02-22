package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MvcShippingBenefitUiModel(
        @SerializedName("benefit_amount")
        var benefitAmount: Int = 0,
        @SerializedName("sp_id")
        var spId: Long = 0
) : Parcelable
