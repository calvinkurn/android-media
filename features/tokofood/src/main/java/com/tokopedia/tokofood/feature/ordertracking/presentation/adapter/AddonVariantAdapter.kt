package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderDetailVariantAddOnBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.AddonVariantItemUiModel

class AddonVariantAdapter(private val addOnVariantList: List<AddonVariantItemUiModel>) :
    RecyclerView.Adapter<AddonVariantAdapter.AddonVariantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonVariantViewHolder {
        val binding = ItemTokofoodOrderDetailVariantAddOnBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddonVariantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddonVariantViewHolder, position: Int) {
        if (addOnVariantList.isNotEmpty()) {
            holder.bind(addOnVariantList[position])
        }
    }

    override fun getItemCount(): Int = addOnVariantList.size

    inner class AddonVariantViewHolder(private val binding: ItemTokofoodOrderDetailVariantAddOnBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AddonVariantItemUiModel) {
            with(binding) {
                tvVariantAddonName.text = item.displayName
            }
        }
    }
}