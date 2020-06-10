package com.tokopedia.review.feature.inbox.container.data

sealed class ReviewInboxTabs {
    data class ReviewInboxPending(val counter: String = "") : ReviewInboxTabs()
    data class ReviewInboxHistory(val counter: String = "") : ReviewInboxTabs()
    object ReviewInboxSeller : ReviewInboxTabs()
}