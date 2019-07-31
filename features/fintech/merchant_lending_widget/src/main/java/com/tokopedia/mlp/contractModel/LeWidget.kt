package com.tokopedia.mlp.contractModel

import com.google.gson.annotations.SerializedName
data class LeWidget(

	@SerializedName("widgets")
	val widgets: List<WidgetsItem?>? = null
)

