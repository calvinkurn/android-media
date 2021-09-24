package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.ItemPMProNewSellerRequirement
import kotlinx.android.synthetic.main.item_requirement_pm_pro_new_seller.view.*

class PMProNewSellerRequirementAdapter(val items: List<ItemPMProNewSellerRequirement>) :
    RecyclerView.Adapter<PMProNewSellerRequirementAdapter.PMProNewSellerRequirementViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PMProNewSellerRequirementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_requirement_pm_pro_new_seller, parent, false)
        return PMProNewSellerRequirementViewHolder(view)
    }

    override fun onBindViewHolder(holder: PMProNewSellerRequirementViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = items.size

    class PMProNewSellerRequirementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: ItemPMProNewSellerRequirement) {
            with(itemView) {
                ivRequirementPmProNewSeller.loadImage(data.imageUrl)
                tvRequirementPmProNewSeller.text = data.title
            }
        }
    }
}