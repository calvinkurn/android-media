package com.tokopedia.tkpd.flashsale.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductListToReserveResponse(
    @SerializedName("getFlashSaleProductListToReserve")
    val getFlashSaleProductListToReserve: GetFlashSaleProductListToReserve = GetFlashSaleProductListToReserve()
) {
    data class GetFlashSaleProductListToReserve (
        @SerializedName("submitted_product_ids" ) val submittedProductIds : List<Long>        = emptyList(),
        @SerializedName("total_product"         ) val totalProduct        : Int               = 0,
        @SerializedName("product_list"          ) val productList         : List<ProductList> = emptyList()
    )

    data class ProductList (
        @SerializedName("product_id"                ) val productId               : Long            = 0,
        @SerializedName("name"                      ) val name                    : String          = "",
        @SerializedName("picture_url"               ) val pictureUrl              : String          = "",
        @SuppressLint("Invalid Data Type") // price is using dataclass
        @SerializedName("price"                     ) val price                   : Price           = Price(),
        @SerializedName("stock"                     ) val stock                   : Int             = 0,
        @SerializedName("count_view"                ) val countView               : Int             = 0,
        @SerializedName("count_sold"                ) val countSold               : Int             = 0,
        @SerializedName("count_eligible_warehouses" ) val countEligibleWarehouses : Int             = 0,
        @SerializedName("product_criteria"          ) val productCriteria         : ProductCriteria = ProductCriteria(),
        @SerializedName("disable_detail"            ) val disableDetail           : DisableDetail   = DisableDetail(),
        @SerializedName("variant_meta"              ) val variantMeta             : VariantMeta     = VariantMeta()
    )

    @SuppressLint("Invalid Data Type") // price is still using integer at GQL
    data class Price (
        @SerializedName("price"       ) val price      : Long = 0,
        @SerializedName("lower_price" ) val lowerPrice : Long = 0,
        @SerializedName("upper_price" ) val upperPrice : Long = 0
    )


    data class ProductCriteria (
        @SerializedName("criteria_id" ) val criteriaId : Long = 0
    )

    data class DisableDetail (
        @SerializedName("is_disabled"                ) val isDisabled              : Boolean = false,
        @SerializedName("disable_title"              ) val disableTitle            : String  = "",
        @SerializedName("disable_description"        ) val disableDescription      : String  = "",
        @SerializedName("show_criteria_checking_cta" ) val showCriteriaCheckingCta : Boolean = false
    )

    data class VariantMeta (
        @SerializedName("count_variants"          ) val countVariants         : Int = 0,
        @SerializedName("count_eligible_variants" ) val countEligibleVariants : Int = 0
    )
}