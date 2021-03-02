package com.tokopedia.catalog.model.raw

import com.google.gson.annotations.SerializedName

data class ProductListResponse(

	@field:SerializedName("searchProduct")
	val searchProduct: CatalogSearchProductResponse.SearchProduct? = null
)