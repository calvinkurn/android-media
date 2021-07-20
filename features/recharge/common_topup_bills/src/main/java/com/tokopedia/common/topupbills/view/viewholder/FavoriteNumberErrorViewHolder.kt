package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberErrorStateBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberErrorStateListener
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberErrorDataView

class FavoriteNumberErrorViewHolder(
    private val binding: ItemTopupBillsFavoriteNumberErrorStateBinding,
    private val errorStateListener: FavoriteNumberErrorStateListener
) : AbstractViewHolder<TopupBillsFavNumberErrorDataView>(binding.root) {

    override fun bind(element: TopupBillsFavNumberErrorDataView?) {
        binding.run {
            commonTopupBillsErrorStateButton.setOnClickListener {
                errorStateListener.refreshFavoriteNumberPage()
            }
            commonTopupBillsErrorStateImg.setImageResource(
                R.drawable.common_topup_ic_ilustrasi_error
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number_error_state
    }
}