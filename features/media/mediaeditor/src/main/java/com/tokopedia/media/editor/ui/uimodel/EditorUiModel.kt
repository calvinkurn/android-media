package com.tokopedia.media.editor.ui.uimodel

import com.tokopedia.picker.common.utils.isVideoFormat

class EditorUiModel(
    private val originalUrl: String
) {
    var removedBackgroundUrl: String? = null
    val editList = mutableListOf<EditorDetailUiModel>()

    var backValue = 0
    var removeBackgroundStartState = 0

    val isVideo: Boolean = isVideoFormat(originalUrl)

    fun getImageUrl(): String {
        return if (editList.isNotEmpty()) {
            val index = (editList.size - 1) - backValue
            if (index < 0) originalUrl else editList[index].resultUrl!!
        } else {
            originalUrl
        }
    }

    fun getOriginalUrl(): String {
        return originalUrl
    }
}