package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputTextModel(
    var text: String = "",
    // TODO style, font, etc.
    var addText: AddTextModel? = null,
) : Parcelable
