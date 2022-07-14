package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.BalanceWidgetItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceDividerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.layoutmanager.NpaGridLayoutManager
import com.tokopedia.home.databinding.LayoutBalanceWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class BalanceWidgetViewHolder(itemView: View, val listener: HomeCategoryListener?) :
    AbstractViewHolder<HomeBalanceModel>(itemView) {
    private var binding: LayoutBalanceWidgetBinding? by viewBinding()
    private var balanceAdapter: BalanceAdapter? = null
    private var balanceDividerAdapter: BalanceDividerAdapter? = null

    companion object {
        var LAYOUT = R.layout.layout_balance_widget
        private const val FIRST_ITEM_DECORATION = 0
    }

    override fun bind(element: HomeBalanceModel) {
        setLayout(element)
    }

    private fun setLayout(element: HomeBalanceModel) {
        val totalData = element.balanceDrawerItemModels.size
        if (binding?.rvBalanceWidget?.adapter == null) {
            balanceAdapter =
                BalanceAdapter(listener, object : DiffUtil.ItemCallback<BalanceDrawerItemModel>() {
                    override fun areItemsTheSame(
                        oldItem: BalanceDrawerItemModel,
                        newItem: BalanceDrawerItemModel
                    ): Boolean {
                        return oldItem.state == newItem.state
                    }

                    override fun areContentsTheSame(
                        oldItem: BalanceDrawerItemModel,
                        newItem: BalanceDrawerItemModel
                    ): Boolean {
                        return oldItem == newItem
                    }
                })
            binding?.rvBalanceWidget?.adapter = balanceAdapter
        }
        binding?.rvBalanceWidget?.layoutManager = getLayoutManager(totalData)
        try {
            if (binding?.rvBalanceWidget?.itemDecorationCount == FIRST_ITEM_DECORATION) {
                binding?.rvBalanceWidget?.addItemDecoration(BalanceWidgetItemDecoration(totalData))
            } else {
                binding?.rvBalanceWidget?.removeItemDecorationAt(FIRST_ITEM_DECORATION)
                binding?.rvBalanceWidget?.addItemDecoration(BalanceWidgetItemDecoration(totalData))
            }
        } catch (e: Exception) {

        }
        balanceAdapter?.setItemList(element)

        //divider
        if (binding?.rvBalanceDivider?.adapter == null) {
            balanceDividerAdapter =
                BalanceDividerAdapter(object : DiffUtil.ItemCallback<BalanceDividerModel>() {
                    override fun areItemsTheSame(
                        oldItem: BalanceDividerModel,
                        newItem: BalanceDividerModel
                    ): Boolean {
                        return oldItem == newItem
                    }

                    override fun areContentsTheSame(
                        oldItem: BalanceDividerModel,
                        newItem: BalanceDividerModel
                    ): Boolean {
                        return oldItem.equals(newItem)
                    }
                })
            binding?.rvBalanceDivider?.adapter = balanceDividerAdapter
        }
        binding?.rvBalanceDivider?.layoutManager = getLayoutManager(totalData)
        balanceDividerAdapter?.addDivider(totalData)
    }

    fun getSubscriptionView(): View? {
        if (balanceAdapter?.getItemList()?.containsSubscription() == true) {
            return balanceAdapter?.getSubscriptionView()
        }
        return null
    }

    private fun getLayoutManager(totalData: Int): NpaGridLayoutManager {
        return NpaGridLayoutManager(itemView.context, totalData)
    }
}