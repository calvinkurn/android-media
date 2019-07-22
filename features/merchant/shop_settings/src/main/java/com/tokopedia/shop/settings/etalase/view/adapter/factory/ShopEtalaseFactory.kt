package com.tokopedia.shop.settings.etalase.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleViewModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreViewModel
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseTitleViewHolder
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseViewHolder
import com.tokopedia.shop.settings.etalase.view.viewholder.TickerReadMoreViewHolder

/**
 * Created by hendry on 16/08/18.
 */
class ShopEtalaseFactory(
        private val onShopEtalaseViewHolderListener: ShopEtalaseViewHolder.OnShopEtalaseViewHolderListener,
        private val tickerViewHolderViewHolderListener: TickerReadMoreViewHolder.TickerViewHolderViewHolderListener
) : BaseShopEtalaseFactory() {
    override fun type(model: TickerReadMoreViewModel): Int {
        return TickerReadMoreViewHolder.LAYOUT
    }

    override fun type(model: ShopEtalaseViewModel): Int {
        return ShopEtalaseViewHolder.LAYOUT
    }

    override fun type(model: ShopEtalaseTitleViewModel): Int {
        return ShopEtalaseTitleViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ShopEtalaseViewHolder.LAYOUT) {
            ShopEtalaseViewHolder(parent, onShopEtalaseViewHolderListener)
        }else if(type == TickerReadMoreViewHolder.LAYOUT){
            TickerReadMoreViewHolder(parent,tickerViewHolderViewHolderListener)
        }else if (type == ShopEtalaseTitleViewHolder.LAYOUT) {
            ShopEtalaseTitleViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}