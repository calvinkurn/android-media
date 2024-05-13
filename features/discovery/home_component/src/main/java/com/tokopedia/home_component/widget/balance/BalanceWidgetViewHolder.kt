package com.tokopedia.home_component.widget.balance

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.LayoutBalanceWidgetBinding
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetViewHolder(
    itemView: View,
) : AbstractViewHolder<BalanceWidgetUiModel>(itemView) {
    private var binding: LayoutBalanceWidgetBinding? by viewBinding()
    private var balanceAdapter: BalanceWidgetAdapter? = null

    companion object {
        val LAYOUT = home_componentR.layout.layout_balance_widget
    }

    override fun bind(element: BalanceWidgetUiModel) {
        setLayout(element)
    }

    override fun bind(element: BalanceWidgetUiModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()) {
            balanceAdapter?.let { adapter ->
                adapter.currentList.forEachIndexed { index, _ ->
                    adapter.notifyItemChanged(index, payloads[0])
                }
            }
        } else {
            bind(element)
        }
    }

    private fun setLayout(element: BalanceWidgetUiModel) {
        if (binding?.rvBalanceWidget?.adapter == null) {
            balanceAdapter = BalanceWidgetAdapter(BalanceTypeFactoryImpl())
            binding?.rvBalanceWidget?.adapter = balanceAdapter
        }
        val layoutManager = binding?.rvBalanceWidget?.layoutManager

        if (binding?.rvBalanceWidget?.itemDecorationCount == 0) {
            binding?.rvBalanceWidget?.addItemDecoration(
                BalanceSpacingItemDecoration()
            )
        }

        if (layoutManager == null) {
            binding?.rvBalanceWidget?.layoutManager = NpaLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL)
        }
        balanceAdapter?.setItemList(element.balanceItems)
    }
}
