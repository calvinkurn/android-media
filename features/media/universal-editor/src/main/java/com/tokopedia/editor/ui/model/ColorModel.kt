package com.tokopedia.editor.ui.model

data class ColorModel (
    val colorInt: Int = 0,
    val colorName: String = "",

    // used as color text when colorInt is used on background
    val textColorAlternate: Int = 0
)
