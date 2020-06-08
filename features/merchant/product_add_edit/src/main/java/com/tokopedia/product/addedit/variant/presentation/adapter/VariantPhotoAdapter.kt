package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.presentation.viewholder.VariantPhotoViewHolder

class VariantPhotoAdapter(private val onItemClickedListener: OnItemClickListener) : RecyclerView.Adapter<VariantPhotoViewHolder>(), VariantPhotoViewHolder.OnItemClickListener {

    private var items: MutableList<UnitValue> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantPhotoViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_variant_photo, parent, false)
        return VariantPhotoViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VariantPhotoViewHolder, position: Int) {
        val variantUnitValue = items[position]
        holder.bindData(variantUnitValue.value)
    }

    fun setData(items: List<UnitValue>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addData(item: UnitValue) {
        this.items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    override fun onItemClicked(position: Int) {
        onItemClickedListener.onItemClicked(position)
    }
}