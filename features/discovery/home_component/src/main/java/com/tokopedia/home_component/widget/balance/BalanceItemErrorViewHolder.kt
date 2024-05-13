package com.tokopedia.home_component.widget.balance

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutBalanceItemErrorBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemErrorViewHolder(
    v: View,
): AbstractViewHolder<BalanceItemErrorUiModel>(v) {
    companion object {
        val LAYOUT = home_componentR.layout.layout_balance_item_error
    }

    private val binding: LayoutBalanceItemErrorBinding? by viewBinding()

    override fun bind(
        model: BalanceItemErrorUiModel,
    ) {

    }

    override fun bind(
        model: BalanceItemErrorUiModel,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()) {
        } else {
            bind(model)
        }
    }
}
