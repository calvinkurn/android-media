package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.applyPaddingToLastItem

class PromoRecommendationDelegateAdapter(
    private val onVoucherClick: (PromoItem) -> Unit,
    private val onButtonUseRecommendedVoucherClick: (PromoRecommendationItem) -> Unit
) : DelegateAdapter<PromoRecommendationItem, PromoRecommendationDelegateAdapter.ViewHolder>(
    PromoRecommendationItem::class.java
) {

    companion object {
        private const val PADDING_BOTTOM_DP = 16
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoRecommendationItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: PromoUsageItemVoucherRecommendationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerView.applyPaddingToLastItem(PADDING_BOTTOM_DP)
        }

        fun bind(item: PromoRecommendationItem) {
            binding.btnRecommendationUseVoucher.setOnClickListener {
                onButtonUseRecommendedVoucherClick(item)
            }

            val voucherAdapter = VoucherAdapter(onVoucherClick)
            binding.tpgRecommendationTitle.text = item.title
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(binding.recyclerView.context)
                adapter = voucherAdapter
            }

            voucherAdapter.submit(item.promos)
        }
    }

    inner class VoucherAdapter(
        private val onVoucherClick: (PromoItem) -> Unit
    ) : RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

        private val differCallback = object : DiffUtil.ItemCallback<PromoItem>() {
            override fun areItemsTheSame(
                oldItem: PromoItem,
                newItem: PromoItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PromoItem,
                newItem: PromoItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        private val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = PromoUsageItemVoucherBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(differ.currentList[position])
        }

        inner class ViewHolder(
            private val binding: PromoUsageItemVoucherBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: PromoItem) {
                binding.voucherView.bind(item)
                binding.root.setOnClickListener { onVoucherClick(item) }
            }
        }

        fun submit(promos: List<PromoItem>) {
            differ.submitList(promos)
        }
    }
}
