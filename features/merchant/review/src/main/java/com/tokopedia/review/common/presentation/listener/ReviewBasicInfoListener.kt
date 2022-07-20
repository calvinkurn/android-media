package com.tokopedia.review.common.presentation.listener

interface ReviewBasicInfoListener {

    fun onUserNameClicked(userId: String)

    fun trackOnUserInfoClicked(feedbackId: String, userId: String, statistics: String)
}