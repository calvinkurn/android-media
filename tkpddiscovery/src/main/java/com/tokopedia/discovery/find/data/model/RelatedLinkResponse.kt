package com.tokopedia.discovery.find.data.model

import com.google.gson.annotations.SerializedName

data class RelatedLinkResponse (
	@SerializedName("categoryTkpdFindRelated") val categoryTkpdFindRelated : CategoryTkpdFindRelated
)