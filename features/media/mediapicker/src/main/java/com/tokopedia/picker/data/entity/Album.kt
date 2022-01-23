package com.tokopedia.picker.data.entity

import android.net.Uri

data class Album(
    var id: Long,
    var name: String,
    var preview: Uri?,
    var count: Int
)