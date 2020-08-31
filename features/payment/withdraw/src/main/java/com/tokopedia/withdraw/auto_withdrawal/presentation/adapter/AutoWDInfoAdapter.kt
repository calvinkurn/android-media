package com.tokopedia.withdraw.auto_withdrawal.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.domain.model.InfoAutoWDData

class AutoWDInfoAdapter(private val infoAutoWDDataList: ArrayList<InfoAutoWDData>)
    : RecyclerView.Adapter<AutoWDInfoAdapter.AutoWDInfoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoWDInfoHolder {
        return AutoWDInfoHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.swd_item_awd_info, parent, false))
    }

    override fun getItemCount(): Int = infoAutoWDDataList.size

    override fun onBindViewHolder(holder: AutoWDInfoHolder, position: Int) {
        holder.bind(infoAutoWDDataList[position])
    }

    inner class AutoWDInfoHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAutoWdInfoTitle = itemView.findViewById<TextView>(R.id.tvAutoWDInfoTitle)
        private val tvAutoWDInfoDescription = itemView.findViewById<TextView>(R.id.tvAutoWDInfoDescription)
        private val ivInfoIcon = itemView.findViewById<ImageUnify>(R.id.ivInfoIcon)

        fun bind(infoAutoWDData: InfoAutoWDData) {
            ivInfoIcon.setImageUrl(infoAutoWDData.icon)
            tvAutoWdInfoTitle.text = infoAutoWDData.title
            tvAutoWDInfoDescription.text = infoAutoWDData.description
        }
    }
}