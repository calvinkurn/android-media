package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherSectionBinding
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.domain.entity.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class VoucherAccordionDelegateAdapter(
    private val onSectionClick: (PromoAccordionItem) -> Unit,
    private val onVoucherClick: (PromoItem) -> Unit,
    private val onViewAllVoucherClick: (PromoAccordionItem) -> Unit
) : DelegateAdapter<PromoAccordionItem, VoucherAccordionDelegateAdapter.ViewHolder>(PromoAccordionItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherSectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: PromoAccordionItem,
        viewHolder: VoucherAccordionDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: PromoAccordionItem) {
            bindChevron(section)
            bindVouchers(section, onVoucherClick, onViewAllVoucherClick)
        }

        private fun bindChevron(section: PromoAccordionItem) {
            binding.tpgSectionTitle.text = section.title
            val iconDrawable = if (section.isExpanded) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            }

            binding.iconChevron.setImage(iconDrawable)

        }

        private fun bindVouchers(
            section: PromoAccordionItem,
            onVoucherClick: (PromoItem) -> Unit,
            onViewAllVoucherClick: (PromoAccordionItem) -> Unit
        ) {
            val voucherAdapter = CompositeAdapter.Builder()
                .add(VoucherAccordionChildDelegateAdapter(onVoucherClick))
                .add(ViewAllVoucherDelegateAdapter(section, onViewAllVoucherClick))
                .build()


            binding.childRecyclerView.apply {
                layoutManager = LinearLayoutManager(binding.childRecyclerView.context)
                adapter = voucherAdapter
            }

            binding.childRecyclerView.isVisible = section.isExpanded
            binding.root.setOnClickListener { onSectionClick(section) }

            voucherAdapter.submit(section.sections)
        }

    }
}
