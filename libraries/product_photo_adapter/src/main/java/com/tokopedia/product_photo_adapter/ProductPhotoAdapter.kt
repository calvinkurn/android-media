package com.tokopedia.product_photo_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductPhotoAdapter(private val maxSize: Int,
                          private val productPhotoPaths: MutableList<String>,
                          private val onStartDragListener: ProductPhotoViewHolder.OnStartDragListener)
    : RecyclerView.Adapter<ProductPhotoViewHolder>(), ProductPhotoViewHolder.OnDeleteButtonClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_photo_item, parent, false)
        return ProductPhotoViewHolder(itemView, this, onStartDragListener)
    }

    override fun getItemCount(): Int {
        return productPhotoPaths.size
    }

    override fun onBindViewHolder(holder: ProductPhotoViewHolder, position: Int) {
        val productPhotoPath = productPhotoPaths[position]
        holder.bindData(position, productPhotoPath)
    }

    fun onMoveItem(fromPosition: Int, toPosition: Int) {
        val productPhotoPath = productPhotoPaths[fromPosition]
        // e.g. from 3 to 2
        // e.g. 0,1,2,3,4 => 0,1,2,4
        productPhotoPaths.removeAt(fromPosition)
        // e.g. 0,1,2,4 => 0,1,3,2,4
        productPhotoPaths.add(toPosition, productPhotoPath)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun addItem(productPhotoPath: String) {
        if (productPhotoPaths.size == maxSize) return
        else productPhotoPaths.add(productPhotoPath)
        notifyItemInserted(productPhotoPaths.lastIndex)
    }

    override fun onDeleteButtonClicked(position: Int) {
        productPhotoPaths.removeAt(position)
        notifyDataSetChanged()
    }
}