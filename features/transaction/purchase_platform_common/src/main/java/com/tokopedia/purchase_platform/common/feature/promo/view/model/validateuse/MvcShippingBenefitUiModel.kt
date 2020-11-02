package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MvcShippingBenefitUiModel(
        @SerializedName("benefit_amount")
        var benefitAmount: Int = 0,
        @SerializedName("sp_id")
        var spId: Int = 0
) : Parcelable