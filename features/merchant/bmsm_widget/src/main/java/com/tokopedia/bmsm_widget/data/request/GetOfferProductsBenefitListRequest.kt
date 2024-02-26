package com.tokopedia.bmsm_widget.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetOfferProductsBenefitListRequest(
    @SerializedName("filter")
    val filter: Filter,
    @SerializedName("request_header")
    val requestHeader: RequestHeader,
    @SerializedName("user_location")
    val userLocation: UserLocation
) {
    data class Filter(
        @SuppressLint("Invalid Data Type")
        @SerializedName("offer_id")
        val offerId: Long,
        @SerializedName("tier_product")
        val tierProduct: List<TierProduct> = emptyList(),
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,
        @SerializedName("main_product")
        val mainProducts: List<MainProduct> = emptyList()
    ) {
        data class TierProduct(
            @SerializedName("product_benefit")
            val productBenefit: List<ProductBenefit>,
            @SuppressLint("Invalid Data Type")
            @SerializedName("tier_id")
            val tierId: Long
        ) {
            data class ProductBenefit(
                @SuppressLint("Invalid Data Type")
                @SerializedName("product_id")
                val productId: Long,
                @SerializedName("quantity")
                val quantity: Int
            )
        }
        
        data class MainProduct(
            @SerializedName("product_id")
            val productId: Long,
            @SerializedName("quantity")
            val quantity: Int
        )
        
    }

    data class UserLocation(
        @SerializedName("address_id")
        val addressId: Long,
        @SerializedName("district_id")
        val districtId: Long,
        @SerializedName("postal_code")
        val postalCode: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("city_id")
        val cityId: Long
    )

    data class RequestHeader(
        @SerializedName("device")
        val device: String,
        @SerializedName("ip")
        val ip: String,
        @SerializedName("source")
        val source: String,
        @SerializedName("usecase")
        val useCase: String,
        @SerializedName("version")
        val version: String
    )
}
