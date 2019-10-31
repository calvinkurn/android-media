package com.tokopedia.discovery.categoryrevamp.data.catalogModel

import com.google.gson.annotations.SerializedName

data class CatalogListResponse(

	@field:SerializedName("searchCatalog")
	val searchCatalog: SearchCatalog? = null
)