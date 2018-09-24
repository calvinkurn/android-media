package com.tokopedia.shop.note.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import kotlinx.android.synthetic.main.item_shop_info_note.view.*

class ShopNoteViewHolder(val view: View, val listener: OnNoteClicked): AbstractViewHolder<ShopNoteViewModel>(view) {
    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_info_note
    }
    override fun bind(element: ShopNoteViewModel) {
        itemView.label_view.title = element.title
        itemView.label_view.setOnClickListener { listener.onNoteClicked(adapterPosition.toLong(), element) }
    }

    interface OnNoteClicked{
        fun onNoteClicked(position: Long, shopNoteViewModel: ShopNoteViewModel)
    }
}