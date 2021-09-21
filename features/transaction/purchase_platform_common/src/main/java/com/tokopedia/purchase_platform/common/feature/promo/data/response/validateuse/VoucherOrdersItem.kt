package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class VoucherOrdersItem(

        @field:SerializedName("code")
        val code: String = "",

        @field:SerializedName("unique_id")
        val uniqueId: String = "",

        @field:SerializedName("title_description")
        val titleDescription: String = "",

        @field:SerializedName("is_po")
        val isPo: Int = 0,

        @field:SerializedName("type")
        val type: String = "",

        @field:SerializedName("message")
        val message: Message = Message(),

        @field:SerializedName("duration")
        val duration: String = "",

        @field:SerializedName("success")
        val success: Boolean = false,

        @field:SerializedName("invoice_description")
        val invoiceDescription: String = ""
)