package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.viewholder.MultipleVariantEditSelectViewHolder

class MultipleVariantEditSelectTypeAdapter: RecyclerView.Adapter<MultipleVariantEditSelectViewHolder>(),
        MultipleVariantEditSelectViewHolder.OnFieldClickListener {

    private var items: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleVariantEditSelectViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_variant_edit_select, parent, false)
        return MultipleVariantEditSelectViewHolder(rootView, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MultipleVariantEditSelectViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun onFieldClicked(position: Int) {

    }

    fun setData(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItem(position: Int): String {
        return items[position]
    }
}