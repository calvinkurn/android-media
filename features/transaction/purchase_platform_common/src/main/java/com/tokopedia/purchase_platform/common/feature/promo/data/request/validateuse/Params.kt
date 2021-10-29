package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Params(
        @field:SerializedName("promo")
        var promo: ValidateUsePromoRequest = ValidateUsePromoRequest()
) : Parcelable