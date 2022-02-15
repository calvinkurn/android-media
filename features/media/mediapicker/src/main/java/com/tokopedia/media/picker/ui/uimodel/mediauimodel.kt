package com.tokopedia.media.picker.ui.uimodel

import com.tokopedia.media.common.uimodel.MediaUiModel

// TODO: override hashCode on [MediaUiModel] instead

fun List<MediaUiModel>.containByName(media: MediaUiModel): Boolean {
    return this.any {
        it.name == media.name
    }
}

fun List<MediaUiModel>.getIndexOf(media: MediaUiModel): Int {
    return this.indices
        .firstOrNull {
            this[it].name == media.name
        }?: -1
}

fun List<MediaUiModel>?.hasVideoBy(count: Int): Boolean {
    return this?.filter { it.isVideo() }?.size?: 0 >= count
}

fun MutableList<MediaUiModel>.safeRemove(media: MediaUiModel): List<MediaUiModel> {
    val index = getIndexOf(media)
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