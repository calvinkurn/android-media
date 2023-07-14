package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherSectionBinding
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.domain.entity.list.VoucherAccordion
import com.tokopedia.promousage.util.composite.DelegateAdapter

class VoucherAccordionDelegateAdapter(
    private val onSectionClick: (Int) -> Unit,
    private val onVoucherClick: (Voucher) -> Unit,
    private val onViewAllVoucherClick: (Int) -> Unit
) : DelegateAdapter<VoucherAccordion, VoucherAccordionDelegateAdapter.ViewHolder>(VoucherAccordion::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherSectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: VoucherAccordion,
        viewHolder: VoucherAccordionDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onSectionClick(bindingAdapterPosition) }
        }

        fun bind(section: VoucherAccordion) {
            bindChevron(section)
            bindVouchers(section, onVoucherClick, onViewAllVoucherClick)
        }

        private fun bindChevron(section: VoucherAccordion) {
            binding.tpgSectionTitle.text = section.title
            val iconDrawable = if (section.isExpanded) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            }

            binding.iconChevron.setImage(iconDrawable)

        }

        private fun bindVouchers(
            section: VoucherAccordion,
            onVoucherClick: (Voucher) -> Unit,
            onViewAllVoucherClick: (Int) -> Unit
        ) {
            val voucherAdapter = CompositeAdapter.Builder()
                .add(VoucherAccordionChildDelegateAdapter(onVoucherClick))
                .add(ViewAllVoucherDelegateAdapter(bindingAdapterPosition, onViewAllVoucherClick))
                .build()


            binding.childRecyclerView.apply {
                layoutManager = LinearLayoutManager(binding.childRecyclerView.context)
                adapter = voucherAdapter
            }

            binding.childRecyclerView.isVisible = section.isExpanded
            voucherAdapter.submit(section.vouchers)
        }

    }
}
