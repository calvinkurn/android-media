package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberBinding
import com.tokopedia.common.topupbills.view.model.favorite.TopupBillsFavNumberDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.kotlin.extensions.view.show

class FavoriteNumberViewHolder(
        private val binding: ItemTopupBillsFavoriteNumberBinding,
        private val favoriteNumberListener: OnFavoriteNumberClickListener
): AbstractViewHolder<TopupBillsFavNumberDataView>(binding.root) {

    override fun bind(item: TopupBillsFavNumberDataView) {
        binding.run {

            if (item.favoriteNumber.clientName.isNotEmpty()) {
                commonTopupbillsFavoriteNumberClientName.show()
                commonTopupbillsFavoriteNumberClientNumber.show()
                commonTopupbillsFavoriteNumberClientName.text = item.favoriteNumber.clientName
                commonTopupbillsFavoriteNumberClientNumber.text = item.favoriteNumber.clientNumber
            } else {
                commonTopupbillsFavoriteNumberClientName.show()
                commonTopupbillsFavoriteNumberClientNumber.hide()
                commonTopupbillsFavoriteNumberClientName.text = item.favoriteNumber.clientNumber
            }

            if (item.favoriteNumber.iconUrl.isNotEmpty()) {
                commonTopupbillsFavoriteNumberIcon.loadImage(item.favoriteNumber.iconUrl)
            }
            commonTopupbillsFavoriteNumberContainer.setOnClickListener {
                favoriteNumberListener.onFavoriteNumberClick(item.favoriteNumber, adapterPosition+1)
            }
            commonTopupbillsFavoriteNumberMenu.setOnClickListener {
                favoriteNumberListener.onFavoriteNumberMenuClick(item.favoriteNumber)
            }
        }
    }

    interface OnFavoriteNumberClickListener {
        fun onFavoriteNumberClick(clientNumber: TopupBillsSeamlessFavNumberItem, position: Int)
        fun onFavoriteNumberMenuClick(favNumberItem: TopupBillsSeamlessFavNumberItem)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number
    }
}