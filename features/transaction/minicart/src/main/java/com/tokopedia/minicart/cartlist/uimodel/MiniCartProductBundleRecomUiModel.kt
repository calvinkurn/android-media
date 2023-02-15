package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel

data class MiniCartProductBundleRecomUiModel(
    val shopId: String = "",
    val warehouseId: String = "",
    val title: String = "",
    var productBundleList: List<ShopHomeProductBundleItemUiModel> = listOf()
) : Visitable<MiniCartListAdapterTypeFactory> {
    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int = typeFactory.type(this)
}
