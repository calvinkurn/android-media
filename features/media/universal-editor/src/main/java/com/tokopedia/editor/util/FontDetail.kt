package com.tokopedia.editor.util

import android.graphics.Typeface

private const val OPEN_SAUCE_ONE_FONT = "OpenSauceEditorRegular.ttf"
private const val OPEN_SAUCE_ONE_FONT_ITALIC = "OpenSauceEditorItalic.ttf"

enum class FontDetail(val fontName: String, val fontStyle: Int) {
    OPEN_SAUCE_ONE_REGULAR(OPEN_SAUCE_ONE_FONT, Typeface.NORMAL),
    OPEN_SAUCE_ONE_BOLD(OPEN_SAUCE_ONE_FONT, Typeface.BOLD),
    OPEN_SAUCE_ONE_ITALIC(OPEN_SAUCE_ONE_FONT_ITALIC, Typeface.NORMAL);
}
