package com.tokopedia.common_category.data.catalogModel

import com.google.gson.annotations.SerializedName

data class CatalogListResponse(

	@field:SerializedName("searchCatalog")
	val searchCatalog: SearchCatalog? = null
)