package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
data class ProductTagging(
        @SerializedName("products")
        val listOfProducts: List<Product> = emptyList(),
        @SerializedName("vouchers")
        val listOfVouchers: List<Voucher> = emptyList()
)