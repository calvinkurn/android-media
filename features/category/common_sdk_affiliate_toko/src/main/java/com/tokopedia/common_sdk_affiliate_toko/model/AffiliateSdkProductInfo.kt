package com.tokopedia.common_sdk_affiliate_toko.model

/**
 * Encapsulates Product Information
 *
 * @param[categoryID] categoryId of Product
 * @param[isVariant] isVariant of Product
 * @param[stockQty] stockQty of Product
 * @param[productId] productId of Product
 */
data class AffiliateSdkProductInfo(
    val categoryID: String = "",
    val isVariant: Boolean = false,
    val stockQty: Int = 0,
    val productId: String = ""
)