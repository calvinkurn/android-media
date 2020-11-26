package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsShortcutList(

	@SerializedName("shortcutGroupList")
	@Expose
	val shortcutGroupList: List<ShortcutGroupListItem> = arrayListOf()
)