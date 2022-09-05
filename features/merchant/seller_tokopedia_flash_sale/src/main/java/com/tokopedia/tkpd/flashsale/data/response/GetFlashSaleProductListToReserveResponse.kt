package com.tokopedia.tkpd.flashsale.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductListToReserveResponse(
    @SerializedName("getFlashSaleProductListToReserve")
    val getFlashSaleProductListToReserve: GetFlashSaleProductListToReserve = GetFlashSaleProductListToReserve()
) {
    data class GetFlashSaleProductListToReserve (
        @SerializedName("submitted_product_ids" ) var submittedProductIds : List<Int>         = arrayListOf(),
        @SerializedName("total_product"         ) var totalProduct        : Int               = 0,
        @SerializedName("product_list"          ) var productList         : List<ProductList> = arrayListOf()
    )

    data class ProductList (
        @SerializedName("product_id"                ) var productId               : Long            = 0,
        @SerializedName("name"                      ) var name                    : String          = "",
        @SerializedName("picture_url"               ) var pictureUrl              : String          = "",
        @SuppressLint("Invalid Data Type") // price is using dataclass
        @SerializedName("price"                     ) var price                   : Price           = Price(),
        @SerializedName("stock"                     ) var stock                   : Int             = 0,
        @SerializedName("count_view"                ) var countView               : Int             = 0,
        @SerializedName("count_sold"                ) var countSold               : Int             = 0,
        @SerializedName("count_eligible_warehouses" ) var countEligibleWarehouses : Int             = 0,
        @SerializedName("product_criteria"          ) var productCriteria         : ProductCriteria = ProductCriteria(),
        @SerializedName("disable_detail"            ) var disableDetail           : DisableDetail   = DisableDetail(),
        @SerializedName("variant_meta"              ) var variantMeta             : VariantMeta     = VariantMeta()
    )

    @SuppressLint("Invalid Data Type") // price is still using integer at GQL
    data class Price (
        @SerializedName("price"       ) var price      : Long = 0,
        @SerializedName("lower_price" ) var lowerPrice : Long = 0,
        @SerializedName("upper_price" ) var upperPrice : Long = 0
    )


    data class ProductCriteria (
        @SerializedName("criteria_id" ) var criteriaId : Long = 0
    )

    data class DisableDetail (
        @SerializedName("is_disabled"                ) var isDisabled              : Boolean = false,
        @SerializedName("disable_title"              ) var disableTitle            : String  = "",
        @SerializedName("disable_description"        ) var disableDescription      : String  = "",
        @SerializedName("show_criteria_checking_cta" ) var showCriteriaCheckingCta : Boolean = false
    )

    data class VariantMeta (
        @SerializedName("count_variants"          ) var countVariants         : Int = 0,
        @SerializedName("count_eligible_variants" ) var countEligibleVariants : Int = 0
    )
}