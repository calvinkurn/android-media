package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import kotlinx.android.synthetic.main.item_logistic_info.view.*

class SomDetailLogisticInfoAdapter(private val logisticInfoAll: ArrayList<SomDetailOrder.Data.GetSomDetail.LogisticInfo.All>):
        RecyclerView.Adapter<SomDetailLogisticInfoAdapter.SomDetailLogisticInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SomDetailLogisticInfoViewHolder {
        return SomDetailLogisticInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_logistic_info, parent, false))
    }

    override fun getItemCount(): Int {
        return logisticInfoAll.size
    }

    override fun onBindViewHolder(holder: SomDetailLogisticInfoViewHolder, position: Int) {
        holder.bind(logisticInfoAll[position], position)
    }
    
    inner class SomDetailLogisticInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: SomDetailOrder.Data.GetSomDetail.LogisticInfo.All, position: Int) {
            with(itemView) {
                tvNumberItemInfo.text = StringBuilder("${position + 1}.")
                tvContentItemInfo.text = MethodChecker.fromHtml(data.infoTextLong)
            }
        }
    }
}