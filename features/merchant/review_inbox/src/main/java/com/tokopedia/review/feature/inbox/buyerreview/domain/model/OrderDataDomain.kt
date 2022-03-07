package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/15/17.
 */
data class OrderDataDomain(
    val invoiceRefNum: String,
    val createTimeFmt: String,
    val invoiceUrl: String
)