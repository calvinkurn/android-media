package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShortcutGroupListItem(

	@SerializedName("shortcutList")
	@Expose
	val shortcutList: List<ShortcutListItem> = arrayListOf(),

	@SerializedName("groupCode")
	@Expose
	val groupCode: String = ""
)