package com.tokopedia.shop_widget.note.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.common.customview.ShopPageLabelView
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemShopInfoNoteBinding
import com.tokopedia.shop_widget.note.view.model.ShopNoteUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopNoteViewHolder(val view: View, val listener: OnNoteClicked): AbstractViewHolder<ShopNoteUiModel>(view) {
    companion object {
        @JvmStatic val LAYOUT = R.layout.item_shop_info_note
    }

    private val viewBinding: ItemShopInfoNoteBinding? by viewBinding()
    private val labelView: ShopPageLabelView? = viewBinding?.labelView

    override fun bind(element: ShopNoteUiModel) {
        labelView?.title = element.title
        labelView?.setOnClickListener { listener.onNoteClicked(adapterPosition.toLong(), element) }
    }

    interface OnNoteClicked{
        fun onNoteClicked(position: Long, shopNoteUiModel: ShopNoteUiModel)
    }
}