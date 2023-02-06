package com.tokopedia.media.editor.ui.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorAddLogoUiModel(
    val imageRealSize: Pair<Int, Int> = Pair(0, 0),
    var overlayLogoUrl: String = "",
    val logoUrl: String = ""
) : Parcelable
