package com.tokopedia.editor.util

import android.graphics.Typeface

private const val OPEN_SAUCE_ONE_FONT = "OpenSauceOneRegular.ttf"

enum class FontDetail(val fontName: String, val fontStyle: Int) {
    OPEN_SAUCE_ONE_REGULAR(OPEN_SAUCE_ONE_FONT, Typeface.NORMAL),
    OPEN_SAUCE_ONE_BOLD(OPEN_SAUCE_ONE_FONT, Typeface.BOLD),
    OPEN_SAUCE_ONE_ITALIC(OPEN_SAUCE_ONE_FONT, Typeface.ITALIC);
}
