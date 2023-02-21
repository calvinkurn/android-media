package com.tokopedia.shop.settings.notes.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.util.FORMAT_DATE_TIME
import com.tokopedia.shop.settings.common.util.toReadableString
import com.tokopedia.shop.settings.databinding.ItemShopNoteBinding
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteViewHolder(itemView: View, private val onOnShopNoteViewHolderListener: OnShopNoteViewHolderListener?) : AbstractViewHolder<ShopNoteUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_shop_note
    }

    private val binding: ItemShopNoteBinding? by viewBinding()

    interface OnShopNoteViewHolderListener {
        fun onIconMoreClicked(shopNoteUiModel: ShopNoteUiModel)
    }

    override fun bind(shopNoteUiModel: ShopNoteUiModel) {
        binding?.apply {
            tvNoteName.text = shopNoteUiModel.title
            tvLastUpdate.text = toReadableString(FORMAT_DATE_TIME, shopNoteUiModel.updateTimeUTC)
            ivMenuMore.setOnClickListener {
                onOnShopNoteViewHolderListener?.onIconMoreClicked(shopNoteUiModel)
            }
        }
    }
}
