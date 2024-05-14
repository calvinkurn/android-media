package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.widget

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.BalanceSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item.BalanceTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.BalanceWidgetAdapter
import com.tokopedia.home.databinding.LayoutDynamicBalanceWidgetBinding
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home.R as homeR
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetViewHolder(
    itemView: View,
    private val listener: HomeCategoryListener
) : AbstractViewHolder<BalanceWidgetUiModel>(itemView) {
    private var binding: LayoutDynamicBalanceWidgetBinding? by viewBinding()
    private var balanceAdapter: BalanceWidgetAdapter? = null

    companion object {

        @LayoutRes
        val LAYOUT = homeR.layout.layout_dynamic_balance_widget
    }

    override fun bind(element: BalanceWidgetUiModel) {
        if (binding?.rvBalanceWidget?.adapter == null) {
            balanceAdapter = BalanceWidgetAdapter(BalanceTypeFactoryImpl(listener))
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
