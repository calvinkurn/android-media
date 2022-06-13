package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipFilterViewHolder

class DigitalChipsAdapter(private val chipListener: DigitalPDPChipFilterViewHolder.ChipListener,
                          private val limitFilterDataCollection: Int
): RecyclerView.Adapter<DigitalPDPChipFilterViewHolder>() {
    private var filterTagComponent = TelcoFilterTagComponent()
    private var listFilterDataCollection = mutableListOf<FilterTagDataCollection>()

    override fun getItemCount(): Int {
        return listFilterDataCollection.size
    }

    override fun onBindViewHolder(holder: DigitalPDPChipFilterViewHolder, position: Int) {
        holder.bind(filterTagComponent, listFilterDataCollection[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPDPChipFilterViewHolder {
        val binding = ViewPdpFilterChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return DigitalPDPChipFilterViewHolder(binding, chipListener)
    }


    fun setChipList(filterTagComponent: TelcoFilterTagComponent){
        this.filterTagComponent = filterTagComponent
        this.listFilterDataCollection = filterTagComponent.filterTagDataCollections.take(limitFilterDataCollection).toMutableList()
        notifyDataSetChanged()
    }
}