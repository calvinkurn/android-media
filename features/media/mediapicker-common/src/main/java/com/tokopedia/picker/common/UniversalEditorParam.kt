package com.tokopedia.picker.common

import android.os.Parcelable
import com.tokopedia.picker.common.types.ToolType
import com.tokopedia.picker.common.utils.MediaFile
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class UniversalEditorParam(
    // A list of medias (images or videos)
    var paths: List<String> = mutableListOf(),

    // Toolbar content
    val headerTitle: Int = R.string.universal_editor_toolbar_title,
    val proceedButtonText: Int = R.string.universal_editor_toolbar_action_button,

    // Editor tool list
    val tools: @RawValue Map<MediaType, List<Int>> = defaultToolList()
) : Parcelable {

    @IgnoredOnParcel
    val firstFile = MediaFile(paths.firstOrNull())

    /**
     * set the list of media files (image or video) to passing
     * into Universal Media Editor.
     */
    fun filePaths(list: List<String>) = apply { paths = list }
}

internal fun defaultToolList() = mapOf(
    MediaType.Image to listOf(
        ToolType.TEXT,
        ToolType.PLACEMENT
    ),
    MediaType.Video to listOf(
        ToolType.TEXT,
        ToolType.AUDIO_MUTE
    ),
)

@Parcelize
enum class MediaType(val value: Int) : Parcelable {
    Video(1),
    Image(2);
}
