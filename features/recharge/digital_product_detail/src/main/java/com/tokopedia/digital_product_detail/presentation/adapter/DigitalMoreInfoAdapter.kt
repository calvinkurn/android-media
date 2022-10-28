package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.databinding.ViewMoreInfoBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPViewMoreInfoViewHolder

class DigitalMoreInfoAdapter: RecyclerView.Adapter<DigitalPDPViewMoreInfoViewHolder>() {
    private var listInfo = emptyList<String>()

    override fun getItemCount(): Int = listInfo.size

    override fun onBindViewHolder(holder: DigitalPDPViewMoreInfoViewHolder, position: Int) {
        holder.bind(listInfo[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalPDPViewMoreInfoViewHolder {
        val binding = ViewMoreInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalPDPViewMoreInfoViewHolder(binding)
    }

    fun setListInfo(listInfo: List<String>){
        this.listInfo = listInfo
        notifyDataSetChanged()
    }
}