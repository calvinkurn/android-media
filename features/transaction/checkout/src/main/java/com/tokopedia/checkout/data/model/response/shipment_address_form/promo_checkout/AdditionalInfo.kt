package com.tokopedia.checkout.data.model.response.shipment_address_form.promo_checkout

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
		val listUsageSummaries: List<UsageSummaries>? = null
)