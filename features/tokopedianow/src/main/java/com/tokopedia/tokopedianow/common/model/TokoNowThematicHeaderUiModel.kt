package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowThematicHeaderTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.unifycomponents.ticker.TickerData

data class TokoNowThematicHeaderUiModel(
    val id: String = String.EMPTY,
    val pageTitle: String = String.EMPTY,
    val pageTitleColor: Int? = null,
    val ctaText: String = String.EMPTY,
    val ctaTextColor: Int? = null,
    val ctaChevronColor: Int? = null,
    val backgroundLightColor: String = String.EMPTY,
    val backgroundDarkColor: String = String.EMPTY,
    val backgroundGradientColor: GradientColor? = null,
    val isSuperGraphicImageShown: Boolean = false,
    val iconPullRefreshType: Int = LayoutIconPullRefreshView.TYPE_WHITE,
    val chosenAddress: ChosenAddress? = null,
    val ticker: Ticker? = null,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.SHOW
): Visitable<TokoNowThematicHeaderTypeFactory> {
    override fun type(typeFactory: TokoNowThematicHeaderTypeFactory): Int = typeFactory.type(this)

    data class GradientColor(
        val startColor: Int,
        val endColor: Int
    )

    data class ChosenAddress(
        val chooseAddressResIntColor: Int? = null,
        val isShown: Boolean = false
    )

    data class Ticker(
        val tickerList: List<TickerData> = emptyList()
    )
}
