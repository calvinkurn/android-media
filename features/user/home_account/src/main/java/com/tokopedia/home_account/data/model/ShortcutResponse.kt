package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShortcutResponse(
	@SerializedName("tokopointsShortcutList")
	@Expose
	val tokopointsShortcutList: TokopointsShortcutList = TokopointsShortcutList(),
	@SerializedName("tokopointsStatusFiltered")
	@Expose
	val tokopointsStatusFiltered: TokopointsStatusFiltered = TokopointsStatusFiltered()
)