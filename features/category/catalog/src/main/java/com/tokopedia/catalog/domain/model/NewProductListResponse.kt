package com.tokopedia.catalog.domain.model

import com.google.gson.annotations.SerializedName

data class NewProductListResponse(
	@SerializedName("searchProduct")
	val searchProduct: CatalogSearchProductForReimaganeResponse.SearchProduct? = null
)
