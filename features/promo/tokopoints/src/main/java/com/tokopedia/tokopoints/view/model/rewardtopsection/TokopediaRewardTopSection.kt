package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class TokopediaRewardTopSection(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("profilePicture")
	val profilePicture: String? = null,

	@SerializedName("tier")
	val tier: Tier? = null,

	@SerializedName("dynamicActionList")
	val dynamicActionList: List<DynamicActionListItem?>? = null,

	@SerializedName("introductionText")
	val introductionText: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("targetText")
	val targetText: String? = null
)