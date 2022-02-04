package com.tokopedia.media.picker.data.entity

import android.net.Uri

data class Album(
    var id: Long = 0L,
    var name: String = "",
    var preview: Uri? = null,
    var count: Int = 0
)