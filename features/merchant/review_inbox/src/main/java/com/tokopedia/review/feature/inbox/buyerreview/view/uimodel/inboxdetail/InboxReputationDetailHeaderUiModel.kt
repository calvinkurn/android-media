package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailHeaderUiModel (
    var avatarImage: String,
    var name: String,
    var deadline: String,
    var reputationDataUiModel: ReputationDataUiModel,
    var role: Int,
    val revieweeBadgeCustomerUiModel: RevieweeBadgeCustomerUiModel,
    val revieweeBadgeSellerUiModel: RevieweeBadgeSellerUiModel,
    val shopId: Long,
    val userId: Long
) : Visitable<InboxReputationDetailTypeFactory> {

    override fun type(typeFactory: InboxReputationDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

}