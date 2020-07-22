package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.SerializedName

data class ShortcutGroupListItem(

	@SerializedName("shortcutList")
	val shortcutList: List<ShortcutListItem?>? = null,

	@SerializedName("groupCode")
	val groupCode: String? = null
)