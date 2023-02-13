package com.tokopedia.tkpd.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetFlashSaleForSellerCategoryListResponse(
    @SerializedName("getFlashSaleForSellerCategoryList")
    val getFlashSaleForSellerCategoryList: GetFlashSaleForSellerCategoryList = GetFlashSaleForSellerCategoryList()
) {
    data class GetFlashSaleForSellerCategoryList(
        @SerializedName("category_list")
        val categoryList: List<Category> = listOf()
    ) {
        data class Category(
            @SerializedName("category_id")
            val categoryId: String = "",
            @SerializedName("category_name")
            val categoryName: String = ""
        )
    }
}
