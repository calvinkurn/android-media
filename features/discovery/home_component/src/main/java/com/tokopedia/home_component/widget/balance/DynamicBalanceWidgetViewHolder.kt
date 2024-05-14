package com.tokopedia.home_component.widget.balance

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutDynamicBalanceWidgetBinding
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class DynamicBalanceWidgetViewHolder(
    itemView: View,
) : AbstractViewHolder<BalanceWidgetUiModel>(itemView) {
    private var binding: LayoutDynamicBalanceWidgetBinding? by viewBinding()
    private var balanceAdapter: BalanceWidgetAdapter? = null

    companion object {

        @LayoutRes
        val LAYOUT = home_componentR.layout.layout_dynamic_balance_widget
    }

    override fun bind(element: BalanceWidgetUiModel) {
        if (binding?.rvBalanceWidget?.adapter == null) {
            balanceAdapter = BalanceWidgetAdapter(BalanceTypeFactoryImpl())
            binding?.rvBalanceWidget?.adapter = balanceAdapter
        }
        binding?.rvBalanceWidget?.layoutManager = NpaLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL)

        if (binding?.rvBalanceWidget?.itemDecorationCount == 0) {
            binding?.rvBalanceWidget?.addItemDecoration(
                BalanceSpacingItemDecoration()
            )
        }
        balanceAdapter?.setItemList(element.balanceItems)
    }
}
