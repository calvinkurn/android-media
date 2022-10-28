package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.InitialShimmerTransactionRevampDataModel

class InitialShimmeringTransactionDataRevampViewHolder (itemView: View)
    : AbstractViewHolder<InitialShimmerTransactionRevampDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_nav_shimmer_transaction_data_revamp
    }

    override fun bind(element: InitialShimmerTransactionRevampDataModel) {
    }
}