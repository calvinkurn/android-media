package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.*

data class ComponentData(
        //region General data
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
        //endregion

        //region Content data
        @SerializedName("campaign")
        val campaign: CampaignModular = CampaignModular(),
        @SerializedName("thematicCampaign")
        val thematicCampaign: ThematicCampaign = ThematicCampaign(),
        @SerializedName("isCashback")
        val isCashback: Cashback = Cashback(),
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
        val youtubeVideos: List<YoutubeVideo> = listOf(),
        @SerializedName("wholesale")
        val wholesale: List<Wholesale>? = null,
        @SerializedName("preorder")
        val preOrder: PreOrder = PreOrder(),
        @SerializedName("isCOD")
        val isCod: Boolean = false,
        //endregion
        //region Variant data
        @SerializedName("parentID")
        val parentId: String = "",
        @SerializedName("errorCode")
        val errorCode: Int = 0,
        @SerializedName("sizeChart")
        val sizeChart: String = "",
        @SerializedName("defaultChild")
        val defaultChild: String = "",
        @SerializedName("variants")
        val variants: List<ProductP1Variant> = listOf(),
        @SerializedName("children")
        val children : List<ProductP1VariantChild> = listOf()
        //endregioncopy
)  {
    companion object{
        private const val PRODUCT_IMAGE_TYPE = "image"
    }

    val hasWholesale: Boolean
        get() = wholesale != null && wholesale.isNotEmpty()

    fun getFirstProductImage(): String? {
        if (media.isEmpty()) return null

        val firstImage = media.find {
            it.type == PRODUCT_IMAGE_TYPE
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
            it.type == PRODUCT_IMAGE_TYPE
        }?.uRLThumbnail
    }

    fun getImagePathExceptVideo(): ArrayList<String>? {
        val imageData = media.filter { it.type == PRODUCT_IMAGE_TYPE && it.uRLOriginal.isNotEmpty() }.map { it.uRLOriginal }
        val arrayList = arrayListOf<String>()
        return if (imageData.isEmpty()) {
            null
        } else {
            arrayList.addAll(imageData)
            arrayList
        }
    }

    fun getImagePath(): ArrayList<String> {
        return ArrayList(media.map {
            if (it.type == PRODUCT_IMAGE_TYPE) {
                it.uRLOriginal
            } else {
                it.uRLThumbnail
            }
        })
    }
}