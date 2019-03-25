package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.VoucherOrdersItem

@Generated("com.robohorse.robopojogenerator")
data class ClashingInfoDetail(

	@field:SerializedName("is_clashed_promos")
	val isClashedPromos: Boolean? = false,

	@field:SerializedName("clash_reason")
	val clashReason: String? = "",

	@field:SerializedName("clash_message")
	val clashMessage: String? = "",

	@field:SerializedName("options")
	val options: ArrayList<ClashingVoucherOption?>? = ArrayList()
)