package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.viewholder.ShopHomeProductItemCarouselViewHolder

class ShopPageHomeCarousellAdapterTypeFactory() : BaseAdapterTypeFactory() {

    var shopPageHomeCarAdapter: ShopPageHomeCarousellAdapter? = null

    fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int {
        return ShopHomeProductItemCarouselViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHomeProductItemCarouselViewHolder.LAYOUT -> {
                ShopHomeProductItemCarouselViewHolder(parent, null,shopPageHomeCarAdapter?.uiModel)
            }
            else -> {
                return super.createViewHolder(parent, type)
            }
        }
    }
}