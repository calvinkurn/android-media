package com.tokopedia.product.detail.common.data.model.pdplayout

data class DynamicProductInfoP1(
        val basic: BasicInfo = BasicInfo(),
        val data: ComponentData = ComponentData(),
        val layoutName: String = "",
        val pdpSession:String = ""
) {

    fun isProductVariant(): Boolean = data.variant.isVariant

    fun isProductActive(): Boolean = getFinalStock().toIntOrNull() ?: 0 > 0 && basic.isActive()

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

    fun getFinalStock(): String {
        return if (data.campaign.isActive) {
            data.campaign.stock.toString()
        } else {
            data.stock.value.toString()
        }
    }
}