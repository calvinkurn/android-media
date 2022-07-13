package com.tokopedia.media.editor.ui.uimodel

class EditorUiModel(
    private val originalUrl: String
) {
    var removedBackgroundUrl: String? = null
    val editList = mutableListOf<EditorDetailUiModel>()

    var backValue = 0

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