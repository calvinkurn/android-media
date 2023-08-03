package com.tokopedia.shop.home.view.model.nav_banner

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

data class ShopHomeCategory(
    val id: String,
    val title: String,
    val imageUrl: String,
    val appearance: ShopHomeCategoryAppearance,
    override val widgetId: String = "",
    override val layoutOrder: Int = -1,
    override val name: String = WidgetName.CATEGORY,
    override val type: String = "",
    override val header: Header = Header(),
    override val isFestivity: Boolean = false,
): BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}

