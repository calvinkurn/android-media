package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.sellerpersona.databinding.ItemPersonaSellerTypeBinding
import com.tokopedia.sellerpersona.view.model.SellerTypeUiModel

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class SellerTypeAdapter : Adapter<SellerTypeAdapter.TypeViewHolder>() {

    private val items: MutableList<SellerTypeUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonaSellerTypeBinding.inflate(inflater, parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<SellerTypeUiModel>) {
        this.items.clear()
        this.items.addAll(items)
    }

    class TypeViewHolder(binding: ItemPersonaSellerTypeBinding) : ViewHolder(binding.root) {

        fun bind(item: SellerTypeUiModel) {

        }
    }
}