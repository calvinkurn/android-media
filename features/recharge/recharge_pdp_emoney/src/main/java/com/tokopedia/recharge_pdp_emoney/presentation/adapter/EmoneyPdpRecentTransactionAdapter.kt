package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.RecentTransactionViewHolder

/**
 * @author by jessica on 05/04/21
 */
class EmoneyPdpRecentTransactionAdapter(
        private val list: List<TopupBillsRecommendation>,
        private val listener: RecentTransactionViewHolder.ActionListener) : RecyclerView.Adapter<RecentTransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(RecentTransactionViewHolder.LAYOUT, parent, false)
        return RecentTransactionViewHolder(view, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecentTransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }
}