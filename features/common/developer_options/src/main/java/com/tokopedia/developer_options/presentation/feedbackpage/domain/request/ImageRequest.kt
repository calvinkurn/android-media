package com.tokopedia.developer_options.presentation.feedbackpage.domain.request

import okhttp3.MultipartBody

data class ImageRequest(
        var file: String,
        var value: MultipartBody.Part
)