package com.tokopedia.kol.feature.following_list.data.pojo

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Meta(

	@SerializedName("nextCursor")
	@Expose
	val nextCursor: String = "",

	@SerializedName("limit")
	@Expose
	val limit: Int = 0,

	@SerializedName("currentCursor")
	@Expose
	val currentCursor: String = "",

	@SerializedName("currentTotal")
	@Expose
	val currentTotal: Int = 0
)