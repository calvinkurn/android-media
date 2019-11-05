package com.tokopedia.tokopoints.view.model.addpointsection

import com.google.gson.annotations.SerializedName

data class SheetHowToGetV2(

	@SerializedName("subTitle")
	val subTitle: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("sections")
	val sections: List<SectionsItem?>? = null
)
