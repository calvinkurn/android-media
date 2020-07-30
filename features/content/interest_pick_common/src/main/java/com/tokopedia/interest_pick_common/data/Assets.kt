package com.tokopedia.interest_pick_common.data

import com.google.gson.annotations.Expose
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class Assets(

	@SerializedName("titleIntro")
	@Expose
	val titleIntro: String = "",

	@SerializedName("instruction")
	@Expose
	val instruction: String = "",

	@SerializedName("titleFull")
	@Expose
	val titleFull: String = "",

	@SerializedName("buttonCta")
	@Expose
	val buttonCta: String = ""
)