package com.tokopedia.shop.settings.notes.view.viewholder

import androidx.core.content.ContextCompat
import android.text.style.ForegroundColorSpan
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by hendry on 16/08/18.
 */
class ShopNoteViewHolder(itemView: View,
                         private val onOnShopNoteViewHolderListener: OnShopNoteViewHolderListener?) : AbstractViewHolder<ShopNoteUiModel>(itemView) {

    private val ivMenuMore: View
    private val tvNoteName: Typography
    private val tvLastUpdate: Typography
    private val boldColor: ForegroundColorSpan

    init {
        ivMenuMore = itemView.findViewById(R.id.ivMenuMore)
        tvNoteName = itemView.findViewById(R.id.tvNoteName)
        tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate)
        boldColor = ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
    }

    interface OnShopNoteViewHolderListener {
        fun onIconMoreClicked(shopNoteUiModel: ShopNoteUiModel)
    }

    override fun bind(shopNoteUiModel: ShopNoteUiModel) {
        tvNoteName.text = shopNoteUiModel.title!!
        tvLastUpdate.text = toReadableString(FORMAT_DATE_TIME, shopNoteUiModel.updateTimeUTC)
        ivMenuMore.setOnClickListener {
            onOnShopNoteViewHolderListener?.onIconMoreClicked(shopNoteUiModel)
        }
    }

    companion object {

        val LAYOUT = R.layout.item_shop_note
    }

}
