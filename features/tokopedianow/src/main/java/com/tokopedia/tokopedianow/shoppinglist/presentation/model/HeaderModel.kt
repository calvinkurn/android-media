package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel.GradientColor

data class HeaderModel(
    val pageTitle: String = String.EMPTY,
    val pageTitleColor: Int? = null,
    val ctaText: String = String.EMPTY,
    val ctaTextColor: Int? = null,
    val ctaChevronIsShown: Boolean = true,
    val ctaChevronColor: Int? = null,
    val backgroundGradientColor: GradientColor? = null
)
