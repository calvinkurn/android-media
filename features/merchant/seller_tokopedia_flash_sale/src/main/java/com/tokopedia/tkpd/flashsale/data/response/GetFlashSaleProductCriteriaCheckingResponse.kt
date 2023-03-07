package com.tokopedia.tkpd.flashsale.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductCriteriaCheckingResponse (
    @SerializedName("getFlashSaleProductCriteriaChecking" )
    var getFlashSaleProductCriteriaChecking : GetFlashSaleProductCriteriaChecking = GetFlashSaleProductCriteriaChecking()
) {
    data class ResponseHeader (
        @SerializedName("status"  ) var status  : String  = "",
        @SerializedName("success" ) var success : Boolean = false
    )

    data class Category (
        @SerializedName("is_eligible" ) var isEligible : Boolean = false,
        @SerializedName("name"        ) var name       : String  = ""
    )

    data class Rating (
        @SerializedName("is_eligible" ) var isEligible : Boolean = false,
        @SerializedName("min_rating"  ) var minRating  : Double     = 0.toDouble()
    )

    data class ProductScore (
        @SerializedName("is_eligible"       ) var isEligible      : Boolean = false,
        @SerializedName("min_product_score" ) var minProductScore : Long    = 0
    )

    data class CountSold (
        @SerializedName("is_eligible"    ) var isEligible   : Boolean = false,
        @SerializedName("min_count_sold" ) var minCountSold : Long     = 0,
        @SerializedName("max_count_sold" ) var maxCountSold : Long     = 0
    )

    data class MinOrder (
        @SerializedName("is_eligible" ) var isEligible : Boolean = false,
        @SerializedName("min_order"   ) var minOrder   : Long     = 0
    )

    data class MaxAppearance (
        @SerializedName("is_eligible"            ) var isEligible           : Boolean = false,
        @SerializedName("max_appearance"         ) var maxAppearance        : Long    = 0,
        @SerializedName("day_periode_appearance" ) var dayPeriodeAppearance : Long    = 0
    )

    data class ExcludeWholesale (
        @SerializedName("is_active"   ) var isActive   : Boolean = false,
        @SerializedName("is_eligible" ) var isEligible : Boolean = false
    )

    data class ExcludeSecondHand (
        @SerializedName("is_active"   ) var isActive   : Boolean = false,
        @SerializedName("is_eligible" ) var isEligible : Boolean = false
    )

    data class ExcludePreOrder (
        @SerializedName("is_active"   ) var isActive   : Boolean = false,
        @SerializedName("is_eligible" ) var isEligible : Boolean = false
    )

    data class FreeOngkir (
        @SerializedName("is_active"   ) var isActive   : Boolean = false,
        @SerializedName("is_eligible" ) var isEligible : Boolean = false
    )

    data class Price (
        @SerializedName("is_eligible" ) var isEligible : Boolean = false,
        @SuppressLint("Invalid Data Type") // price is using integer at BE
        @SerializedName("min_price"   ) var minPrice   : Long  = 0,
        @SuppressLint("Invalid Data Type") // price is using integer at BE
        @SerializedName("max_price"   ) var maxPrice   : Long  = 0
    )

    data class Stock (
        @SerializedName("is_eligible" ) var isEligible : Boolean = false,
        @SerializedName("min_stock"   ) var minStock   : Long    = 0
    )

    data class Warehouses (
        @SerializedName("warehouse_id"          ) var warehouseId         : Long    = 0L,
        @SerializedName("city_name"             ) var cityName            : String  = "",
        @SerializedName("is_dilayani_tokopedia" ) var isDilayaniTokopedia : Boolean = false,
        @SuppressLint("Invalid Data Type") // price is using object
        @SerializedName("price"                 ) var price               : Price   = Price(),
        @SerializedName("stock"                 ) var stock               : Stock   = Stock()
    )

    data class ProductList (
        @SerializedName("name"                ) var name              : String              = "",
        @SerializedName("picture_url"         ) var pictureUrl        : String              = "",
        @SerializedName("is_multiwarehouse"   ) var isMultiwarehouse  : Boolean             = false,
        @SerializedName("category"            ) var category          : Category            = Category(),
        @SerializedName("rating"              ) var rating            : Rating              = Rating(),
        @SerializedName("product_score"       ) var productScore      : ProductScore        = ProductScore(),
        @SerializedName("count_sold"          ) var countSold         : CountSold           = CountSold(),
        @SerializedName("min_order"           ) var minOrder          : MinOrder            = MinOrder(),
        @SerializedName("max_appearance"      ) var maxAppearance     : MaxAppearance       = MaxAppearance(),
        @SerializedName("exclude_wholesale"   ) var excludeWholesale  : ExcludeWholesale    = ExcludeWholesale(),
        @SerializedName("exclude_second_hand" ) var excludeSecondHand : ExcludeSecondHand   = ExcludeSecondHand(),
        @SerializedName("exclude_pre_order"   ) var excludePreOrder   : ExcludePreOrder     = ExcludePreOrder(),
        @SerializedName("free_ongkir"         ) var freeOngkir        : FreeOngkir          = FreeOngkir(),
        @SerializedName("warehouses"          ) var warehouses        : List<Warehouses>    = emptyList()
    )

    data class GetFlashSaleProductCriteriaChecking (
        @SerializedName("response_header" ) var responseHeader : ResponseHeader?   = ResponseHeader(),
        @SerializedName("product_list"    ) var productList    : List<ProductList> = emptyList()
    )
}


