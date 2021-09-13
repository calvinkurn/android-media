package com.tokopedia.review.feature.inbox.buyerreview.domain.model

/**
 * @author by nisie on 8/15/17.
 */
class RevieweeDataDomain constructor(
    val revieweeName: String,
    val revieweeUri: String,
    val revieweeRole: String,
    val revieweeRoleId: Int,
    val revieweePicture: String?,
    val revieweeBadgeCustomer: RevieweeBadgeCustomerDomain,
    val revieweeBadgeSeller: RevieweeBadgeSellerDomain
)