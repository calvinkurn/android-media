package com.tokopedia.review.feature.inbox.history.presentation.util

interface ReviewHistoryItemListener {
    fun trackAttachedImageClicked(productId: String?, feedbackId: String?)
}