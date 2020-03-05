package com.tokopedia.purchase_platform.features.promo.data.response.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class AdditionalInfo(

	@field:SerializedName("sp_ids")
	val spIds: List<Int?>? = null,

	@field:SerializedName("message_info")
	val messageInfo: MessageInfo? = null,

	@field:SerializedName("error_detail")
	val errorDetail: ErrorDetail? = null
)