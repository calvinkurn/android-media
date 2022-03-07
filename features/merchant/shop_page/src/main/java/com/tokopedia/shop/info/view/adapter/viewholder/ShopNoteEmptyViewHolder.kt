package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopNoteEmptyBinding
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopNoteEmptyViewHolder(val view: View): AbstractViewHolder<EmptyModel>(view) {
    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_note_empty
    }

    private val viewBinding: ItemShopNoteEmptyBinding? by viewBinding()
    private var title: Typography? = viewBinding?.title
    private var buttonAddNote: UnifyButton? = viewBinding?.buttonAddNote

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