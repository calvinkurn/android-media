package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/19/17.
 */
class ReviewDomain constructor(
    data: List<ReviewItemDomain?>?,
    reputationId: Long, userData: UserDataDomain,
    shopData: ShopDataDomain, invoiceRefNum: String?,
    invoiceTime: String?, orderId: String?
) {
    val data: List<ReviewItemDomain?>? = null
    val reputationId: Long
    val userData: UserDataDomain
    val shopData: ShopDataDomain
    val invoiceRefNum: String?
    val invoiceTime: String?
    val orderId: String?

    init {
        this.data = data
        this.reputationId = reputationId
        this.userData = userData
        this.shopData = shopData
        this.invoiceRefNum = invoiceRefNum
        this.invoiceTime = invoiceTime
        this.orderId = orderId
    }
}