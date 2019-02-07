package com.tokopedia.transactionanalytics.data.expresscheckout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 07/02/19.
 */

data class Checkout(
        @SerializedName("actionField")
        var actionField: ActionField? = null,

        @SerializedName("products")
        var products: ArrayList<Product>? = null
)