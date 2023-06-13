package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductPerCriteriaResponse(
    @SerializedName("getFlashSaleProductPerCriteria" )
    val getFlashSaleProductPerCriteria : GetFlashSaleProductPerCriteria = GetFlashSaleProductPerCriteria()
) {

    data class GetFlashSaleProductPerCriteria (
        @SerializedName("product_criteria" ) val productCriteria : List<ProductCriteria> = emptyList()
    )

    data class ProductCriteria (
        @SerializedName("criteria_id"     ) val criteriaId     : Long               = 0,
        @SerializedName("criteria_name"   ) val criteriaName   : String             = "",
        @SerializedName("max_submission"  ) val maxSubmission  : Int                = 0,
        @SerializedName("count_submitted" ) val countSubmitted : Int                = 0,
        @SerializedName("count_remaining" ) val countRemaining : Int                = 0,
        @SerializedName("category_list"   ) val categoryList   : List<CategoryList> = emptyList()
    )

    data class CategoryList (
        @SerializedName("category_id"   ) val categoryId   : Long   = 0,
        @SerializedName("category_name" ) val categoryName : String = ""
    )
}