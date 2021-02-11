package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShortcutListItem(
	@SerializedName("cta")
	@Expose
	val cta: Cta = Cta(),

	@SerializedName("iconImageURL")
	@Expose
	val iconImageURL: String = "",

	@SerializedName("iconImageURLOptimized")
	@Expose
	val iconImageURLOptimized: String = "",

	@SerializedName("description")
	@Expose
	val description: String = "",

	@SerializedName("id")
	@Expose
	val id: Int = 0
)