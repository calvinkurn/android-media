package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.viewholder.ShopHomeCarousellProductViewHolder
import com.tokopedia.shop.home.view.viewholder.ShopHomeProductEtalaseTitleViewHolder
import com.tokopedia.shop.home.view.viewholder.ShopHomeProductViewHolder

class ShopHomeAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int {
        return ShopHomeProductViewHolder.LAYOUT
    }

    fun type(shopProductEtalaseTitleViewModel: ShopHomeProductEtalaseTitleUiModel): Int {
        return ShopHomeProductEtalaseTitleViewHolder.LAYOUT
    }

    fun type(shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel): Int {
        return ShopHomeCarousellProductViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHomeProductViewHolder.LAYOUT -> {
                ShopHomeProductViewHolder(parent, null)
            }
            ShopHomeProductEtalaseTitleViewHolder.LAYOUT -> {
                ShopHomeProductEtalaseTitleViewHolder(parent)
            }
            ShopHomeCarousellProductViewHolder.LAYOUT -> {
                ShopHomeCarousellProductViewHolder(parent)
            }
            else -> {
                return super.createViewHolder(parent, type)
            }
        }
    }
}