package com.tokopedia.purchase_platform.features.promo.data.response.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

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
        val usageSummaries: List<UsageSummaries> = emptyList()
)