package com.tokopedia.shop.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignValidatedProductListResponse(
    @SerializedName("getSellerCampaignValidatedProductList")
    val response: GetSellerCampaignValidatedProductList
) {
    data class PictureUrl (
        @SerializedName("url_thumbnail")
        val urlThumbnail: String
    )

    data class TrxStats (
        @SerializedName("sold")
        val sold: Long
    )

    data class Product (
        @SerializedName("product_id")
        val productId: String,
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("price")
        val price: Double,
        @SerializedName("formatted_price")
        val formattedPrice: String,
        @SerializedName("product_url")
        val productUrl: String,
        @SerializedName("sku")
        val sku: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("pictures")
        val pictures: List<PictureUrl>,
        @SerializedName("disabled")
        val disabled: Boolean,
        @SerializedName("disabled_reason")
        val disabledReason: String,
        @SerializedName("transaction_statistics")
        val transactionStatistics: TrxStats,
        @SerializedName("stock")
        val stock: Long,
        @SerializedName("variant_childs_ids")
        val variantChildsIds: List<String>
    )

    data class GetSellerCampaignValidatedProductList (
        @SerializedName("products")
        val products: List<Product>
    )

}