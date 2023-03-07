package com.tokopedia.common_sdk_affiliate_toko.model

/**
 * Encapsulates Product Information
 *
 * @param[categoryID] categoryId of Product
 * @param[isVariant] isVariant of Product
 * @param[stockQty] stockQty of Product
 */
data class AffiliateSdkProductInfo(
    val categoryID: String,
    val isVariant: Boolean,
    val stockQty: Int,
)