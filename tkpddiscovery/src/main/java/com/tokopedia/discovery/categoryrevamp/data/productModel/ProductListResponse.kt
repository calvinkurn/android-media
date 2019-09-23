package com.tokopedia.discovery.categoryrevamp.data.productModel

import com.google.gson.annotations.SerializedName

data class ProductListResponse(

	@field:SerializedName("searchProduct")
	val searchProduct: SearchProduct? = null
)