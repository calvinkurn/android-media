package com.tokopedia.developer_options.api.request

import okhttp3.MultipartBody

data class ImageRequest(
        var file: String,
        var value: MultipartBody.Part
)