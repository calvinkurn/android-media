package com.tokopedia.developer_options.api

import android.net.Uri

data class ImageRequest(
        var issueKey: String,
        var imageUri: Uri
)