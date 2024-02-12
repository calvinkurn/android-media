package com.tokopedia.bmsm_widget.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetOfferProductsBenefitListResponse(
    @SerializedName("GetOfferProductsBenefitList")
    val getOfferProductsBenefitList: GetOfferProductsBenefitList
) {
    data class GetOfferProductsBenefitList(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader,
        @SerializedName("tier_list_gift")
        val tierListGift: List<TierGift>
    ) {
        data class ResponseHeader(
            @SerializedName("errorMessage")
            val errorMessage: List<String>,
            @SerializedName("status")
            val status: String,
            @SerializedName("success")
            val success: Boolean
        )

        data class TierGift(
            @SerializedName("is_eligible")
            val isEligible: Boolean,
            @SerializedName("max_benefit_qty")
            val maxBenefitQty: Int,
            @SerializedName("products")
            val products: List<Product>,
            @SuppressLint("Invalid Data Type")
            @SerializedName("tier_id")
            val tierId: Long,
            @SerializedName("tier_message")
            val tierMessage: String,
            @SerializedName("tier_name")
            val tierName: String
        ) {
            data class Product(
                @SerializedName("finalPrice")
                val finalPrice: Long,
                @SerializedName("is_oos")
                val isOos: Boolean,
                @SerializedName("originalPrice")
                val originalPrice: Long,
                @SerializedName("product_cache_image_url")
                val productCacheImageUrl: String,
                @SerializedName("product_id")
                val productId: Long,
                @SerializedName("product_name")
                val productName: String,
                @SerializedName("quantity")
                val quantity: Int,
                @SerializedName("stock")
                val stock: Long,
                @SuppressLint("Invalid Data Type") 
                @SerializedName("warehouse_id")
                val warehouseId: Long
            )
        }
    }
}
