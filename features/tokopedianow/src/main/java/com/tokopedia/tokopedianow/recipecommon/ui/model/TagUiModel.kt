package com.tokopedia.tokopedianow.recipecommon.ui.model

data class TagUiModel(
    val tag: String,
    val shouldFormatTag: Boolean,
    val shouldUseStaticBackgroundColor: Boolean = true
)
