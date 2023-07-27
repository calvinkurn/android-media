package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherSectionBinding
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class PromoAccordionHeaderDelegateAdapter(
    private val onVoucherAccordionClick: (PromoAccordionHeaderItem) -> Unit
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
                tpgSectionTitle.text = item.title
                val iconDrawable = if (item.isExpanded) {
                    IconUnify.CHEVRON_UP
                } else {
                    IconUnify.CHEVRON_DOWN
                }
                binding.iconChevron.setImage(iconDrawable)
                binding.root.setOnClickListener { onVoucherAccordionClick(item) }
            }
        }
    }
}
