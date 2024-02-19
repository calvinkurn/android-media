package com.tokopedia.tokopedianow.recipecommon.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class TagUiModel(
    val tag: String,
    val shouldFormatTag: Boolean,
    val shouldUseStaticBackgroundColor: Boolean = true
)
