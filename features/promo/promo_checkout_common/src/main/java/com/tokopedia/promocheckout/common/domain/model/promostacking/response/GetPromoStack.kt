package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class GetPromoStack(

	@field:SerializedName("data")
	val data: Data = Data(),

	@field:SerializedName("message")
	val message: ArrayList<String>? = ArrayList(),

	@field:SerializedName("status")
	val status: String = ""
)