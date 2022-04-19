package com.tokopedia.media.picker.ui.uimodel

import com.tokopedia.picker.common.uimodel.MediaUiModel

fun List<MediaUiModel>?.hasVideoBy(count: Int): Boolean {
    return this?.filter { it.isVideo() }?.size?: 0 >= count
}

fun MutableList<MediaUiModel>.safeRemove(media: MediaUiModel): List<MediaUiModel> {
    val index = indexOf(media)
    if (index != -1) removeAt(index)
    return this
}

fun List<MediaUiModel>.fastSubtract(medias: List<MediaUiModel>): List<MediaUiModel> {
    val elements = mutableMapOf<String, Boolean>()
    val results = mutableListOf<MediaUiModel>()

    medias.forEach {
        elements[it.name] = true
    }

    for (i in this.indices) {
        if (elements.containsKey(this[i].name)) continue
        results.add(this[i])
    }

    return results
}