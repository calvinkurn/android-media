package com.tokopedia.editor.ui.model

data class ColorModel (
    // colorInt is store color that represent on UI dot
    val colorInt: Int = 0,

    val colorName: String = "",

    // used as text color when colorInt is used on background
    val textColorAlternate: Int = 0
)
