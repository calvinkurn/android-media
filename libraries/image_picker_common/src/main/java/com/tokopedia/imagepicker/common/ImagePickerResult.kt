package com.tokopedia.imagepicker.common


data class ImagePickerResult (
    var imageUrlOrPathList: MutableList<String> = mutableListOf(),
    val originalImageUrl : MutableList<String> = mutableListOf(),
    val isEditted: MutableList<Boolean> = mutableListOf(),
    val imagesFedIntoPicker: MutableList<String> = mutableListOf()
)