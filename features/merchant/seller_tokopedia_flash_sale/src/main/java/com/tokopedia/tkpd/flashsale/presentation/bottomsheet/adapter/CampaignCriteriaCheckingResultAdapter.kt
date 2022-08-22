package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultBinding

class CampaignCriteriaCheckingResultAdapter: RecyclerView.Adapter<CampaignCriteriaCheckingResultAdapter.CriteriaViewHolder>() {

    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemCampaignCriteriaResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<String>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(private val binding: StfsItemCampaignCriteriaResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun setExpandIcon(isContentVisible: Boolean) {
            binding.iconExpand.setImage(if (isContentVisible) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            })
        }

        init {
            binding.iconExpand.setOnClickListener {
                binding.layoutContent.root.apply {
                    isVisible = !isVisible
                    setExpandIcon(isVisible)
                }
            }
        }

        fun bind(title: String) {
            binding.tfVariantName.text = title.take(10)
        }
    }
}