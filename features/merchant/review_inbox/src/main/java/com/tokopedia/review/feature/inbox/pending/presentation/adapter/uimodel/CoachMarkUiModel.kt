package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.review.feature.inbox.pending.presentation.coachmark.ReviewPendingCoachMarkItemManager

interface CoachMarkUiModel {
    fun getCoachMarkItemManager(): ReviewPendingCoachMarkItemManager?
}