package com.tokopedia.product.detail.data.util

import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ThematicCampaign
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.view.util.toDate

/**
 * Created by Yehezkiel on 2020-02-26
 */
object VariantMapper {

    fun putChatProductInfoTo(
        intent: Intent?,
        productId: String?
    ) {
        if (intent == null || productId == null) return
        val productIds = listOf(productId)
        val stringProductPreviews = CommonUtil.toJson(productIds)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

    fun updateDynamicProductInfo(oldData: DynamicProductInfoP1?, newData: VariantChild?): DynamicProductInfoP1? {
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
            url = newData?.url ?: ""
        )

        val newCampaign = oldData.data.campaign.copy(
            campaignID = newData?.campaign?.campaignID ?: "",
            campaignType = newData?.campaign?.campaignType.toString(),
            campaignTypeName = newData?.campaign?.campaignTypeName ?: "",
            isActive = newData?.campaign?.isActive ?: false,
            originalPrice = newData?.campaign?.originalPrice ?: 0.0,
            discountedPrice = newData?.campaign?.discountedPrice ?: 0.0,
            startDate = newData?.campaign?.startDate ?: "",
            endDate = newData?.campaign?.endDateUnix toDate "yyyy-MM-dd HH:mm:ss",
            endDateUnix = newData?.campaign?.endDateUnix ?: "",
            stock = newData?.campaign?.stock ?: 0,
            isAppsOnly = newData?.campaign?.isAppsOnly ?: false,
            appLinks = newData?.campaign?.applinks ?: "",
            percentageAmount = newData?.campaign?.discountedPercentage.orZero(),
            stockSoldPercentage = newData?.campaign?.stockSoldPercentage.toIntSafely(),
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

        val newPrice = oldData.data.price.copy(
            value = newData?.price ?: 0.0
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
            stock = newStock,
            isCod = newData?.isCod ?: false
        )

        return DynamicProductInfoP1(
            basic = basic,
            data = data,
            bestSellerContent = oldData.bestSellerContent,
            layoutName = oldData.layoutName,
            pdpSession = oldData.pdpSession
        )
    }

    fun updateProductInfoByThumbVariant(oldData: DynamicProductInfoP1?, newData: VariantChild?): DynamicProductInfoP1? {
        val basic = oldData?.basic?.copy(
            productID = newData?.productId.orEmpty(),
            sku = newData?.sku.orEmpty(),
            minOrder = newData?.getFinalMinOrder().orZero(),
            status = if (newData?.isBuyable == true) {
                ProductStatusTypeDef.ACTIVE
            } else {
                ProductStatusTypeDef.WAREHOUSE
            },
            url = newData?.url.orEmpty()
        ) ?: return oldData

        return oldData.copy(
            basic = basic
        )
    }

    fun generateVariantString(variantData: ProductVariant?): String {
        return try {
            variantData?.variants?.map { it.name }?.joinToString(separator = ", ") ?: ""
        } catch (e: Throwable) {
            ""
        }
    }
}
