package com.tokopedia.product.detail.common.data.model.pdplayout

import com.tokopedia.product.detail.common.ProductDetailCommonConstant


data class DynamicProductInfoP1(
    val basic: BasicInfo = BasicInfo(),
    val data: ComponentData = ComponentData(),
    val bestSellerContent: Map<String, OneLinersContent>? = mapOf(),
    val stockAssuranceContent: Map<String, OneLinersContent>? = mapOf(),
    val layoutName: String = "",
    val pdpSession: String = "",
    val requestId: String = ""
) {

    fun isProductVariant(): Boolean = data.variant.isVariant

    fun isProductActive(): Boolean = getFinalStock().toIntOrNull() ?: 0 > 0 && basic.isActive()

    val isUsingOvo: Boolean
        get() = data.campaign.isUsingOvo

    val shopTypeString: String
        get() {
            return when {
                basic.isTokoNow -> ProductDetailCommonConstant.VALUE_TOKONOW
                data.isOS -> ProductDetailCommonConstant.VALUE_OFFICIAL_STORE
                data.isPowerMerchant -> ProductDetailCommonConstant.VALUE_GOLD_MERCHANT
                else -> ProductDetailCommonConstant.VALUE_REGULER
            }
        }

    val parentProductId: String
        get() =
            if (data.variant.isVariant && data.variant.parentID.isNotEmpty() && data.variant.parentID.toLongOrNull() ?: 0L > 0L) {
                data.variant.parentID
            } else {
                basic.productID
            }

    val getProductName: String
        get() = data.name

    val finalPrice: Double
        get() {
            return if (data.campaign.isActive) {
                data.campaign.discountedPrice
            } else {
                data.price.value
            }
        }

    fun getFinalStock(): String {
        return if (data.campaign.isActive) {
            data.campaign.stock.toString()
        } else {
            data.stock.value.toString()
        }
    }
}
