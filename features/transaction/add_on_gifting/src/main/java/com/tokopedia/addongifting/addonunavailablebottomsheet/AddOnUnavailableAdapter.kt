package com.tokopedia.addongifting.addonunavailablebottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addongifting.databinding.ItemProductUnavailableBinding

class AddOnUnavailableAdapter : RecyclerView.Adapter<AddOnUnavailableProductViewHolder>() {

    var products: List<AddOnUnavailableProductUiModel> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return AddOnUnavailableProductViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnUnavailableProductViewHolder {
        val binding = ItemProductUnavailableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddOnUnavailableProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddOnUnavailableProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

}