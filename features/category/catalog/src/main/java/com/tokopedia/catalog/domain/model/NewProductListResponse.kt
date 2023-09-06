package com.tokopedia.catalog.domain.model

import com.google.gson.annotations.SerializedName

data class NewProductListResponse(

	@field:SerializedName("searchProduct")
	val searchProduct: CatalogSearchProductForReimaganeResponse.SearchProduct? = null
)
