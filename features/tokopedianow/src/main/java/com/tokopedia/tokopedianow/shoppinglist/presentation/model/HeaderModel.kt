package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel.GradientColor

data class HeaderModel(
    val pageTitle: String = String.EMPTY,
    val pageTitleColor: Int? = null,
    val ctaText: String = String.EMPTY,
    val ctaTextColor: Int? = null,
    val ctaChevronColor: Int? = null,
    val backgroundGradientColor: GradientColor? = null,
    val chooseAddressResIntColor: Int? = null,
    val isSuperGraphicImageShown: Boolean = false,
    val isChooseAddressShown: Boolean = false,
    val iconPullRefreshType: Int = LayoutIconPullRefreshView.TYPE_WHITE
)
