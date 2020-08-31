package com.tokopedia.shop.common.data.source.cloud.model

data class ShopFreeShippingStatus(
    val active: Boolean,
    val status: Boolean,
    val statusEligible: Boolean
) {

    fun isEligible(): Boolean {
        return status || statusEligible
    }
}