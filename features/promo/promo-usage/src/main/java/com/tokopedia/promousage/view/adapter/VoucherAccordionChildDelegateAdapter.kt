package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding
import com.tokopedia.promousage.domain.entity.VoucherState
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.domain.entity.list.Voucher

class VoucherAccordionChildDelegateAdapter(
    private val onVoucherClick: (Voucher) -> Unit,
    private val onHyperlinkClick: (String) -> Unit,
) : DelegateAdapter<Voucher, VoucherAccordionChildDelegateAdapter.ViewHolder>(Voucher::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: Voucher, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(voucher: Voucher) {
            if (voucher.visible) {
                binding.voucherView.bind(voucher)
                binding.voucherView.setOnHyperlinkTextClick {
                    if (voucher.voucherState is VoucherState.Actionable) {
                        onHyperlinkClick(voucher.voucherState.appLink)
                    }
                }
            }

            binding.voucherView.isVisible = voucher.visible

            binding.root.setOnClickListener { onVoucherClick(voucher) }
        }
    }

}
