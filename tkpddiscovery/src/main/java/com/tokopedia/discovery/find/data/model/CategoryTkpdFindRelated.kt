package com.tokopedia.discovery.find.data.model

import com.google.gson.annotations.SerializedName

data class CategoryTkpdFindRelated (
		@SerializedName("relatedHotlist") val relatedHotlist : List<RelatedLinkData>,
		@SerializedName("relatedCategory") val relatedCategory : List<RelatedLinkData>
)