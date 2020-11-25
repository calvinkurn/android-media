package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class DynamicActionListItem(

	@SerializedName("cta")
	val cta: Cta? = null,

	@SerializedName("iconImageURL")
	val iconImageURL: String? = null,

	@SerializedName("iconImageURLOptimized")
	val iconImageURLOptimized: String? = null,

	@SerializedName("iconImageURLOptimizedScrolled")
	val iconImageURLOptimizedScrolled: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("counter")
	val counter: Counter? = null,

	@SerializedName("counterTotal")
	val counterTotal: CounterTotal? = null,

	@SerializedName("iconImageURLScrolled")
	val iconImageURLScrolled: String? = null
)