package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel

/**
 * @author by nisie on 8/15/17.
 */
class InboxReputationItemUiModel constructor(
    val reputationId: String, val revieweeName: String?, val createTime: String?,
    val revieweePicture: String?, val reputationDaysLeft: String,
    val invoice: String?,
    val reputationDataUiModel: ReputationDataUiModel,
    val role: Int,
    val revieweeBadgeCustomerUiModel: RevieweeBadgeCustomerUiModel,
    val revieweeBadgeSellerUiModel: RevieweeBadgeSellerUiModel,
    val shopId: Long, val userId: Long
) : Visitable<InboxReputationTypeFactory?> {
    public override fun type(typeFactory: InboxReputationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val ROLE_SELLER: Int = 2
        val ROLE_BUYER: Int = 1
    }
}