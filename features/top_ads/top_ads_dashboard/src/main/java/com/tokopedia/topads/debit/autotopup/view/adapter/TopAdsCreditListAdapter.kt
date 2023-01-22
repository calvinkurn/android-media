package com.tokopedia.topads.debit.autotopup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.debit.autotopup.data.model.TopUpCreditItemData
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.CreditItemViewHolder

class TopAdsCreditListAdapter() :
    RecyclerView.Adapter<CreditItemViewHolder>() {
    private val list: ArrayList<TopUpCreditItemData> by lazy { ArrayList() }
    private var nominalClickListener:NominalClickListener?= null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreditItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_top_up_credit_item, parent, false)
        return CreditItemViewHolder(view, nominalClickListener)
    }

    override fun onBindViewHolder(holder: CreditItemViewHolder, position: Int) {
        holder.bind(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(nominalList: List<TopUpCreditItemData>) {
        list.clear()
        list.addAll(nominalList)
        notifyDataSetChanged()
    }

    fun setNominalClickListener(nominalClickListener: NominalClickListener){
        this.nominalClickListener = nominalClickListener
    }

    interface NominalClickListener{
        fun onNominalClicked(nominalList: ArrayList<TopUpCreditItemData>, position: Int)
    }
}
