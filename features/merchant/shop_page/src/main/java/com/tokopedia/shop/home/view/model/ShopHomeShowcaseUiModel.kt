package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

data class ShopHomeShowcaseUiModel(
    val categoryHeader: CategoryHeader,
    val tabs: List<ShopHomeShowCaseTab>,
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetName.SHOWCASE_NAVIGATION_BANNER,
    override val type: String = WidgetType.DISPLAY,
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
): BaseShopHomeWidgetUiModel() {

    data class CategoryHeader(
        val title: String,
        val ctaLink: String,
        val widgetStyle: ShopHomeShowcaseWidgetStyle
    )


    enum class ShopHomeShowcaseWidgetStyle {
        ROUNDED_CORNER,
        CIRCLE
    }

    enum class ShopHomeShowcaseMainBannerPosition {
        LEFT,
        TOP,
        CAROUSEL
    }

    data class ShopHomeShowCaseTab(
        val text: String,
        val imageUrl: String,
        val mainBannerPosition: ShopHomeShowcaseMainBannerPosition,
        val showcases: List<ShopHomeShowcase>,
    ) {
        data class ShopHomeShowcase(
            val id: String,
            val name: String,
            val imageUrl: String,
            val ctaLink: String,
            val isMainBanner: Boolean
        )

    }

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}

