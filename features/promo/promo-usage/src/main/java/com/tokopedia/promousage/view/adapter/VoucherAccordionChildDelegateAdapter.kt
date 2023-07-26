package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.domain.entity.Promo

class VoucherAccordionChildDelegateAdapter(
    private val onVoucherClick: (Promo) -> Unit
) : DelegateAdapter<Promo, VoucherAccordionChildDelegateAdapter.ViewHolder>(Promo::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: Promo, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(promo: Promo) {
            if (promo.isExpanded) {
                binding.voucherView.bind(promo)
            }

            binding.voucherView.isVisible = promo.isExpanded

            binding.root.setOnClickListener { onVoucherClick(promo) }
        }
    }

}
