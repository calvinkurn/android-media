package com.tokopedia.common.topupbills.view.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberShimmerBinding
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberShimmerDataView

class PersoFavoriteNumberShimmerViewHolder(
    binding: ItemTopupBillsFavoriteNumberShimmerBinding
) : AbstractViewHolder<TopupBillsPersoFavNumberShimmerDataView>(binding.root) {

    override fun bind(element: TopupBillsPersoFavNumberShimmerDataView?) {
        // do nothing
    }

    companion object {
        // reuse seamless favorite number layout
        @LayoutRes
        val LAYOUT = R.layout.item_topup_bills_favorite_number_shimmer
    }
}