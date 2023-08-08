package com.tokopedia.product.detail.common.data.model.pdplayout

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.Cashback
import com.tokopedia.product.detail.common.data.model.product.PostAtcLayout
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.common.data.model.product.Stock
import com.tokopedia.product.detail.common.data.model.product.VariantBasic
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.utils.extensions.validDimensionRatio

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
    @SerializedName("recommendation")
    val productMediaRecomBasicInfo: ProductMediaRecomBasicInfo = ProductMediaRecomBasicInfo(),
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
    @SerializedName("postATCLayout")
    val postAtcLayout: PostAtcLayout = PostAtcLayout(),
    @SerializedName("defaultChild")
    val defaultChild: String = "",
    @SerializedName("variants")
    val variants: List<Variant> = listOf(),
    @SerializedName("children")
    val children: List<VariantChild> = listOf(),
    /**
     * used when landing on pdp, if it is empty use hardcode FE
     * and if there’s a user activity for choosing the variant, use children.subText below
     * Details: https://tokopedia.atlassian.net/wiki/spaces/PDP/pages/2245002923/PDP+P1+Product+Variant+Partial+OOS
     */
    @SerializedName("landingSubText")
    val landingSubText: String = "",
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
    @SerializedName("eduLink")
    val eduLink: EduLinkData = EduLinkData(),
    //endregioncopy

    //region category carousel
    @SerializedName("titleCarousel")
    val titleCarousel: String = "",
    @SerializedName("list")
    val categoryCarouselList: List<CategoryCarousel> = listOf(),

    // region product detail info since improve to pdp catalog
    @SerializedName("catalogBottomsheet")
    val catalogBottomSheet: ProductDetailInfoSeeMoreData? = null,
    @SerializedName("bottomsheet")
    val bottomSheet: ProductDetailInfoSeeMoreData = ProductDetailInfoSeeMoreData(),
    // endregion

    // region Additional Value for Global Bundling Component Data
    @SerializedName("widgetType")
    val widgetType: Int = -1,
    @SerializedName("whID")
    val whId: String = "",
    // endregion

    // region CustomInfoTitle
    @SerializedName("status")
    val status: String = "",
    @SerializedName("componentName")
    val componentName: String = "",
    // endregion

    // region Variant Thumb
    // componentType value is thumbnail or chips
    @SerializedName("componentType")
    val componentType: String = "",
    // endregion

    @SerializedName("variantCampaign")
    val variantCampaign: VariantCampaign = VariantCampaign(),

    // region a plus content data
    @SerializedName("contentMedia")
    val contentMedia: List<ContentMedia> = listOf(),
    @SerializedName("show")
    val show: Boolean = false,
    @SerializedName("ctaText")
    val ctaText: String = ""
    // endregion
) {
    companion object {
        private const val PRODUCT_IMAGE_TYPE = "image"
    }

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

    fun getGalleryItems(): List<ProductDetailGallery.Item> {
        return media.mapIndexed { index, media ->
            val url: String
            val thumbnailUrl: String
            val type: ProductDetailGallery.Item.Type
            if (media.type == PRODUCT_IMAGE_TYPE) {
                url = media.urlHD
                thumbnailUrl = media.uRLThumbnail
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

    fun requiredForContentMediaToggle() = ctaText.isNotBlank()
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

data class VariantCampaign(
    @SerializedName("campaigns")
    val campaigns: List<com.tokopedia.product.detail.common.data.model.variant.VariantCampaign> = emptyList(),
    @SerializedName("thematicCampaigns")
    val thematicCampaigns: List<ThematicCampaign> = emptyList()
)

data class ContentMedia(
    @SerializedName("url")
    val url: String,
    @SerializedName("ratio")
    val ratio: String
) {
    fun valid() = url.isNotBlank() && ratio.validDimensionRatio()
}
