package com.tokopedia.shop.settings.notes.view.viewholder

import android.support.v4.content.ContextCompat
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteViewHolder(itemView: View,
                         private val onOnShopNoteViewHolderListener: OnShopNoteViewHolderListener?) : AbstractViewHolder<ShopNoteViewModel>(itemView) {

    private val ivMenuMore: View
    private val tvNoteName: TextView
    private val tvLastUpdate: TextView
    private val boldColor: ForegroundColorSpan

    init {
        ivMenuMore = itemView.findViewById(R.id.ivMenuMore)
        tvNoteName = itemView.findViewById(R.id.tvNoteName)
        tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate)
        boldColor = ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.design.R.color.font_black_primary_70))
    }

    interface OnShopNoteViewHolderListener {
        fun onIconMoreClicked(shopNoteViewModel: ShopNoteViewModel)
    }

    override fun bind(shopNoteViewModel: ShopNoteViewModel) {
        tvNoteName.text = shopNoteViewModel.title!!
        tvLastUpdate.text = toReadableString(FORMAT_DATE_TIME, shopNoteViewModel.updateTimeUTC)
        ivMenuMore.setOnClickListener {
            onOnShopNoteViewHolderListener?.onIconMoreClicked(shopNoteViewModel)
        }
    }

    companion object {

        val LAYOUT = R.layout.item_shop_note
    }

}
