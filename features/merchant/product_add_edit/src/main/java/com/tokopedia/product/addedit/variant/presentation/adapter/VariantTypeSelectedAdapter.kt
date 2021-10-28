package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeSelectedViewHolder

class VariantTypeSelectedAdapter : RecyclerView.Adapter<VariantTypeSelectedViewHolder>() {

    private var items: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeSelectedViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_type_selected, parent, false)
        return VariantTypeSelectedViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantTypeSelectedViewHolder, position: Int) {
        val variantUnitValue = items[position]
        holder.bindData(variantUnitValue)
    }

    fun setData(items: List<String>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(item: String) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): String {
        return items[position]
    }
}