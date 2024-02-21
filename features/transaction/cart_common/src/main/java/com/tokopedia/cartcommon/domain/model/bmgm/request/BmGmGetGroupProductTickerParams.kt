package com.tokopedia.cartcommon.domain.model.bmgm.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class BmGmGetGroupProductTickerParams(
    @SerializedName("lang")
    var lang: String = "id",

    @SerializedName("type")
    var type: String = "bmgm",

    @SerializedName("source")
    var source: String = SOURCE_CART,

    @SerializedName("carts")
    var carts: ArrayList<BmGmCart> = arrayListOf()
) {
    data class BmGmCart(
        @SerializedName("cart_string_order")
        var cartStringOrder: String = "",

        @SerializedName("cart_details")
        var cartDetails: ArrayList<BmGmCartDetails> = arrayListOf()
    ) {
        data class BmGmCartDetails(
            @SerializedName("bundle_detail")
            var bundleDetail: BundleDetail = BundleDetail(),

            @SerializedName("offer")
            var offer: Offer = Offer(),

            @SerializedName("products")
            var products: List<Product> = emptyList()
        ) {
            data class BundleDetail(
                @SerializedName("bundle_id")
                var bundleId: Long = 0L,

                @SerializedName("bundle_group_id")
                var bundleGroupId: String = ""
            )

            data class Offer(
                @SerializedName("offer_id")
                var offerId: Long = 0L,

                @SerializedName("offer_json_data")
                var offerJsonData: String = "",

                @SerializedName("tier_products")
                var tierProducts: List<TierProducts> = emptyList()
            ) {

                data class TierProducts(
                    @SerializedName("tier_id")
                    val tierId: Long = 0L,
                    @SerializedName("products_benefit")
                    val productsBenefit: List<ProductsBenefit> = emptyList()
                ) {

                    data class ProductsBenefit(
                        @SerializedName("product_id")
                        val productId: Long = 0L,
                        @SerializedName("quantity")
                        val quantity: Int = 0
                    )
                }
            }

            data class Product(
                @SerializedName("cart_id")
                var cartId: String = "",

                @SerializedName("shop_id")
                var shopId: String = "",

                @SerializedName("product_id")
                var productId: String = "",

                @SerializedName("warehouse_id")
                var warehouseId: String = "",

                @SerializedName("qty")
                var qty: Int = 0,

                @SuppressLint("Invalid Data Type")
                @SerializedName("final_price")
                var finalPrice: Long = 0,

                @SerializedName("checkbox_state")
                var checkboxState: Boolean = false
            )
        }
    }

    companion object {
        const val SOURCE_CART = "cart"
        const val SOURCE_OFFER_PAGE = "offer_page"
    }
}
