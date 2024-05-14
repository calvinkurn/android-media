package com.tokopedia.home_component.widget.balance

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutDynamicBalanceWidgetErrorBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetErrorViewHolder (itemView: View) :
    AbstractViewHolder<BalanceWidgetErrorUiModel>(itemView) {
    private var binding: LayoutDynamicBalanceWidgetErrorBinding? by viewBinding()

    companion object {
        val LAYOUT = home_componentR.layout.layout_dynamic_balance_widget_error
        private const val START_PROGRESS_STATE = true
    }

    override fun bind(element: BalanceWidgetErrorUiModel) {
        binding?.errorBalanceWidget?.refreshBtn?.setOnClickListener {
            binding?.errorBalanceWidget?.progressState = START_PROGRESS_STATE
        }
    }
}
