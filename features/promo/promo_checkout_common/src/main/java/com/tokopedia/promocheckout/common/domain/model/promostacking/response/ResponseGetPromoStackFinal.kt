package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class ResponseGetPromoStackFinal(

	@field:SerializedName("get_promo_stack_use")
	val getPromoStackUse: GetPromoStack = GetPromoStack()
)