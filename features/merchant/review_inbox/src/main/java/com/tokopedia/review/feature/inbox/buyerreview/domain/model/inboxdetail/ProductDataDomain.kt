package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class ProductDataDomain constructor(
    val productId: Long,
    val productName: String,
    val productImageUrl: String,
    val productPageUrl: String,
    val shopId: Long,
    val productStatus: Int
)