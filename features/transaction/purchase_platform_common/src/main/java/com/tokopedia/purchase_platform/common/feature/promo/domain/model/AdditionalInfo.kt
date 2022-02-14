package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class AdditionalInfo(
        @field:SerializedName("sp_ids")
        val spIds: List<Int> = emptyList(),
        @field:SerializedName("message_info")
        val messageInfo: MessageInfo = MessageInfo(),
        @field:SerializedName("error_detail")
        val errorDetail: ErrorDetail = ErrorDetail(),
        @field:SerializedName("empty_cart_info")
        val cartEmptyInfo: CartEmptyInfo = CartEmptyInfo(),
        @field:SerializedName("usage_summaries")
        val listUsageSummaries: List<UsageSummaries> = emptyList(),
        @field:SerializedName("promo_sp_ids")
        val promoSpIds: List<PromoSpId> = emptyList()
)