package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopHomeShowcaseNavigationUiModel(
    val appearance: ShopHomeShowcaseNavigationBannerAppearance = TopMainBanner(
        title = "",
        viewAllCtaAppLink = "",
        showcases = emptyList()
    ),
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetName.SHOWCASE_NAVIGATION_BANNER,
    override val type: String = WidgetType.DISPLAY,
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

