package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
data class ProductTagging(
        @SerializedName("products")
        val listOfProducts: List<Product> = emptyList(),
        @SerializedName("vouchers")
        val listOfVouchers: List<Voucher> = emptyList(),
        @SerializedName("config")
        val config: Config = Config()
) {
        data class Response(
                @SerializedName("playGetTagsItem")
                val playGetTagsItem: ProductTagging = ProductTagging()
        )
}

data class ProductTag(
        @SerializedName("is_show_product_tagging")
        var isShowProductTagging: Boolean = false,
        @SerializedName("products")
        val listOfProducts: List<Product> = emptyList()
)

data class MerchantVoucher(
        @SerializedName("vouchers")
        val listOfVouchers: List<Voucher> = emptyList()
)

data class Config(
        @SerializedName("peek_product_count")
        val peekProductCount: Int = 0
)