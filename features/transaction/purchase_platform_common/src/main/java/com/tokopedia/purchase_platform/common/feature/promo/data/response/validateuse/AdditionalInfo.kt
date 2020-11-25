package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSpId

@Generated("com.robohorse.robopojogenerator")
data class AdditionalInfo(

        @field:SerializedName("sp_ids")
        val spIds: List<Int> = emptyList(),

        @field:SerializedName("message_info")
        val messageInfo: MessageInfo = MessageInfo(),

        @field:SerializedName("error_detail")
        val errorDetail: ErrorDetail = ErrorDetail(),

        @field:SerializedName("empty_cart_info")
        val emptyCartInfo: EmptyCartInfo = EmptyCartInfo(),

        @field:SerializedName("usage_summaries")
        val usageSummaries: List<UsageSummaries> = emptyList(),

        @field:SerializedName("promo_sp_ids")
        val promoSpIds: List<PromoSpId> = emptyList()

)