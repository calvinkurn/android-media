package com.tokopedia.product.detail.common.data.model.pdplayout

import com.tokopedia.product.detail.common.data.model.constant.ProductCampaignStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef
import java.util.concurrent.TimeUnit

data class DynamicProductInfoP1(
        val basic: BasicInfo = BasicInfo(),
        val data: ComponentData = ComponentData(),
        val layoutName: String = "",
        val campaignStatus:String = ""
) {

    fun isUpcomingNplType(): Boolean = data.upcomingType.isNotEmpty() && data.upcomingType.equals(ProductUpcomingTypeDef.UPCOMING_NPL, true)
            && campaignStatus.equals(ProductCampaignStatusTypeDef.UPCOMING, true)

    fun isProductActive(nearestWarehouseStock: Int): Boolean = nearestWarehouseStock > 0 && basic.isActive()

    val isPreOrder: Boolean
        get() = data.preOrder.isActive

    val isUsingOvo: Boolean
        get() = data.campaign.isUsingOvo

    val isLeasing: Boolean
        get() = basic.isLeasing

    val shopTypeString: String
        get() {
            return if (data.isOS)
                "official_store"
            else if (data.isPowerMerchant)
                "gold_merchant"
            else
                "reguler"
        }

    val parentProductId: String
        get() =
            if (data.variant.isVariant && data.variant.parentID.isNotEmpty() && data.variant.parentID.toInt() > 0) {
                data.variant.parentID
            } else {
                basic.productID
            }

    val shouldShowCod: Boolean
        get() = (!data.campaign.activeAndHasId) && data.isCOD

    val getProductName: String
        get() = data.name

    val finalPrice: Int
        get() {
            return if (data.campaign.isActive) {
                data.campaign.discountedPrice
            } else {
                data.price.value
            }
        }

    val priceBeforeInt: Int
        get() {
            return if (data.campaign.isActive) {
                data.campaign.originalPrice
            } else {
                0
            }
        }

    val dropPercentage: String?
        get() {
            return if (data.campaign.isActive) {
                data.campaign.percentageAmount.toString()
            } else {
                ""
            }
        }

    fun checkImei(imeiRemoteConfig: Boolean): Boolean {
        return imeiRemoteConfig && data.campaign.isCheckImei
    }

    fun getFinalStock(multiOriginStock: String): String {
        return if (multiOriginStock.isEmpty()) {
            if (data.campaign.isActive) {
                data.campaign.stock.toString()
            } else {
                data.stock.value.toString()
            }
        } else {
            multiOriginStock
        }
    }

    fun shouldShowNotifyMe(): Boolean {
        return try {
            val now = System.currentTimeMillis()
            val startTime = (data.startDate.toLongOrNull() ?: 0) * 1000L
            val dayLeft = TimeUnit.MICROSECONDS.toDays(now - startTime)
            !(data.campaignId.isEmpty() || dayLeft > 3)
        } catch (ex: Exception) {
            false
        }
    }
}