package com.tokopedia.catalog.model.raw

import com.google.gson.annotations.SerializedName
import com.tokopedia.common_category.model.productModel.SearchProduct

data class AceSearchProductResponse(

        @field:SerializedName("ace_search_product")
        val aceSearchProduct: AceSearchProduct? = null
){

    data class AceSearchProduct(
            @field:SerializedName("data")
            val searchData: SearchProduct? = null,
            @field:SerializedName("header")
            val header: Header? = null
    ){
        data class Header(
                @field:SerializedName("totalData")
                val totalData: Int = 0
        )
    }
}