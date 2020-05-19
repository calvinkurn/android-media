package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.viewholder.VariantValueViewHolder

class VariantValueAdapter : RecyclerView.Adapter<VariantValueViewHolder>() {

    private var items: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantValueViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_value, parent, false)
        return VariantValueViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantValueViewHolder, position: Int) {
        val productNameRecommendation = items[position]
        holder.bindData(productNameRecommendation)
    }

    fun setData(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }
}