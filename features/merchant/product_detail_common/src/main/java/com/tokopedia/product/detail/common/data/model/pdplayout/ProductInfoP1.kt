package com.tokopedia.product.detail.common.data.model.pdplayout

import com.tokopedia.analytics.byteio.ProductType
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.purchase_platform.common.utils.isBlankOrZero
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero

data class ProductInfoP1(
    val basic: BasicInfo = BasicInfo(),
    val data: ComponentData = ComponentData(),
    val bestSellerContent: Map<String, OneLinersContent>? = mapOf(),
    val stockAssuranceContent: Map<String, OneLinersContent>? = mapOf(),
    val layoutName: String = "",
    val pdpSession: String = "",
    val requestId: String = "",
    val isCampaign: Boolean = false,
    val cacheState: CacheState = CacheState(),
    val hasInfiniteRecommendation: Boolean = false,
    val infiniteRecommendationPageName: String = "",
    val infiniteRecommendationQueryParam: String = ""
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

    val originalPrice: Double
        get() {
            return if (data.campaign.isActive) {
                data.campaign.originalPrice
            } else {
                data.price.value
            }
        }

    val originalPriceFmt: String
        get() {
            return data.campaign.slashPriceFmt.ifBlank { data.price.priceFmt }
        }

    val isFromCache
        get() = cacheState.isFromCache

    val productType: ProductType
        get() {
            return when {
                basic.isActive() && getFinalStock().isNotBlankOrZero() -> ProductType.AVAILABLE
                basic.isActive() && getFinalStock().isBlankOrZero() -> ProductType.SOLD_OUT
                else -> ProductType.NOT_AVAILABLE
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

data class CacheState(
    val remoteCacheableActive: Boolean = false,
    // data source state, true = from cache, false = from cloud
    val isFromCache: Boolean = false,

    // caching flow pdp, true = cache first then cloud, false = cache only or cloud only
    val cacheFirstThenCloud: Boolean = false,

    // flag is the data is prefetch
    val isPrefetch: Boolean = false
)
