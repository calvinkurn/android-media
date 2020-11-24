package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsShortcutList(

	@SerializedName("shortcutGroupList")
	@Expose
	val shortcutGroupList: List<ShortcutGroupListItem> = arrayListOf()
)