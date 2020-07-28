package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.SerializedName

data class ShortcutListItem(

	@SerializedName("cta")
	val cta: Cta? = null,

	@SerializedName("iconImageURL")
	val iconImageURL: String? = null,

	@SerializedName("iconImageURLOptimized")
	val iconImageURLOptimized: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("id")
	val id: Int? = null
)