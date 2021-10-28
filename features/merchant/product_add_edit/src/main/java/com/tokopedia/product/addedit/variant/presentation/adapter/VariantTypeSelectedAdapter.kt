package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeSelectedViewHolder

class VariantTypeSelectedAdapter : RecyclerView.Adapter<VariantTypeSelectedViewHolder>() {

    private var items: MutableList<String> = mutableListOf()
    private var onEditButtonClickedListener: ((position: Int) -> Unit)? = null
    private var onDeleteButtonClickedListener: ((position: Int) -> Unit)? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeSelectedViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_variant_type_selected, parent, false)
        return initViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: VariantTypeSelectedViewHolder, position: Int) {
        val variantUnitValue = items[position]
        holder.bindData(variantUnitValue)
    }

    private fun initViewHolder(rootView: View): VariantTypeSelectedViewHolder {
        val viewHolder = VariantTypeSelectedViewHolder(rootView)
        val position = viewHolder.adapterPosition

        viewHolder.binding?.iconDelete?.setOnClickListener {
            onDeleteButtonClickedListener?.invoke(position)
        }
        viewHolder.binding?.iconEdit?.setOnClickListener {
            onEditButtonClickedListener?.invoke(position)
        }

        return viewHolder
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

    fun setOnEditButtonClickedListener(listener: (position: Int) -> Unit) {
        onEditButtonClickedListener = listener
    }

    fun setOnDeleteButtonClickedListener(listener: (position: Int) -> Unit) {
        onDeleteButtonClickedListener = listener
    }
}