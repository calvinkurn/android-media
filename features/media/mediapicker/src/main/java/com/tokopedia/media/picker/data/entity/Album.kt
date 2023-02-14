package com.tokopedia.media.picker.data.entity

import android.net.Uri

data class Album(
    var id: Long = 0L,
    var name: String = "",
    var uri: Uri? = null,
    var count: Int = 1
)
