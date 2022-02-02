package com.tokopedia.digital_product_detail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterChipBinding
import com.tokopedia.digital_product_detail.presentation.adapter.viewholder.DigitalPDPChipFilterViewHolder

class DigitalChipsAdapter: RecyclerView.Adapter<DigitalPDPChipFilterViewHolder>(), DigitalPDPChipFilterViewHolder.ChipListener {

    private var listFilterDataCollection = mutableListOf<FilterTagDataCollection>()

    override fun getItemCount(): Int {
        return listFilterDataCollection.size
    }

    override fun onBindViewHolder(holder: DigitalPDPChipFilterViewHolder, position: Int) {
        holder.bind(listFilterDataCollection[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPDPChipFilterViewHolder {
        val binding = ViewPdpFilterChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return DigitalPDPChipFilterViewHolder(binding, this)
    }

    override fun onChipClicked(element: FilterTagDataCollection, position: Int) {

    }

    fun setChipList(listFilterDataCollection : List<FilterTagDataCollection>){
        this.listFilterDataCollection = listFilterDataCollection.take(LIMIT_FILTER_DATA_COLLECTION).toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        const val LIMIT_FILTER_DATA_COLLECTION = 5
    }
}