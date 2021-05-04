package com.tokopedia.product.detail.data.util

import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.*
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.view.util.toDate

/**
 * Created by Yehezkiel on 2020-02-26
 */
object VariantMapper {

    fun putChatProductInfoTo(
            intent: Intent?,
            productId: String?,
            productInfo: DynamicProductInfoP1?,
            variantResp: ProductVariant?,
            freeOngkirImgUrl: String
    ) {
        if (intent == null || productId == null) return
        val variants = variantResp?.mapSelectedProductVariants(productId)
        val productImageUrl = productInfo?.data?.getProductImageUrl() ?: ""
        val productName = productInfo?.getProductName ?: ""
        val productPrice = productInfo?.finalPrice?.getCurrencyFormatted() ?: ""
        val priceBeforeInt = productInfo?.priceBeforeInt ?: 0
        val priceBefore = if (priceBeforeInt > 0) {
            priceBeforeInt.getCurrencyFormatted()
        } else {
            ""
        }
        val dropPercentage = productInfo?.dropPercentage ?: ""
        val productUrl = productInfo?.basic?.url ?: ""
        val isActive = productInfo?.basic?.isActive() ?: true
        val productFsIsActive = freeOngkirImgUrl.isNotEmpty()
        val productColorVariant = variants?.get("colour")?.get("value") ?: ""
        val productColorHexVariant = variants?.get("colour")?.get("hex") ?: ""
        val productSizeVariant = variants?.get("size")?.get("value") ?: ""
        val productColorVariantId = variants?.get("colour")?.get("id") ?: ""
        val productSizeVariantId = variants?.get("size")?.get("id") ?: ""
        val productPreview = ProductPreview(
                productId,
                productImageUrl,
                productName,
                productPrice,
                productColorVariantId,
                productColorVariant,
                productColorHexVariant,
                productSizeVariantId,
                productSizeVariant,
                productUrl,
                productFsIsActive,
                freeOngkirImgUrl,
                priceBefore,
                priceBeforeInt,
                dropPercentage,
                isActive
        )
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

    fun updateDynamicProductInfo(oldData: DynamicProductInfoP1?, newData: VariantChild?, existingListMedia: List<Media>?): DynamicProductInfoP1? {
        if (oldData == null) return null

        val basic = oldData.basic.copy(
                productID = newData?.productId ?: "",
                sku = newData?.sku ?: "",
                minOrder = newData?.getFinalMinOrder() ?: 0,
                status = if (newData?.isBuyable == true) {
                    ProductStatusTypeDef.ACTIVE
                } else {
                    ProductStatusTypeDef.WAREHOUSE
                },
                url = newData?.url ?: "")

        val newCampaign = oldData.data.campaign.copy(
                campaignID = newData?.campaign?.campaignID ?: "",
                campaignType = newData?.campaign?.campaignType.toString(),
                campaignTypeName = newData?.campaign?.campaignTypeName ?: "",
                isActive = newData?.campaign?.isActive ?: false,
                originalPrice = newData?.campaign?.originalPrice?.toInt() ?: 0,
                discountedPrice = newData?.campaign?.discountedPrice?.toInt() ?: 0,
                startDate = newData?.campaign?.startDate ?: "",
                endDate = newData?.campaign?.endDateUnix toDate "yyyy-MM-dd HH:mm:ss",
                endDateUnix = newData?.campaign?.endDateUnix ?: "",
                stock = newData?.campaign?.stock ?: 0,
                isAppsOnly = newData?.campaign?.isAppsOnly ?: false,
                appLinks = newData?.campaign?.applinks ?: "",
                percentageAmount = newData?.campaign?.discountedPercentage?.toInt() ?: 0,
                stockSoldPercentage = newData?.campaign?.stockSoldPercentage?.toInt() ?: 0,
                isCheckImei = newData?.campaign?.isCheckImei ?: false,
                isUsingOvo = newData?.campaign?.isUsingOvo ?: false,
                hideGimmick = newData?.campaign?.hideGimmick ?: false,
                background = newData?.campaign?.background ?: "",
                campaignIdentifier = newData?.campaign?.campaignIdentifier ?: 0
        )

        val newThematicCampaign = ThematicCampaign(
                campaignName = newData?.thematicCampaign?.campaignName ?: "",
                icon = newData?.thematicCampaign?.icon ?: "",
                background = newData?.thematicCampaign?.background ?: "",
                additionalInfo = newData?.thematicCampaign?.additionalInfo ?: ""
        )

        val newMedia = if (newData?.hasPicture == true) {
            val copyOfOldMedia = existingListMedia?.toMutableList()
            val newMedia = Media(type = "image", uRL300 = newData.picture?.original
                    ?: "", uRLOriginal = newData.picture?.original
                    ?: "", uRLThumbnail = newData.picture?.original ?: "").apply {
                id = (newData.productId + System.nanoTime())
            }

            copyOfOldMedia?.add(0, newMedia)

            copyOfOldMedia ?: mutableListOf()
        } else {
            oldData.data.media
        }

        val newPrice = oldData.data.price.copy(
                value = newData?.price?.toInt() ?: 0
        )

        val newStock = oldData.data.stock.copy(
                value = newData?.stock?.stock ?: 0,
                stockWording = newData?.stock?.stockWordingHTML ?: ""
        )

        val data = oldData.data.copy(
                campaign = newCampaign,
                thematicCampaign = newThematicCampaign,
                price = newPrice,
                name = newData?.name ?: "",
                media = newMedia,
                stock = newStock,
                isCod = newData?.isCod ?: false
        )

        return DynamicProductInfoP1(basic, data, oldData.layoutName)
    }

    fun updateMediaToCurrentP1Data(oldData: DynamicProductInfoP1?, media: MutableList<Media>): DynamicProductInfoP1 {
        val basic = oldData?.basic?.copy()
        val data = oldData?.data?.copy(
                media = media
        )
        return DynamicProductInfoP1(basic ?: BasicInfo(), data
                ?: ComponentData(), oldData?.layoutName ?: "")
    }

    fun generateVariantString(variantData: ProductVariant?): String {
        return try {
            variantData?.variants?.map { it.name }?.joinToString(separator = ", ") ?: ""
        } catch (e: Throwable) {
            ""
        }
    }
}
