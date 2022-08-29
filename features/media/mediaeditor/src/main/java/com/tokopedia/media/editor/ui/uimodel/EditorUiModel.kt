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
    var isAutoCropped: Boolean = false

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

    fun isShowUndoButton(): Boolean {
        return (!isVideo && (editList.size - backValue) > if (isAutoCropped)
            UNDO_LIMIT_AUTO_CROP
        else
            UNDO_LIMIT_NON_CROP)
    }

    fun isShowRedoButton(): Boolean {
        return (!isVideo && backValue != 0)
    }

    fun getUndoStartIndex(): Int{
        return editList.size - backValue
    }

    fun getRedoStartIndex(): Int{
        return (editList.size - 1) - backValue
    }

    companion object {
        private const val UNDO_LIMIT_NON_CROP = 0
        private const val UNDO_LIMIT_AUTO_CROP = 1
    }
}