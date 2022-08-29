package com.tokopedia.product.detail.common.data.model.pdplayout


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.Cashback
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.common.data.model.product.Stock
import com.tokopedia.product.detail.common.data.model.product.VariantBasic
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild

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

        //region custom info palugada ... on pdpDataCustomInfo
        @SerializedName("label")
        val labels: List<CustomInfoLabelData> = listOf(),
        @SerializedName("lightIcon")
        val lightIcon: String = "",
        @SerializedName("darkIcon")
        val darkIcon: String = "",
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
        @SerializedName("containerType")
        val containerType: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("parentName")
        val parentName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Price = Price(),
        @SerializedName("stock")
        val stock: Stock = Stock(),
        @SerializedName("variant")
        val variant: VariantBasic = VariantBasic(),
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
        @SerializedName("maxFinalPrice")
        val maxFinalPrice: Float = 0F,
        @SerializedName("defaultChild")
        val defaultChild: String = "",
        @SerializedName("variants")
        val variants: List<Variant> = listOf(),
        @SerializedName("children")
        val children: List<VariantChild> = listOf(),
        //endregioncopy

        //region one liners data
        @SerializedName("productID")
        val productId: String = "",
        @SerializedName("oneLinerContent")
        val oneLinerContent: String = "",
        @SerializedName("linkText")
        val linkText: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("isVisible")
        val isVisible: Boolean = true,
        //endregioncopy

        //region category carousel
        @SerializedName("titleCarousel")
        val titleCarousel: String = "",
        @SerializedName("list")
        val categoryCarouselList: List<CategoryCarousel> = listOf()
        //endregion
) {
    companion object {
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

    fun getGalleryItems(): List<ProductDetailGallery.Item> {
        return media.mapIndexed { index, media ->
            val url: String
            val thumbnailUrl: String
            val type: ProductDetailGallery.Item.Type
            if (media.type == PRODUCT_IMAGE_TYPE) {
                url = media.uRLOriginal
                thumbnailUrl = media.uRLOriginal
                type = ProductDetailGallery.Item.Type.Image
            } else {
                url = media.videoURLAndroid
                thumbnailUrl = media.uRLThumbnail
                type = ProductDetailGallery.Item.Type.Video
            }

            ProductDetailGallery.Item(
                id = index.toString(),
                url = url,
                thumbnailUrl = thumbnailUrl,
                tag = media.description.takeIf { media.variantOptionId != "0" },
                type = type
            )
        }
    }
}

data class CategoryCarousel(
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("isApplink")
        var isApplink: Boolean = false,
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("categoryID")
        val categoryId: String = ""
)