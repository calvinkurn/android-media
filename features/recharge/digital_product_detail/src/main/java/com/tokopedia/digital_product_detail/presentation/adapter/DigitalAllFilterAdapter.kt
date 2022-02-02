package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterCheckboxBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPFilterAllViewHolder

class DigitalAllFilterAdapter(private val checkBoxListener: DigitalPDPFilterAllViewHolder.CheckBoxListener): RecyclerView.Adapter<DigitalPDPFilterAllViewHolder>()  {
    private var filterTagComponent = TelcoFilterTagComponent()
    private var listFilterDataCollection = mutableListOf<FilterTagDataCollection>()

    override fun getItemCount(): Int {
        return listFilterDataCollection.size
    }

    override fun onBindViewHolder(holder: DigitalPDPFilterAllViewHolder, position: Int) {
        holder.bind(filterTagComponent, listFilterDataCollection[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPDPFilterAllViewHolder {
        val binding = ViewPdpFilterCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return DigitalPDPFilterAllViewHolder(binding, checkBoxListener)
    }

    fun setCheckBoxList(filterTagComponent: TelcoFilterTagComponent){
        this.filterTagComponent = filterTagComponent
        this.listFilterDataCollection = filterTagComponent.filterTagDataCollections.toMutableList()
        notifyDataSetChanged()
    }

}