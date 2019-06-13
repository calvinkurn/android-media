package com.tokopedia.mlp.contractModel

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class WidgetsItem(

	@SerializedName("bottom_sheet")
	val bottomSheet: List<BottomSheetItem?>? = null,

	@SerializedName("header")
	val header: Header? = null,

	@SerializedName("body")
	val body: List<BodyItem?>? = null
)