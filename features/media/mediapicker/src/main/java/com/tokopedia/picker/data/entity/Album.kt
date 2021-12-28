package com.tokopedia.picker.data.entity

import android.net.Uri

data class Album(
    var name: String,
    var preview: Uri? = null,
    var count: Int = 0
) {
    var medias: MutableList<Media> = mutableListOf()
}