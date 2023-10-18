package com.tokopedia.affiliate.model.pojo

import com.google.gson.annotations.SerializedName

data class TokonowRemoteConfigData(

	@SerializedName("tokonow_id_staging")
	val tokonowIdStaging: String? = null,

	@SerializedName("imageURL")
	val imageURL: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("weblink")
	val weblink: String? = null,

	@SerializedName("tokonow_id")
	val tokonowId: String? = null
)
