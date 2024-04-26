package com.tokopedia.shop.home.view.model.showcase_navigation

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.WidgetTypeEnum
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance

data class ShowcaseNavigationUiModel(
    val appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance = LeftMainBannerAppearance(
        tabs = emptyList(),
        title = "",
        viewAllCtaAppLink = "",
        cornerShape = ShowcaseCornerShape.ROUNDED_CORNER
    ),
    var lastTabIndexSelected: Int = 0,
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetNameEnum.SHOWCASE_NAVIGATION_BANNER.value,
    override val type: String = WidgetTypeEnum.DISPLAY.value,
    override val header: Header = Header(),
    override val isFestivity: Boolean = false
): BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}

