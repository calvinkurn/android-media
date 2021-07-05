package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel.Header

/**
 * Created by zulfikarrahman on 1/16/18.
 */

data class ShopHomeCarousellProductUiModel(
        override val widgetId: String = "",
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: Header = Header(),
        val productList: List<ShopHomeProductUiModel> = listOf()
) : BaseShopHomeWidgetUiModel {

    companion object{
        const val IS_ATC = 1
    }

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
