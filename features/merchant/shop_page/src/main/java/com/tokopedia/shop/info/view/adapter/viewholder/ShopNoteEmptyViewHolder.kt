package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShopNoteEmptyViewHolder(val view: View): AbstractViewHolder<EmptyModel>(view) {
    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_note_empty
    }

    private var title: Typography? = itemView.findViewById(R.id.title)
    private var buttonAddNote: UnifyButton? = itemView.findViewById(R.id.buttonAddNote)

    override fun bind(element: EmptyModel) {
        title?.text = element.title
        if (element.callback != null){
            buttonAddNote?.visibility = View.VISIBLE
            buttonAddNote?.setOnClickListener { element.callback.onEmptyButtonClicked() }
        } else {
            buttonAddNote?.visibility = View.GONE
        }
    }
}