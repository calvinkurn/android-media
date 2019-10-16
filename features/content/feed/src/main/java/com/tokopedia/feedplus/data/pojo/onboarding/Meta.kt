package com.tokopedia.feedplus.data.pojo.onboarding

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Meta(

	@SerializedName("assets")
	@Expose
	val assets: Assets = Assets(),

	@SerializedName("minPicked")
	@Expose
	val minPicked: Int = 0,

	@SerializedName("isEnabled")
	@Expose
	val isEnabled: Boolean = false,

	@SerializedName("source")
	@Expose
	val source: String = ""
)