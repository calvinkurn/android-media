package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class BalanceItemLoadingViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemLoadingUiModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.layout_dynamic_balance_item_loading
    }

    override fun bind(
        model: BalanceItemLoadingUiModel,
    ) {

    }
}
