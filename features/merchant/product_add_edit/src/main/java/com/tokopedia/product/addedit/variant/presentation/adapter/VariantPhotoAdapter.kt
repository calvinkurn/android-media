package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantPhotoViewHolder

class VariantPhotoAdapter(private val onItemClickedListener: OnItemClickListener) :
        RecyclerView.Adapter<VariantPhotoViewHolder>(), VariantPhotoViewHolder.OnItemClickListener {

    private var items: MutableList<VariantPhoto> = mutableListOf()

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
        val variantPhoto = items[position]
        holder.bindData(variantPhoto)
    }

    fun getData(): List<VariantPhoto> {
        return items
    }

    fun setData(items: List<VariantPhoto>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addDataIfNotExist(item: VariantPhoto) {
        val isDataExist = items.any {
            it.variantUnitValueName == item.variantUnitValueName
        }
        if (!isDataExist) {
            this.items.add(item)
            notifyDataSetChanged()
        }
    }

    fun removeData(position: Int) {
        if (position >= 0 && position < this.items.size) {
            this.items.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun updateImageData(imageUrlOrPath: String, position: Int) {
        if (position >= 0 && position < this.items.size) {
            val variantName = this.items[position].variantUnitValueName
            this.items[position] = VariantPhoto(variantName, imageUrlOrPath)
            notifyItemChanged(position)
        }
    }

    override fun onItemClicked(position: Int) {
        onItemClickedListener.onItemClicked(position)
    }


}