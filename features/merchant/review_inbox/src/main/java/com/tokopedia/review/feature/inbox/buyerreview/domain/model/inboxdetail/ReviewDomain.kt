package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/19/17.
 */
data class ReviewDomain(
    val data: List<ReviewItemDomain> = listOf(),
    val reputationId: String = "",
    val userData: UserDataDomain = UserDataDomain(),
    val shopData: ShopDataDomain = ShopDataDomain(),
    val invoiceRefNum: String = "",
    val invoiceTime: String = "",
    val orderId: String = ""
)