package com.tokopedia.shop.settings.notes.view.adapter.factory

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteReorderViewHolder

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteReorderFactory(private val onStartDragListener: OnStartDragListener?) : BaseShopNoteFactory() {

    override fun type(model: ShopNoteViewModel): Int {
        return ShopNoteReorderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ShopNoteReorderViewHolder.LAYOUT) {
            ShopNoteReorderViewHolder(parent, onStartDragListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}