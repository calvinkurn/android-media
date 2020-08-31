package com.tokopedia.common_category.model.bannedCategory

import com.google.gson.annotations.SerializedName

data class BannedCategoryResponse(

	@field:SerializedName("CategoryDetailQueryV3")
	val categoryDetailQuery: CategoryDetailQueryV3? = null
)