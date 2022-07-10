package com.tokopedia.product.manage.feature.campaignstock.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.databinding.ItemCampaignStockInfoCardBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.helper.VariantReservedViewHolderHelper.bind
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel

class VariantReservedEventInfoAdapter(private val itemList: List<ReservedEventInfoUiModel>) :
    RecyclerView.Adapter<VariantReservedEventInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCampaignStockInfoCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemList.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = itemList.count()

    class ViewHolder(private val binding: ItemCampaignStockInfoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: ReservedEventInfoUiModel) {
            binding.bind(itemView.context, uiModel)
        }
    }
}