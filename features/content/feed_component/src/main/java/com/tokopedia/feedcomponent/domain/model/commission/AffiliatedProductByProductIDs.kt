package com.tokopedia.feedcomponent.domain.model.commission

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class AffiliatedProductByProductIDs(

        @SerializedName("totalProductCommission")
        val totalProductCommission: String = "0",

        @SerializedName("products")
        val products: List<AffiliatedProduct> = emptyList()
)