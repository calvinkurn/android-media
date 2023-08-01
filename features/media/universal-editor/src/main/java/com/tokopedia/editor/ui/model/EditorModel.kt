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
    val image: ImageModel?,
    val video: VideoModel?,
) : Parcelable {

    companion object {
        fun default(): EditorModel {
            return EditorModel(null, null)
        }

        fun createImage(image: ImageModel): EditorModel {
            return EditorModel(image = image, video = null)
        }

        fun createVideo(video: VideoModel): EditorModel {
            return EditorModel(video = video, image = null)
        }
    }
}
