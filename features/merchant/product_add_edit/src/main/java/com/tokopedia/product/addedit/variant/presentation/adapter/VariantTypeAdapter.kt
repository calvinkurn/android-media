package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.viewholder.VariantTypeViewHolder

class VariantTypeAdapter(private val clickListener: VariantTypeViewHolder.OnVariantTypeClickListener)
    : RecyclerView.Adapter<VariantTypeViewHolder>() {

    private var items: List<VariantDetail> = listOf()
    private var maxSelectedItems = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_type, parent, false)
        return VariantTypeViewHolder(rootView, clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantTypeViewHolder, position: Int) {
        holder.bindData(items[position], VariantTypeViewHolder.ViewHolderState.NORMAL)
    }

    fun setData(items: List<VariantDetail>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setMaxSelectedItems(max: Int) {
        maxSelectedItems = max
    }
}