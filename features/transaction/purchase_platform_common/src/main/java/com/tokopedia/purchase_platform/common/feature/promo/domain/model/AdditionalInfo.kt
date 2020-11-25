package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class AdditionalInfo(

        @field:SerializedName("sp_ids")
        val spIds: List<Int?>? = null,

        @field:SerializedName("message_info")
        val messageInfo: MessageInfo? = null,

        @field:SerializedName("error_detail")
        val errorDetail: ErrorDetail? = null,

        @field:SerializedName("empty_cart_info")
        val cartEmptyInfo: CartEmptyInfo? = null,

        @field:SerializedName("usage_summaries")
        val listUsageSummaries: List<UsageSummaries>? = null,

        @field:SerializedName("promo_sp_ids")
        val promoSpIds: List<PromoSpId> = emptyList()
)