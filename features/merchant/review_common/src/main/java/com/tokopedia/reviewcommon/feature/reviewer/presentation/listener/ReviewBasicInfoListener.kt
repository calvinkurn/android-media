package com.tokopedia.reviewcommon.feature.reviewer.presentation.listener

interface ReviewBasicInfoListener {

    fun onUserNameClicked(userId: String)

    fun trackOnUserInfoClicked(feedbackId: String, userId: String, statistics: String)
}