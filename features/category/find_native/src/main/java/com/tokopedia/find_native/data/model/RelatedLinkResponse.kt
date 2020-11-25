package com.tokopedia.find_native.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.find_native.data.model.CategoryTkpdFindRelated

data class RelatedLinkResponse (
	@SerializedName("categoryTkpdFindRelated") val categoryTkpdFindRelated : CategoryTkpdFindRelated
)