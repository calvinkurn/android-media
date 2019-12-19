package com.tokopedia.promocheckout.detail.model.userpoints

import com.google.gson.annotations.SerializedName

data class Points(

	@SerializedName("reward")
	val reward: Int? = null,

	@SerializedName("rewardStr")
	val rewardStr: String? = null,

	@SerializedName("loyaltyStr")
	val loyaltyStr: String? = null,

	@SerializedName("loyalty")
	val loyalty: Int? = null,

	@SerializedName("rewardExpiryInfo")
	val rewardExpiryInfo: String? = null,

	@SerializedName("loyaltyExpiryInfo")
	val loyaltyExpiryInfo: String? = null
)
