package com.tokopedia.picker.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UniversalEditorParam(
    // A list of medias (images or videos)
    var paths: List<String> = mutableListOf(),

    // Toolbar content
    val headerTitle: Int = R.string.universal_editor_toolbar_title,
    val proceedButtonText: Int = R.string.universal_editor_toolbar_action_button,
) : Parcelable {

    fun filePaths(list: List<String>) = apply { paths = list }
}
