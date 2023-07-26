package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.Promo
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.applyPaddingToLastItem

class VoucherRecommendationDelegateAdapter(
    private val onVoucherClick: (Promo) -> Unit,
    private val onButtonUseRecommendedVoucherClick: () -> Unit
) : DelegateAdapter<PromoRecommendationItem, VoucherRecommendationDelegateAdapter.ViewHolder>(PromoRecommendationItem::class.java) {

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

    inner class ViewHolder(private val binding: PromoUsageItemVoucherRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerView.applyPaddingToLastItem(PADDING_BOTTOM_DP)
            binding.btnRecommendationUseVoucher.setOnClickListener { onButtonUseRecommendedVoucherClick() }
        }

        fun bind(recommendation: PromoRecommendationItem) {
            val voucherAdapter = VoucherAdapter(onVoucherClick)

            binding.tpgRecommendationTitle.text = recommendation.title
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(binding.recyclerView.context)
                adapter = voucherAdapter
            }

            voucherAdapter.submit(recommendation.promos)
        }

    }
    inner class VoucherAdapter(private val onVoucherClick: (Promo) -> Unit) : RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

        private val differCallback = object : DiffUtil.ItemCallback<Promo>() {
            override fun areItemsTheSame(
                oldItem: Promo,
                newItem: Promo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Promo,
                newItem: Promo
            ): Boolean {
                return oldItem == newItem
            }
        }

        private val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = PromoUsageItemVoucherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(differ.currentList[position])
        }

        inner class ViewHolder(private val binding: PromoUsageItemVoucherBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(promo: Promo) {
                binding.voucherView.bind(promo)
                binding.root.setOnClickListener { onVoucherClick(promo) }
            }

        }
        fun submit(newPromos: List<Promo>) {
            differ.submitList(newPromos)
        }
    }

}
