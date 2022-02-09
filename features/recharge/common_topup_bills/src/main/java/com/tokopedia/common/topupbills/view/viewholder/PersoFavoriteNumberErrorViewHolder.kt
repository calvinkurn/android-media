package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberErrorStateBinding
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberErrorDataView

class PersoFavoriteNumberErrorViewHolder(
    private val binding: ItemTopupBillsFavoriteNumberErrorStateBinding,
    private val errorStateListener: PersoFavoriteNumberErrorStateListener
) : AbstractViewHolder<TopupBillsPersoFavNumberErrorDataView>(binding.root) {

    override fun bind(element: TopupBillsPersoFavNumberErrorDataView?) {
        binding.run {
            commonTopupBillsErrorStateButton.setOnClickListener {
                errorStateListener.refreshFavoriteNumberPage()
            }
            commonTopupBillsErrorStateImg.setImageResource(
                R.drawable.common_topup_ic_illustration_error
            )
        }
    }

    interface PersoFavoriteNumberErrorStateListener {
        fun refreshFavoriteNumberPage()
    }

    companion object {
        // reuse seamless favorite number layout
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number_error_state
    }
}