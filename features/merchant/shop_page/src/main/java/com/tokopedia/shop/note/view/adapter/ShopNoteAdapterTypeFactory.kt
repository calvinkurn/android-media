package com.tokopedia.shop.note.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.info.view.adapter.viewholder.ShopNoteEmptyViewHolder
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder
import com.tokopedia.shop.note.view.model.ShopNoteUiModel

class ShopNoteAdapterTypeFactory(val onNoteClicked: ShopNoteViewHolder.OnNoteClicked): BaseAdapterTypeFactory(){

    fun type(shopNoteUiModel: ShopNoteUiModel?) = ShopNoteViewHolder.LAYOUT

    override fun type(viewModel: EmptyModel) = ShopNoteEmptyViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            ShopNoteEmptyViewHolder.LAYOUT -> ShopNoteEmptyViewHolder(parent)
            ShopNoteViewHolder.LAYOUT -> ShopNoteViewHolder(parent, onNoteClicked)
            else -> super.createViewHolder(parent, type)
        }
    }
}