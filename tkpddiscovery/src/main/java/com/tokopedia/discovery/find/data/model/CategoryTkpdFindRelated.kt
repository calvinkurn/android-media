package com.tokopedia.discovery.find.data.model

import com.google.gson.annotations.SerializedName

data class CategoryTkpdFindRelated (
		@SerializedName("relatedHotlist") var relatedHotlist : List<RelatedLinkData>,
		@SerializedName("relatedCategory") var relatedCategory : List<RelatedLinkData>
)