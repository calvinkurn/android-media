package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * This model contains all tool state.
 *
 * [EditorModel] collect and manage the tool state in universal media editor,
 * such as, the add text components (both image and video), image placement,
 * audio mute handler, trim, etc.
 */
@Parcelize
data class EditorModel(
    val image: ImageModel? = null,
    val video: VideoModel? = null,
) : Parcelable {
    fun clone(): EditorModel {
        return EditorModel(
            image?.copy(), video?.copy()
        )
    }
}
