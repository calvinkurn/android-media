package com.tokopedia.topads.debit.autotopup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.TopAdsAutoTopUPCreditOptionViewHolder


class TopAdsAutoTopUpChipsAdapter : RecyclerView.Adapter<TopAdsAutoTopUPCreditOptionViewHolder>() {

    @LayoutRes
    private var itemLayout = R.layout.topads_dash_credit_option_chip
    private var selectedTabPosition = 0
    private var listener: OnCreditOptionItemClicked? = null
    private val chipsList: MutableList<DataCredit> = mutableListOf()

    fun setListener(listener: OnCreditOptionItemClicked) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsAutoTopUPCreditOptionViewHolder {
        return TopAdsAutoTopUPCreditOptionViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: TopAdsAutoTopUPCreditOptionViewHolder, position: Int) {
        holder.bind(chipsList[position])
        holder.itemView.setOnClickListener {
            selectedTabPosition = position
            listener?.onItemClicked(position)
            notifyDataSetChanged()
        }
        holder.toggleActivate(position == selectedTabPosition)
    }

    fun setChipData(data: CreditResponse) {
        chipsList.clear()
        data.credit.forEach {
            chipsList.add(it)
        }
        notifyDataSetChanged()
    }

    fun getSelected(): Int {
        return selectedTabPosition

    }

    override fun getItemCount() = chipsList.size

    fun setSelected(pos: Int) {
        selectedTabPosition = pos
        notifyDataSetChanged()
    }

    interface OnCreditOptionItemClicked {
        fun onItemClicked(position: Int)
    }

}
