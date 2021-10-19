package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel.Header

/**
 * Created by Rafli Syam on 03/08/21
 */

data class ShopHomeShowcaseListSliderUiModel(
        override val widgetId: String = "",
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: Header = Header(),
        val showcaseListItem: List<ShopHomeShowcaseListItemUiModel> = listOf()
) : BaseShopHomeWidgetUiModel() {

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
