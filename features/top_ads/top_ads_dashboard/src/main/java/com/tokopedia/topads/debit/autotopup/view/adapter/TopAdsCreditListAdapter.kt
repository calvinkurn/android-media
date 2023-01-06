package com.tokopedia.topads.debit.autotopup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.debit.autotopup.data.model.TopUpCreditItemData
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.CreditItemViewHolder

class TopAdsCreditListAdapter(private val onClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<CreditItemViewHolder>() {
    private val list: ArrayList<TopUpCreditItemData> by lazy { ArrayList() }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreditItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_top_up_credit_item, parent, false)
        return CreditItemViewHolder(view, onClicked)
    }

    override fun onBindViewHolder(holder: CreditItemViewHolder, position: Int) {
        holder.bind(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(adsTypeList: List<TopUpCreditItemData>) {
        list.clear()
        list.addAll(adsTypeList)
        notifyDataSetChanged()
    }

}
