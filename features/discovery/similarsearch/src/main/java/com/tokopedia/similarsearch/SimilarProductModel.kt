package com.tokopedia.similarsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class SimilarProductModel(
        @SerializedName("similar_products_image_search")
        @Expose
        val similarProductsImageSearch: SimilarProductsImageSearch = SimilarProductsImageSearch()
) {

        fun getSimilarProductList(): List<Product> {
                return similarProductsImageSearch.data.productList
        }

        fun getOriginalProduct(): Product {
                return similarProductsImageSearch.data.originalProduct
        }
}