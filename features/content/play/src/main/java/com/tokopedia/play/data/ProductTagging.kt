package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
data class ProductTagging(
        @SerializedName("title")
        val title: String = "Ayo belanja barang pilihan kami sebelum kehabisan!",  // TODO("for testing")
        @SerializedName("is_show_product_tagging")
        val isShowProductTagging: Boolean = true, // TODO("for testing")
        @SerializedName("is_show_discount")
        val isShowDiscount: Boolean = true  // TODO("for testing")
)

data class ProductTaggingItems(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("product")
        val listOfProducts: List<Product> = emptyList(),
        @SerializedName("voucher")
        val listOfVouchers: List<Voucher> = emptyList()
)