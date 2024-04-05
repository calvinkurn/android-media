package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.layoutmanager.NpaLinearLayoutManager
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.home.databinding.LayoutBalanceWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceWidgetViewHolder(
    itemView: View,
    val listener: HomeCategoryListener?,
    private val homeThematicUtil: HomeThematicUtil,
) :
    AbstractViewHolder<HomeBalanceModel>(itemView) {
    private var binding: LayoutBalanceWidgetBinding? by viewBinding()
    private var balanceAdapter: BalanceAdapter? = null

    companion object {
        val LAYOUT = R.layout.layout_balance_widget
    }

    override fun bind(element: HomeBalanceModel) {
        setLayout(element)
    }

    override fun bind(element: HomeBalanceModel, payloads: MutableList<Any>) {
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

    private fun setLayout(element: HomeBalanceModel) {
        val itemList = getUiModelList(element)
        val totalData = element.balanceDrawerItemModels.size
        val spanCount = itemList.size

        if (binding?.rvBalanceWidgetData?.adapter == null) {
            balanceAdapter =
                BalanceAdapter(
                    listener,
                    BalanceTypeFactoryImpl(totalData, homeThematicUtil)
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

    private fun getUiModelList(element: HomeBalanceModel): List<BalanceVisitable> {
        val balanceModelList = mutableListOf<BalanceVisitable>()
        element.balanceDrawerItemModels.forEachIndexed { idx, model ->
            balanceModelList.add(model)
            if (idx < element.balanceDrawerItemModels.size - 1) {
                balanceModelList.add(BalanceDividerModel())
            }
        }
        return balanceModelList
    }
}
