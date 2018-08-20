package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import kotlinx.android.synthetic.main.item_shop_note_empty.view.*

class ShopNoteEmptyViewHolder(val view: View): AbstractViewHolder<EmptyModel>(view) {
    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_note_empty
    }

    override fun bind(element: EmptyModel) {
        itemView.title.text = element.title
        if (element.callback != null){
            itemView.buttonAddNote.visibility = View.VISIBLE
            itemView.buttonAddNote.setOnClickListener { element.callback.onEmptyButtonClicked() }
        } else {
            itemView.buttonAddNote.visibility = View.GONE
        }
    }
}