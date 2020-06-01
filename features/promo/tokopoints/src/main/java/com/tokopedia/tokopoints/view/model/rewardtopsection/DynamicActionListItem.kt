package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class DynamicActionListItem(

	@SerializedName("cta")
	val cta: Cta? = null,

	@SerializedName("metadata")
	val metadata: String? = null,

	@SerializedName("id")
	val id: Int? = null
)