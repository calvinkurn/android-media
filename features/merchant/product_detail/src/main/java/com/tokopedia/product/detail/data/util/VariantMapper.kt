package com.tokopedia.product.detail.data.util

import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.PromoPriceResponse
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

    fun updateDynamicProductInfo(oldData: ProductInfoP1?, newData: VariantChild?): ProductInfoP1? {
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

        val newPrice = oldData.data.price.updatePrice(variantChild = newData)
        val newCampaign = oldData.variantCampignToCampaignModular(newData, newPrice)

        val newStock = oldData.data.stock.copy(
            value = newData?.stock?.stock ?: 0,
            stockWording = newData?.stock?.stockWordingHTML ?: ""
        )

        val data = oldData.data.copy(
            campaign = newCampaign,
            thematicCampaign = newData?.thematicCampaign ?: ThematicCampaign(),
            price = newPrice,
            name = newData?.name ?: "",
            stock = newStock,
            isCod = newData?.isCod ?: false,
            componentPriceType = newData?.componentPriceType ?: 0,
            promoPrice = newData?.promoPrice ?: PromoPriceResponse(),
            labelIcons = newData?.labelIcons.orEmpty()
        )

        return ProductInfoP1(
            basic = basic,
            data = data,
            bestSellerContent = oldData.bestSellerContent,
            layoutName = oldData.layoutName,
            pdpSession = oldData.pdpSession
        )
    }

    private fun ProductInfoP1.variantCampignToCampaignModular(
        newData: VariantChild?,
        newPrice: Price
    ) = data.campaign.copy(
        campaignID = newData?.campaign?.campaignID.orEmpty(),
        campaignType = newData?.campaign?.campaignType.toString(),
        campaignTypeName = newData?.campaign?.campaignTypeName.orEmpty(),
        isActive = newData?.campaign?.isActive ?: false,
        originalPrice = newData?.campaign?.originalPrice.orZero(),
        discountedPrice = newData?.campaign?.discountedPrice.orZero(),
        startDate = newData?.campaign?.startDate.orEmpty(),
        endDate = newData?.campaign?.endDateUnix toDate "yyyy-MM-dd HH:mm:ss",
        endDateUnix = newData?.campaign?.endDateUnix.orEmpty(),
        stock = newData?.campaign?.stock ?: 0,
        isAppsOnly = newData?.campaign?.isAppsOnly.orFalse(),
        appLinks = newData?.campaign?.applinks.orEmpty(),
        percentageAmount = newData?.campaign?.discountedPercentage.orZero(),
        stockSoldPercentage = newData?.campaign?.stockSoldPercentage.toIntSafely(),
        isCheckImei = newData?.campaign?.isCheckImei.orFalse(),
        isUsingOvo = newData?.campaign?.isUsingOvo.orFalse(),
        hideGimmick = newData?.campaign?.hideGimmick.orFalse(),
        background = newData?.campaign?.background.orEmpty(),
        campaignIdentifier = newData?.campaign?.campaignIdentifier.orZero(),
        campaignLogo = newData?.campaign?.campaignLogo.orEmpty()
    ).apply {
        processMaskingPrice(price = newPrice)
    }

    fun generateVariantString(variantData: ProductVariant?): String {
        return try {
            variantData?.variants?.map { it.name }?.joinToString(separator = ", ") ?: ""
        } catch (e: Throwable) {
            ""
        }
    }
}
