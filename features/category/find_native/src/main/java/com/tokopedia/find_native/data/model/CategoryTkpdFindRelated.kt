package com.tokopedia.find_native.data.model

import com.google.gson.annotations.SerializedName

data class CategoryTkpdFindRelated (
		@SerializedName("relatedHotlist") val relatedHotlist : List<RelatedLinkData>,
		@SerializedName("relatedCategory") val relatedCategory : List<RelatedLinkData>
)