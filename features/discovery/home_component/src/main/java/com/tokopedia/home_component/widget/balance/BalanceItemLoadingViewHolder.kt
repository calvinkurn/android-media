package com.tokopedia.home_component.widget.balance

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutBalanceItemBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemLoadingViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemUiModel>(v) {
    companion object {
        val LAYOUT = home_componentR.layout.layout_balance_item
    }

    private val binding: LayoutBalanceItemBinding? by viewBinding()

    override fun bind(
        model: BalanceItemUiModel,
    ) {

    }

    override fun bind(
        model: BalanceItemUiModel,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()) {
        } else {
            bind(model)
        }
    }
}
