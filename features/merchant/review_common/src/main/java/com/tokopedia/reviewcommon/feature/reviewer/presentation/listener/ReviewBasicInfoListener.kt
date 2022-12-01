package com.tokopedia.reviewcommon.feature.reviewer.presentation.listener

interface ReviewBasicInfoListener {

    fun onUserNameClicked(
        feedbackId: String,
        userId: String,
        statistics: String,
        label: String
    )
}