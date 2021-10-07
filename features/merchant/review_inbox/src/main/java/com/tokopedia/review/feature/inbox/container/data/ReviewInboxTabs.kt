package com.tokopedia.review.feature.inbox.container.data

sealed class ReviewInboxTabs {
    data class ReviewInboxPending(val counter: String = "") : ReviewInboxTabs()
    object ReviewInboxHistory : ReviewInboxTabs()
    object ReviewInboxSeller : ReviewInboxTabs()
}