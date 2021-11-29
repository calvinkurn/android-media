package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShortcutResponse(
	@SerializedName("tokopointsShortcutList")
	@Expose
	val tokopointsShortcutList: TokopointsShortcutList = TokopointsShortcutList(),
	@SerializedName("tokopoints")
	@Expose
	val tokopointsStatusFiltered: TokopointsStatusFiltered = TokopointsStatusFiltered()
)