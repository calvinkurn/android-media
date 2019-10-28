package com.tokopedia.sellerorder.detail.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import kotlinx.android.synthetic.main.detail_label_info_item.view.*

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailLabelInfoAdapter: RecyclerView.Adapter<SomDetailLabelInfoAdapter.ViewHolder>() {
    var listLabelInfo = mutableListOf<SomDetailOrder.Data.GetSomDetail.LabelInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_label_info_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listLabelInfo.size
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_info_value.text = listLabelInfo[position].flagName
        holder.itemView.label_info_value.setTextColor(Color.parseColor(listLabelInfo[position].flagColor))
        holder.itemView.card_label_info.setCardBackgroundColor(Color.parseColor(listLabelInfo[position].flagBg))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}