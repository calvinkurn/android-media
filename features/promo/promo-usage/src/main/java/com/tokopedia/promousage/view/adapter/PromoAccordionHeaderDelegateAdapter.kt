package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherSectionBinding
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class PromoAccordionHeaderDelegateAdapter(
    private val onVoucherAccordionHeaderClick: (PromoAccordionHeaderItem) -> Unit
) : DelegateAdapter<PromoAccordionHeaderItem, PromoAccordionHeaderDelegateAdapter.ViewHolder>(
    PromoAccordionHeaderItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherSectionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoAccordionHeaderItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: PromoUsageItemVoucherSectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromoAccordionHeaderItem) {
            with(binding) {
                tpgHeaderTitle.text = item.title
                val chevronIcon = if (item.isExpanded) {
                    IconUnify.CHEVRON_UP
                } else {
                    IconUnify.CHEVRON_DOWN
                }
                binding.iconChevron.setImage(chevronIcon)
                binding.root.setOnClickListener { onVoucherAccordionHeaderClick(item) }
            }
        }
    }
}
