package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class VoucherOrdersItem(
		@field:SerializedName("code")
		val code: String? = null,

		@field:SerializedName("unique_id")
		val uniqueId: String? = null,

		@field:SerializedName("message")
		val message: Message? = null
)