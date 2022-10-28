package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName

data class ShortcutListItem(
	@SerializedName("cta")
	val cta: Cta = Cta(),

	@SerializedName("iconImageURL")
	val iconImageURL: String = "",

	@SerializedName("iconImageURLOptimized")
	val iconImageURLOptimized: String = "",

	@SerializedName("description")
	val description: String = "",

	@SerializedName("id")
	val id: String = "0"
)