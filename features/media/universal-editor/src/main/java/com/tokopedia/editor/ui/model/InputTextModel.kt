package com.tokopedia.editor.ui.model

import android.os.Parcelable
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.FontDetail
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
    var backgroundColor: Int? = null,
    var textAlign: FontAlignment = FontAlignment.CENTER,
    var fontDetail: FontDetail = FontDetail.OPEN_SAUCE_ONE_REGULAR,
    var config: AddTextModel? = null,
) : Parcelable {
    companion object {
        private const val DEFAULT_TEXT_COLOR = -1

        fun default() = InputTextModel()
    }
}
