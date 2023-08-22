package com.tokopedia.editor.ui.model

import android.os.Parcelable
import com.tokopedia.editor.util.FontAlignment
import kotlinx.parcelize.Parcelize

/**
 * backgroundColor:
 * 1. text color
 * 2. background color
 */
@Parcelize
data class InputTextModel(
    var text: String = "",
    var textColor: Int = DEFAULT_TEXT_COLOR,
    var backgroundColor: Pair<Int, Int>? = null,
    var textAlign: FontAlignment = FontAlignment.CENTER,
    var config: AddTextModel? = null,
) : Parcelable {
    companion object {
        private const val DEFAULT_TEXT_COLOR = -1
    }
}
