package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class PromoAccordionItemDelegateAdapter(
    private val onClickPromo: (PromoItem) -> Unit
) : DelegateAdapter<PromoItem, PromoAccordionItemDelegateAdapter.ViewHolder>(PromoItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: PromoUsageItemVoucherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromoItem) {
            if (item.isExpanded) {
                binding.voucherView.bind(item)
            }
            binding.root.setOnClickListener {
                if (item.state is PromoItemState.Normal || item.state is PromoItemState.Selected) {
                    onClickPromo(item)
                }
            }
            binding.voucherView.isVisible = item.isExpanded && item.isVisible
        }
    }
}
