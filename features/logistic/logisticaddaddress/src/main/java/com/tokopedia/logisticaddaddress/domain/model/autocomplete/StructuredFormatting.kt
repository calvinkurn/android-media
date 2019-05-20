package com.tokopedia.logisticaddaddress.domain.model.autocomplete

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class StructuredFormatting(

	@field:SerializedName("main_text_matched_substrings")
	val mainTextMatchedSubstrings: List<MainTextMatchedSubstringsItem> = emptyList(),

	@field:SerializedName("secondary_text")
	val secondaryText: String = "",

	@field:SerializedName("main_text")
	val mainText: String = ""
)