package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.SerializedName

data class TokopointsShortcutList(

	@SerializedName("shortcutGroupList")
	val shortcutGroupList: List<ShortcutGroupListItem?>? = null
)