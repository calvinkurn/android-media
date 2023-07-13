package com.tokopedia.promousage.view.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding

class VoucherDelegateAdapter(
    private val onVoucherClick: (Voucher) -> Unit
) : DelegateAdapter<Voucher, VoucherDelegateAdapter.ViewHolder>(Voucher::class.java) {

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
            if (voucher.shouldVisible) {
                binding.voucherView.bind(voucher)
            }

            binding.voucherView.isVisible = voucher.shouldVisible

            binding.root.setOnClickListener { onVoucherClick(voucher) }
        }
    }
}
