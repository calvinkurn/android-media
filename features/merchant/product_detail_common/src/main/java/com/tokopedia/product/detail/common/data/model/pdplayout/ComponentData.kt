package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.*

data class ComponentData(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("content")
        val content: List<Content> = listOf(),
        @SerializedName("row")
        val row: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("isApplink")
        val isApplink: Boolean = true,
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("separator")
        val separator: String = "",
        @SerializedName("description")
        val description: String = "",

        // Snapshot data
        @SerializedName("campaign")
        val campaign: CampaignModular = CampaignModular(),
        @SerializedName("isCOD")
        val isCOD: Boolean = false,
        @SerializedName("isCashback")
        val isCashback: Cashback = Cashback(),
        @SerializedName("isFreeOngkir")
        val isFreeOngkir: IsFreeOngkir = IsFreeOngkir(),
        @SerializedName("isOS")
        val isOS: Boolean = false,
        @SerializedName("isPowerMerchant")
        val isPowerMerchant: Boolean = false,
        @SerializedName("isTradeIn")
        val isTradeIn: Boolean = false,
        @SerializedName("isWishlist")
        val isWishlist: Boolean = false,
        @SerializedName("media")
        val media: List<Media> = listOf(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("price")
        val price: Price = Price(),
        @SerializedName("stock")
        val stock: Stock = Stock(),
        @SerializedName("variant")
        val variant: Variant = Variant(),
        @SerializedName("videos")
        val videos: List<Video> = listOf(),
        @SerializedName("wholesale")
        val wholesale: List<Wholesale>? = null,
        @SerializedName("preorder")
        val preOrder: PreOrder = PreOrder(),

        //upcoming deals / notifyMe
        @SerializedName("campaignID")
        val campaignId: String = "",
        @SerializedName("campaignType")
        val campaignType: String = "",
        @SerializedName("campaignTypeName")
        val campaignTypeName: String = "",
        @SerializedName("endDate")
        val endDate: String = "",
        @SerializedName("startDate")
        val startDate: String = "",
        @SerializedName("notifyMe")
        val notifyMe: Boolean = false
) {

    val hasWholesale: Boolean
        get() = wholesale != null && wholesale.isNotEmpty()

    fun getFirstProductImage(): String? {
        if (media.isEmpty()) return null

        val firstImage = media.find {
            it.type == "image"
        }

        return if (firstImage != null) {
            when {
                firstImage.uRLThumbnail.isNotEmpty() -> firstImage.uRLThumbnail
                firstImage.uRL300.isNotEmpty() -> firstImage.uRL300 ?: ""
                firstImage.uRLOriginal.isNotEmpty() -> firstImage.uRLOriginal
                else -> ""
            }
        } else {
            ""
        }
    }

    fun getProductImageUrl(): String? {
        if (media.isEmpty()) return null
        return media.find {
            it.type == "image"
        }?.uRLThumbnail
    }

    fun getFsProductIsActive(): Boolean {
        return isFreeOngkir.isActive
    }

    fun getFsProductImageUrl(): String {
        return isFreeOngkir.imageURL
    }

    fun getImagePath(): ArrayList<String> {
        return ArrayList(media.map {
            if (it.type == "image") {
                it.uRLOriginal
            } else {
                it.uRLThumbnail
            }
        })
    }
}