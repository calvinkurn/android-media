package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.layoutmanager.NpaGridLayoutManager
import com.tokopedia.home.databinding.LayoutBalanceWidgetAtf2Binding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetAtf2ViewHolder(itemView: View, val listener: HomeCategoryListener?) :
    AbstractViewHolder<HomeBalanceModel>(itemView) {
    private var binding: LayoutBalanceWidgetAtf2Binding? by viewBinding()
    private var balanceAdapter: BalanceAtf2Adapter? = null

    companion object {
        val LAYOUT = R.layout.layout_balance_widget_atf2
    }

    override fun bind(element: HomeBalanceModel) {
        setLayout(element)
    }

    private fun setLayout(element: HomeBalanceModel) {
        val totalData = element.balanceDrawerItemModels.size
        val totalDataWithDivider = (totalData * 2) - 1

        if (binding?.rvBalanceWidgetData?.adapter == null) {
            balanceAdapter =
                BalanceAtf2Adapter(
                    listener,
                    BalanceTypeFactoryImpl(totalData)
                )
            binding?.rvBalanceWidgetData?.adapter = balanceAdapter
        }
        val layoutManager = binding?.rvBalanceWidgetData?.layoutManager
        if (layoutManager != null && layoutManager is NpaGridLayoutManager && layoutManager.itemCount != totalData) {
            binding?.rvBalanceWidgetData?.layoutManager = NpaGridLayoutManager(itemView.context, totalDataWithDivider)
        } else if (layoutManager == null) {
            binding?.rvBalanceWidgetData?.layoutManager = NpaGridLayoutManager(itemView.context, totalDataWithDivider)
        }
        balanceAdapter?.setItemList(element)
    }
}
