package com.tokopedia.picker.data.entity

data class Directory(
    var name: String
) {
    var medias: MutableList<Media> = mutableListOf()
}