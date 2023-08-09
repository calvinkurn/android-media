package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

class ShopHomeV4TerlarisUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: Header,
    override val isFestivity: Boolean,
    val productList: List<ShopHomeV4TerlarisItemUiModel>
) : BaseShopHomeWidgetUiModel() {

    val impressHolder = ImpressHolder()
    data class ShopHomeV4TerlarisItemUiModel(
        val id: String = "",
        val name: String = "",
        val price: String = "",
        val imgUrl: String = "",
        val appUrl: String = ""
    ) : ImpressHolder()

    override fun type(typeFactory: ShopWidgetTypeFactory?): Int {
        return if (typeFactory is ShopHomeAdapterTypeFactory) {
            typeFactory.type(this)
        } else {
            Int.ZERO
        }
    }
}
