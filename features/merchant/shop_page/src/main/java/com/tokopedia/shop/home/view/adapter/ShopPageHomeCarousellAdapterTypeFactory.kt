package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductItemCarouselViewHolder
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener

class ShopPageHomeCarousellAdapterTypeFactory(
        private val shopPageHomeProductClickListener: ShopPageHomeProductClickListener
) : BaseAdapterTypeFactory() {

    var shopPageHomeCarAdapter: ShopPageHomeCarousellAdapter? = null

    fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int {
        return ShopHomeProductItemCarouselViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHomeProductItemCarouselViewHolder.LAYOUT -> {
                ShopHomeProductItemCarouselViewHolder(
                        parent,
                        shopPageHomeProductClickListener,
                        shopPageHomeCarAdapter?.shopHomeCarousellProductUiModel,
                        shopPageHomeCarAdapter?.parentIndex ?: 0
                )
            }
            else -> {
                return super.createViewHolder(parent, type)
            }
        }
    }
}