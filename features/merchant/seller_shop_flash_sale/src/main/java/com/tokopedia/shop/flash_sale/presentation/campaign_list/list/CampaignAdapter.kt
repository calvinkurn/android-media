package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemCampaignBinding
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel

class CampaignAdapter(
    private val onCampaignClicked: (CampaignUiModel, Int) -> Unit,
    private val onOverflowMenuClicked: (CampaignUiModel) -> Unit
) : RecyclerView.Adapter<CampaignViewHolder>() {

    private var campaigns: MutableList<CampaignUiModel> = mutableListOf()
    private var isLoading = false

    companion object {
        private const val FIRST_ITEM = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val binding =
            SsfsItemCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CampaignViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return campaigns.size
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        campaigns.getOrNull(position)?.let { campaign ->
            val isLoading = isLoading && (position == campaigns.lastIndex)
            holder.bind(
                position,
                campaign,
                onCampaignClicked,
                onOverflowMenuClicked,
                isLoading
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<CampaignUiModel>) {
        this.campaigns.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        this.campaigns = mutableListOf()
        notifyDataSetChanged()
    }

    fun showLoading() {
        if (itemCount.isMoreThanZero()) {
            isLoading = true
            notifyItemChanged(campaigns.lastIndex)
        }
    }

    fun hideLoading() {
        isLoading = false
    }

    fun getItems(): List<CampaignUiModel> {
        return campaigns
    }
}