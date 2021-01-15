package com.tokopedia.feedback_form.feedbackpage.domain.request

import okhttp3.MultipartBody

data class ImageRequest(
        var file: String,
        var value: MultipartBody.Part
)