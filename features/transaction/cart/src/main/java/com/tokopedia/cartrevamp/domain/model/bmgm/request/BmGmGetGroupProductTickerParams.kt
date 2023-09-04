package com.tokopedia.cartrevamp.domain.model.bmgm.request

import com.google.gson.annotations.SerializedName

data class BmGmGetGroupProductTickerParams(
    @SerializedName("lang")
    var lang: String = "id",

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
                var offerJsonData: String = ""
            )

            data class Product(
                @SerializedName("cart_id")
                var cartId: String = "",

                @SerializedName("shop_id")
                var shopId: String = "",

                @SerializedName("product_id")
                var productId: String = "",

                @SerializedName("warehouse_id")
                var warehouseId: Long = 0L,

                @SerializedName("qty")
                var qty: Int = 0,

                @SerializedName("final_price")
                var finalPrice: Double = 0.0,

                @SerializedName("checkbox_state")
                var checkboxState: Boolean = false
            )
        }
    }
}
