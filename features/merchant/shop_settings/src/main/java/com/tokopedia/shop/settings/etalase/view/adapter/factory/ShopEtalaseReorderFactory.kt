package com.tokopedia.shop.settings.etalase.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleViewModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreViewModel
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseReorderViewHolder
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseTitleViewHolder
import com.tokopedia.shop.settings.etalase.view.viewholder.TickerReadMoreEtalaseViewHolder

/**
 * Created by hendry on 16/08/18.
 */
class ShopEtalaseReorderFactory(
        private val onStartDragListener: OnStartDragListener?,
        private val tickerReadMoreListener: TickerReadMoreEtalaseViewHolder.TickerReadMoreListener?
) : BaseShopEtalaseFactory() {
    override fun type(model: TickerReadMoreViewModel): Int {
        return TickerReadMoreEtalaseViewHolder.LAYOUT
    }

    override fun type(model: ShopEtalaseViewModel): Int {
        return ShopEtalaseReorderViewHolder.LAYOUT
    }

    override fun type(model: ShopEtalaseTitleViewModel): Int {
        return ShopEtalaseTitleViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ShopEtalaseReorderViewHolder.LAYOUT) {
            ShopEtalaseReorderViewHolder(parent, onStartDragListener)
        } else if (type == ShopEtalaseTitleViewHolder.LAYOUT) {
            ShopEtalaseTitleViewHolder(parent)
        } else if(type == TickerReadMoreEtalaseViewHolder.LAYOUT){
            TickerReadMoreEtalaseViewHolder(parent,tickerReadMoreListener)
        }
        else {
            super.createViewHolder(parent, type)
        }
    }
}