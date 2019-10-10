package com.tokopedia.promocheckout.detail.model.userpoints
import com.google.gson.annotations.SerializedName

data class Tier(

	@SerializedName("nameDesc")
	val nameDesc: String? = null,

	@SerializedName("eggImageURL")
	val eggImageURL: String? = null
)
