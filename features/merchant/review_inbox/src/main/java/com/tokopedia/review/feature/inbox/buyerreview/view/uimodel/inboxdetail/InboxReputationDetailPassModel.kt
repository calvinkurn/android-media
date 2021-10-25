package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel

/**
 * @author by nisie on 8/28/17.
 */
data class InboxReputationDetailPassModel(
    var reputationDataUiModel: ReputationDataUiModel,
    var reputationId: String,
    var revieweeName: String,
    var revieweeImage: String,
    var deadlineText: String,
    var invoice: String,
    var createTime: String,
    var role: Int
)