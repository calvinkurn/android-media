package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipListBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipListFilerViewHolder

class DigitalFilterAdapter: RecyclerView.Adapter<DigitalPDPChipListFilerViewHolder>() {

    private var listFilterDataComponent = mutableListOf<TelcoFilterTagComponent>()

    override fun getItemCount(): Int {
        return listFilterDataComponent.size
    }

    override fun onBindViewHolder(holder: DigitalPDPChipListFilerViewHolder, position: Int) {
        holder.bind(listFilterDataComponent[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPDPChipListFilerViewHolder {
        val binding = ViewPdpFilterChipListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalPDPChipListFilerViewHolder(binding)
    }

    fun setChipList(listFilterDataComponent: List<TelcoFilterTagComponent>){
        this.listFilterDataComponent = listFilterDataComponent.toMutableList()
        notifyDataSetChanged()
    }

}