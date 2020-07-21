package com.tokopedia.home.account.data.model.tokopointshortcut

import com.google.gson.annotations.SerializedName

data class ShortcutResponse(

	@SerializedName("tokopointsShortcutList")
	val tokopointsShortcutList: TokopointsShortcutList?= null
)