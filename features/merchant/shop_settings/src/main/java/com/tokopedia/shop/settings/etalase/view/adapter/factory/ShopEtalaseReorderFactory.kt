package com.tokopedia.shop.settings.etalase.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseTitleDataModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseDataModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreDataModel
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
    override fun type(model: TickerReadMoreDataModel): Int {
        return TickerReadMoreEtalaseViewHolder.LAYOUT
    }

    override fun type(model: ShopEtalaseDataModel): Int {
        return ShopEtalaseReorderViewHolder.LAYOUT
    }

    override fun type(model: ShopEtalaseTitleDataModel): Int {
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