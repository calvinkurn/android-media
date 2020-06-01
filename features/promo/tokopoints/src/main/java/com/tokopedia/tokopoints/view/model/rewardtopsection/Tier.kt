package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class Tier(

	@SerializedName("nameDesc")
	val nameDesc: String? = null,

	@SerializedName("backgroundImgURLMobile")
	val backgroundImgURLMobile: String? = null
)