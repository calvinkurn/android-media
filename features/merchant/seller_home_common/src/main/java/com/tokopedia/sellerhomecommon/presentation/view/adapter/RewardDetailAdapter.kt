package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.databinding.ShcItemRewardDetailBinding
import com.tokopedia.sellerhomecommon.presentation.model.RewardDetailBenefit

class RewardDetailAdapter(
    private val benefitList: List<RewardDetailBenefit>
): RecyclerView.Adapter<RewardDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShcItemRewardDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = benefitList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = benefitList.size

    inner class ViewHolder(
        private val binding: ShcItemRewardDetailBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(benefit: RewardDetailBenefit) {
            binding.tvRewardDetailItemTitle.text = benefit.benefitTitle
            binding.tvRewardDetailItemDescription.text = benefit.benefitDescription
        }

    }
}
