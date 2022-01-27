package com.tokopedia.play.broadcaster.domain.model.campaign

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 25/01/22.
 */
data class GetCampaignProductResponse(
    @SerializedName("getCampaignProduct")
    val getCampaignProduct: GetCampaignProduct = GetCampaignProduct()
) {

    data class GetCampaignProduct(
        @SerializedName("Products")
        val products: List<Product> = listOf()
    )

    data class Product(
        @SerializedName("ID")
        val id: Long = 0
    )
}