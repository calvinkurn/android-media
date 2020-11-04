package com.tokopedia.shop.settings.notes.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteFactory(private val onShopNoteViewHolderListener: ShopNoteViewHolder.OnShopNoteViewHolderListener) : BaseShopNoteFactory() {

    override fun type(model: ShopNoteUiModel): Int {
        return ShopNoteViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ShopNoteViewHolder.LAYOUT) {
            ShopNoteViewHolder(parent, onShopNoteViewHolderListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}