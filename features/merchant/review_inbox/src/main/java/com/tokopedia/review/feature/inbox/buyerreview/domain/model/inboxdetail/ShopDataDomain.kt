package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class ShopDataDomain constructor(
    val shopId: Long = 0L, 
    val shopUserId: Long = 0L, 
    val domain: String = "", 
    val shopName: String = "",
    val shopUrl: String = "", 
    val logo: String = "", 
    val shopReputation: ShopReputationDomain = ShopReputationDomain()
)