package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.viewholder.VariantPhotoViewHolder

class VariantPhotoAdapter : RecyclerView.Adapter<VariantPhotoViewHolder>() {

    private var items: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantPhotoViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_photo, parent, false)
        return VariantPhotoViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantPhotoViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    fun setData(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }
}