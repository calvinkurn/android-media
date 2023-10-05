package com.tokopedia.purchase_platform.common.feature.promo.data.response.getlastapply

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUsePromoRevamp

data class GetLastApplyResponse(

    @field:SerializedName("get_last_apply_promo")
    val validateUsePromoRevamp: ValidateUsePromoRevamp = ValidateUsePromoRevamp()
)
