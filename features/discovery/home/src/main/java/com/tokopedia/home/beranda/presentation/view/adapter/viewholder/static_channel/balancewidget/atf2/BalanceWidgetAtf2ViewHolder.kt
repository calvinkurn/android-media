package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.atf2

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceAtf2DividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.layoutmanager.NpaLinearLayoutManager
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
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
        val itemList = getUiModelList(element)
        val totalData = element.balanceDrawerItemModels.size
        val spanCount = itemList.size

        if (binding?.rvBalanceWidgetData?.adapter == null) {
            balanceAdapter =
                BalanceAtf2Adapter(
                    listener,
                    BalanceTypeFactoryImpl(totalData)
                )
            binding?.rvBalanceWidgetData?.adapter = balanceAdapter
        }
        val layoutManager = binding?.rvBalanceWidgetData?.layoutManager

        if (binding?.rvBalanceWidgetData?.itemDecorationCount == 0) {
            binding?.rvBalanceWidgetData?.addItemDecoration(
                BalanceSpacingItemDecoration()
            )
        }

        if (layoutManager == null || layoutManager.itemCount != spanCount) {
            binding?.rvBalanceWidgetData?.layoutManager = NpaLinearLayoutManager(itemView.context)
        }
        balanceAdapter?.setItemList(itemList)
    }

    private fun getUiModelList(element: HomeBalanceModel) : List<BalanceVisitable> {
        val balanceModelList = mutableListOf<BalanceVisitable>()
        element.balanceDrawerItemModels.forEachIndexed { idx, it ->
            balanceModelList.add(it)
            if(HomeRollenceController.isUsingAtf2Variant() && idx < element.balanceDrawerItemModels.size - 1) {
                balanceModelList.add(BalanceAtf2DividerModel())
            }
        }
        return balanceModelList
    }
}
