package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoritNumberBinding
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.model.FavoriteNumberDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class FavoriteNumberViewHolder(
        private val binding: ItemTopupBillsFavoritNumberBinding,
        private val favoriteNumberListener: OnFavoriteNumberClickListener
): AbstractViewHolder<FavoriteNumberDataView>(binding.root) {

    override fun bind(item: FavoriteNumberDataView) {
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
                commonTopupbillsFavoriteNumberIcon.setImageUrl(item.favoriteNumber.iconUrl)
            }
            commonTopupbillsFavoriteNumberContainer.setOnClickListener {
                favoriteNumberListener.onFavoriteNumberClick(item.favoriteNumber)
            }
            commonTopupbillsFavoriteNumberMenu.setOnClickListener {
                favoriteNumberListener.onFavoriteNumberMenuClick(item.favoriteNumber)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorit_number
    }
}