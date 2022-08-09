package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDividerModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceDividerViewHolder

/**
 * Created by dhaba
 */
class BalanceDividerAdapter (
    diffUtil: DiffUtil.ItemCallback<BalanceDividerModel>
): ListAdapter<BalanceDividerModel, BalanceDividerViewHolder>(diffUtil) {

    private var attachedRecyclerView: RecyclerView? = null
    private var listDivider: MutableList<BalanceDividerModel> = mutableListOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.attachedRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceDividerViewHolder {
        return BalanceDividerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget_divider, parent, false))
    }

    override fun getItemCount(): Int {
        return listDivider.size
    }

    fun addDivider(totalDivider: Int) {
        listDivider.clear()
        repeat(totalDivider) {
            listDivider.add(BalanceDividerModel())
        }
        super.submitList(listDivider)
    }

    override fun onBindViewHolder(holder: BalanceDividerViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getSubscriptionView(positionSubscription: Int) : View? {
        val viewHolder = attachedRecyclerView?.findViewHolderForAdapterPosition(positionSubscription)
        viewHolder?.let {
            if (it is BalanceDividerViewHolder) {
                return it.coachMarkView
            }
        }
        return null
    }
}