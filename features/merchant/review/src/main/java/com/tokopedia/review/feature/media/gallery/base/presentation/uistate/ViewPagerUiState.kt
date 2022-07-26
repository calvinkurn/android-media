package com.tokopedia.review.feature.media.gallery.base.presentation.uistate

import com.tokopedia.kotlin.extensions.view.ZERO

data class ViewPagerUiState(
    val currentPagerPosition: Int = Int.ZERO,
    val previousPagerPosition: Int = Int.ZERO,
    val enableUserInput: Boolean = true
)
