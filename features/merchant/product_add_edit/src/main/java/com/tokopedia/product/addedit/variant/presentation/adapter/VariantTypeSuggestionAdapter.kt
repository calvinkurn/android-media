package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantTypeSuggestionViewHolder

class VariantTypeSuggestionAdapter : RecyclerView.Adapter<VariantTypeSuggestionViewHolder>() {

    private var highlightCharLength: Int = 0
    private var items: MutableList<String> = mutableListOf()
    private var onItemClickedListener: ((position: Int, variantTypeName: String) -> Unit)? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantTypeSuggestionViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_variant_type_suggestion, parent, false)
        return initViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: VariantTypeSuggestionViewHolder, position: Int) {
        val variantUnitValue = items[position]
        holder.bindData(variantUnitValue, highlightCharLength)
    }

    private fun initViewHolder(rootView: View): VariantTypeSuggestionViewHolder {
        val viewHolder = VariantTypeSuggestionViewHolder(rootView)

        viewHolder.binding?.root?.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClickedListener?.invoke(position, items[position])
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

    fun setHighlightCharLength(length: Int) {
        highlightCharLength = length
        notifyDataSetChanged()
    }

    fun setOnItemClickedListener(listener: (position: Int, variantTypeName: String) -> Unit) {
        onItemClickedListener = listener
    }
}