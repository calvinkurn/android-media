package com.tokopedia.common.topupbills.favorite.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberBinding
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

class PersoFavoriteNumberViewHolder(
    private val binding: ItemTopupBillsFavoriteNumberBinding,
    private val favoriteNumberListener: OnPersoFavoriteNumberClickListener
): AbstractViewHolder<TopupBillsPersoFavNumberDataView>(binding.root) {

    override fun bind(item: TopupBillsPersoFavNumberDataView) {
        binding.run {
            if (item.subtitle.isNotEmpty())
                commonTopupbillsFavoriteNumberClientNumber.show()
            else
                commonTopupbillsFavoriteNumberClientNumber.hide()

            commonTopupbillsFavoriteNumberClientName.text = item.title
            commonTopupbillsFavoriteNumberClientNumber.text = item.subtitle

            if (item.iconUrl.isNotEmpty())
                commonTopupbillsFavoriteNumberIcon.loadImage(item.iconUrl)

            commonTopupbillsFavoriteNumberContainer.setOnClickListener {
                favoriteNumberListener.onFavoriteNumberClick(item, adapterPosition+1)
            }
            commonTopupbillsFavoriteNumberMenu.setOnClickListener {
                favoriteNumberListener.onFavoriteNumberMenuClick(item)
            }
        }
    }

    interface OnPersoFavoriteNumberClickListener {
        fun onFavoriteNumberClick(clientNumber: TopupBillsPersoFavNumberDataView, position: Int)
        fun onFavoriteNumberMenuClick(favNumberItem: TopupBillsPersoFavNumberDataView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number
    }
}