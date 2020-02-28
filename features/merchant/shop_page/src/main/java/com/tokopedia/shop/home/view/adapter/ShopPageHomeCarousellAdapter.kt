package com.tokopedia.shop.home.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

class ShopPageHomeCarousellAdapter(
        shopPageHomeCarousellAdapterTypeFactory: ShopPageHomeCarousellAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPageHomeCarousellAdapterTypeFactory>(shopPageHomeCarousellAdapterTypeFactory) {

    var uiModel: ShopHomeCarousellProductUiModel? = null

    fun setProductListData(productList: List<ShopHomeProductViewModel>) {
        val lastIndex = lastIndex
        visitables.addAll(productList.onEach {
            it.isCarousel = true
        })
        notifyItemRangeInserted(lastIndex, productList.size)
    }

}