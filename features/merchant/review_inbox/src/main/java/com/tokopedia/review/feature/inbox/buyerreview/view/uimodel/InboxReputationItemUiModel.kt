package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel

/**
 * @author by nisie on 8/15/17.
 */
data class InboxReputationItemUiModel(
    val reputationId: String = "",
    val revieweeName: String = "",
    val createTime: String = "",
    val revieweePicture: String = "",
    val reputationDaysLeft: String = "",
    val invoice: String = "",
    val reputationDataUiModel: ReputationDataUiModel = ReputationDataUiModel(),
    val role: String = "",
    val revieweeBadgeCustomerUiModel: RevieweeBadgeCustomerUiModel = RevieweeBadgeCustomerUiModel(),
    val revieweeBadgeSellerUiModel: RevieweeBadgeSellerUiModel = RevieweeBadgeSellerUiModel(),
    val shopId: String = "",
    val userId: String = ""
) : Visitable<InboxReputationTypeFactory> {

    companion object {
        const val ROLE_SELLER = "2"
        const val ROLE_BUYER = "1"
    }

    override fun type(typeFactory: InboxReputationTypeFactory): Int {
        return typeFactory.type(this)
    }
}