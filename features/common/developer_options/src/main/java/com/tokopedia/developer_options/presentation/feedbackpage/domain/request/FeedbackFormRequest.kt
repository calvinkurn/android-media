package com.tokopedia.developer_options.presentation.feedbackpage.domain.request

data class FeedbackFormRequest(
        var platformID: Int?,
        var email: String?,
        var appVersion: String?,
        var bundleVersion: String?,
        var device: String?,
        var os: String?,
        var tokopediaUserID: String?,
        var tokopediaEmail: String?,
        var sessionToken: String?,
        var fcmToken: String?,
        var loginState: String?,
        var lastAccessedPage: String?,
        var category: Int?,
        var journey: String?,
        var expected: String?,
        var labelsId: ArrayList<Int>?,
        var type: Int?,
        var detail: String?
)