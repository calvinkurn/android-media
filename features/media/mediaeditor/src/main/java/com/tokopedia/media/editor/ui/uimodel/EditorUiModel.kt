package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
import com.tokopedia.picker.common.utils.isVideoFormat
import kotlinx.parcelize.Parcelize

@Parcelize
class EditorUiModel(
    private val originalUrl: String = "",
    var removedBackgroundUrl: String? = null,
    val editList: MutableList<EditorDetailUiModel> = mutableListOf(),
    var backValue: Int = 0,
    var removeBackgroundStartState: Int? = null,
    var isVideo: Boolean = false,
    var isAutoCropped: Boolean = false,
    var originalRatio: Float = 1f
) : Parcelable {

    init {
        try {
            isVideo = isVideoFormat(originalUrl)
        } catch (e: Exception) {}
    }

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
        if (backValue >= UNDO_MAX_LIMIT) return false

        return !isVideo && ((editList.size - backValue) > if (isAutoCropped) {
            if (editList.isNotEmpty()) {
                // if auto crop is enable, check if image is cropped / already have target ratio
                // if image already have target ratio, then limit is same as no crop
                if (!editList.first().isToolCrop()) {
                    UNDO_LIMIT_NON_CROP
                } else {
                    UNDO_LIMIT_AUTO_CROP
                }
            } else {
                UNDO_LIMIT_AUTO_CROP
            }
        } else {
            UNDO_LIMIT_NON_CROP
        })
    }

    fun isShowRedoButton(): Boolean {
        return (!isVideo && backValue != 0)
    }

    fun getUndoStartIndex(): Int {
        return editList.size - backValue
    }

    fun getRedoStartIndex(): Int {
        return (editList.size - 1) - backValue
    }

    fun getFilteredStateList(): List<EditorDetailUiModel> {
        val maxStateLimit = (editList.size - 1) - backValue
        var minStateLimit = 0
        removeBackgroundStartState?.let {
            if (maxStateLimit < it) {
                // get bottom limit from another remove background state if any.
                editList.subList(0, maxStateLimit + 1).apply {
                    val list = this
                    if (list.isEmpty()) return@let

                    for (i in (list.size - 1) downTo 0) {
                        if (this[i].isToolRemoveBackground()) {
                            minStateLimit = i
                            return@let
                        }
                    }
                }
            } else minStateLimit = it
        }

        return editList.filterIndexed { index, _ ->
            index in minStateLimit..maxStateLimit
        }
    }

    fun isImageEdited(): Boolean {
        return editList.size > backValue
    }

    companion object {
        private const val UNDO_LIMIT_NON_CROP = 0
        private const val UNDO_LIMIT_AUTO_CROP = 1

        private const val UNDO_MAX_LIMIT = 5
    }
}
