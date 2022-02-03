package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipListBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipFilterViewHolder
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipListFilterViewHolder

class DigitalFilterAdapter(
    private val chipListener: DigitalPDPChipFilterViewHolder.ChipListener,
    private val listListener: DigitalPDPChipListFilterViewHolder.ListFilterListener
): RecyclerView.Adapter<DigitalPDPChipListFilterViewHolder>() {

    private var listFilterDataComponent = mutableListOf<TelcoFilterTagComponent>()

    override fun getItemCount(): Int {
        return listFilterDataComponent.size
    }

    override fun onBindViewHolder(holder: DigitalPDPChipListFilterViewHolder, position: Int) {
        holder.bind(listFilterDataComponent[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPDPChipListFilterViewHolder {
        val binding = ViewPdpFilterChipListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DigitalPDPChipListFilterViewHolder(binding, chipListener, listListener)
    }

    fun setChipList(listFilterDataComponent: List<TelcoFilterTagComponent>){
        this.listFilterDataComponent = listFilterDataComponent.toMutableList()
        notifyDataSetChanged()
    }
}