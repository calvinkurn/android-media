package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.ShimmerBuyAgainUiModel

class BuyAgainShimmerViewHolder(view: View) : AbstractViewHolder<ShimmerBuyAgainUiModel>(view) {

    override fun bind(element: ShimmerBuyAgainUiModel?) = Unit

    companion object {
        val LAYOUT = R.layout.holder_transaction_buy_again_shimmer
    }
}
