package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemPromoAccordionViewAllBinding
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class PromoAccordionViewAllDelegateAdapter(
    private val onViewAllVoucherClick: (PromoAccordionViewAllItem) -> Unit
) : DelegateAdapter<PromoAccordionViewAllItem, PromoAccordionViewAllDelegateAdapter.ViewHolder>(
    PromoAccordionViewAllItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemPromoAccordionViewAllBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoAccordionViewAllItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: PromoUsageItemPromoAccordionViewAllBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromoAccordionViewAllItem) {
            binding.tpgViewAll.text = binding.tpgViewAll.context.getString(
                R.string.promo_voucher_placeholder_view_all_voucher,
                item.hiddenPromoCount
            )
            binding.tpgViewAll.isVisible = item.isExpanded && item.isVisible
            binding.iconChevron.isVisible = item.isExpanded && item.isVisible
            binding.root.setOnClickListener { onViewAllVoucherClick(item) }
        }
    }
}
