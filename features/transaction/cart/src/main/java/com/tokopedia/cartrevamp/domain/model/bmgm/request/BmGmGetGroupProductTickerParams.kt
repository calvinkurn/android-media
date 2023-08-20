package com.tokopedia.cartrevamp.domain.model.bmgm.request

import com.google.gson.annotations.SerializedName

data class BmGmGetGroupProductTickerParams(
    @SerializedName("lang")
    var lang: String = "id",

    @SerializedName("carts")
    var carts: List<BmGmCart> = emptyList()
) {
    data class BmGmCart(
        @SerializedName("cart_string_order")
        var cartStringOrder: String = "",

        @SerializedName("cart_details")
        var cartDetails: List<BmGmCartDetails> = emptyList()
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
                var cartId: String = ""
            )
        }
    }
}
