package com.tokopedia.review.feature.inbox.history.presentation.util

interface ReviewHistoryItemListener {
    fun trackAttachedImageClicked(productId: Long?, feedbackId: Long?)
}