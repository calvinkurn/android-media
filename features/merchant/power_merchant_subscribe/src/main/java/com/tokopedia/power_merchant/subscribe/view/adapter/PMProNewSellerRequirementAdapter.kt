package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemRequirementPmProNewSellerBinding
import com.tokopedia.power_merchant.subscribe.view.model.ItemPMProNewSellerRequirement

class PMProNewSellerRequirementAdapter(val items: List<ItemPMProNewSellerRequirement>) :
    RecyclerView.Adapter<PMProNewSellerRequirementAdapter.PMProNewSellerRequirementViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PMProNewSellerRequirementViewHolder {
        val binding = ItemRequirementPmProNewSellerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PMProNewSellerRequirementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PMProNewSellerRequirementViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = items.size

    class PMProNewSellerRequirementViewHolder(private val binding: ItemRequirementPmProNewSellerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ItemPMProNewSellerRequirement) {
            with(binding) {
                ivRequirementPmProNewSeller.loadImage(data.imageUrl)
                tvRequirementPmProNewSeller.text = data.title
            }
        }
    }
}