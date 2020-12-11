package com.tokopedia.imagepicker.core


data class ImagePickerResult (
    var imageUrlOrPathList: MutableList<String> = mutableListOf(),
    val originalImageUrl : MutableList<String> = mutableListOf(),
    val isEditted: MutableList<Boolean> = mutableListOf()
)