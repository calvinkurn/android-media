package com.tokopedia.product.detail.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductInfo(
        @SerializedName("basic")
        @Expose
        val basic: Basic = Basic(),

        @SerializedName("brand")
        @Expose
        val brand: Brand = Brand(),

        @SerializedName("campaign")
        @Expose
        val campaign: Campaign = Campaign(),

        @SerializedName("cashback")
        @Expose
        val cashback: Cashback = Cashback(),

        @SerializedName("category")
        @Expose
        val category: Category = Category(),

        @SerializedName("menu")
        @Expose
        val menu: Menu = Menu(),

        @SerializedName("pictures")
        @Expose
        val pictures: List<Picture>? = listOf(),

        @SerializedName("preorder")
        @Expose
        val preorder: PreOrder = PreOrder(),

        @SerializedName("stats")
        @Expose
        val stats: Stats = Stats(),

        @SerializedName("txStats")
        @Expose
        val txStats: TxStats = TxStats(),

        @SerializedName("videos")
        @Expose
        val videos: List<Video> = listOf(),

        @SerializedName("wholesale")
        @Expose
        val wholesale: List<Wholesale>? = listOf(),

        @SerializedName("variant")
        @Expose
        val variant: Variant = Variant(),

        @SerializedName("stock")
        @Expose
        val stock: Stock = Stock()
) {
    data class Response(
            @SerializedName("getPDPInfo")
            @Expose
            val data: ProductInfo? = null
    )

    data class WishlistStatus(
            @SerializedName("ProductWishlistQuery")
            @Expose
            var isWishlisted: Boolean? = null
    )

    val parentProductId: String
        get() =
            if (variant.isVariant && variant.parentID.isNotEmpty() && variant.parentID.toInt() > 0) {
                variant.parentID
            } else {
                basic.id.toString()
            }

    val shouldShowCod: Boolean
        get() = (!campaign.activeAndHasId) && basic.isEligibleCod

    val hasActiveCampaign: Boolean
        get() = campaign.activeAndHasId

    val isPreorderActive: Boolean
        get() = with(preorder) { isActive && duration > 0 }

    val hasWholesale: Boolean
        get() = wholesale!= null && wholesale.size > 0

    val firstThumbnailPicture: String
        get() {
            if (pictures!= null && pictures.size > 0) {
                if (pictures[0].urlThumbnail.isNotEmpty()){
                    return pictures[0].urlThumbnail
                } else if (pictures[0].url300.isNotEmpty()){
                    return pictures[0].url300
                } else if (pictures[0].urlOriginal.isNotEmpty()){
                    return pictures[0].urlOriginal
                } else {
                    return ""
                }
            } else {
                return ""
            }
        }

}