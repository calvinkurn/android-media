package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel

/**
 * Created by dhaba
 */
class BalanceDividerAdapter (
    diffUtil: DiffUtil.ItemCallback<BalanceDividerModel>
): ListAdapter<BalanceDividerModel, BalanceDividerAdapter.BalanceWidgetDividerHolder>(diffUtil) {

    var attachedRecyclerView: RecyclerView? = null
    private var listDivider: List<BalanceDividerModel> = mutableListOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.attachedRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceWidgetDividerHolder {
        return BalanceWidgetDividerHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget_divider, parent, false))
    }

    override fun getItemCount(): Int {
        return listDivider.size
    }

    override fun onBindViewHolder(holder: BalanceWidgetDividerHolder, position: Int) {}

    class BalanceWidgetDividerHolder(v: View): RecyclerView.ViewHolder(v)
}