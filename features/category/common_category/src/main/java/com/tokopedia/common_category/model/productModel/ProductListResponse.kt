package com.tokopedia.common_category.model.productModel

import com.google.gson.annotations.SerializedName

data class ProductListResponse(

	@field:SerializedName("searchProduct")
	val searchProduct: SearchProduct? = null
)