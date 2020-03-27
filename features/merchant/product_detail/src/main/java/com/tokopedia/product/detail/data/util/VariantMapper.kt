package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.variant_common.model.VariantChildCommon

/**
 * Created by Yehezkiel on 2020-02-26
 */
object VariantMapper {

    fun updateDynamicProductInfo(oldData: DynamicProductInfoP1?, newData: VariantChildCommon?, existingListMedia: List<Media>?): DynamicProductInfoP1? {
        if (oldData == null) return null

        val basic = oldData.basic.copy(
                productID = newData?.productId.toString(),
                sku = newData?.sku ?: "",
                minOrder = newData?.stock?.minimumOrder ?: 0,
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
                endDate = newData?.campaign?.endDate ?: "",
                stock = newData?.campaign?.stock ?: 0,
                isAppsOnly = newData?.campaign?.isAppsOnly ?: false,
                appLinks = newData?.campaign?.applinks ?: "",
                percentageAmount = newData?.campaign?.discountedPercentage?.toInt() ?: 0
        )

        val newMedia = if (newData?.hasPicture == true) {
            val copyOfOldMedia = existingListMedia?.toMutableList()
            copyOfOldMedia?.add(0, Media(type = "image", uRL300 = newData.picture?.original
                    ?: "", uRLOriginal = newData.picture?.original
                    ?: "", uRLThumbnail = newData.picture?.original ?: ""))
            copyOfOldMedia ?: mutableListOf()
        } else {
            oldData.data.media
        }

        val newPrice = oldData.data.price.copy(
                value = newData?.price?.toInt() ?: 0
        )

        val data = oldData.data.copy(
                isCOD = newData?.isCod ?: false,
                isWishlist = newData?.isWishlist ?: false,
                campaign = newCampaign,
                price = newPrice,
                name= newData?.name ?: "",
                media = newMedia)

        return DynamicProductInfoP1(basic, data, oldData.layoutName)
    }

    fun updateMediaToCurrentP1Data(oldData: DynamicProductInfoP1?, media: MutableList<Media>): DynamicProductInfoP1 {
        val basic = oldData?.basic?.copy()
        val data = oldData?.data?.copy(
                media = media
        )
        return DynamicProductInfoP1(basic ?: BasicInfo(),data ?: ComponentData(), oldData?.layoutName ?: "")
    }
}
