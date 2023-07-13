package com.tokopedia.promousage.view.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoCheckoutItemVoucherViewAllBinding
import com.tokopedia.promousage.R

class ViewAllVoucherDelegateAdapter(
    private val sectionIndex: Int,
    private val onViewAllVoucherClick: (Int) -> Unit
) : DelegateAdapter<ViewAllVoucher, ViewAllVoucherDelegateAdapter.ViewHolder>(ViewAllVoucher::class.java) {


    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoCheckoutItemVoucherViewAllBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: ViewAllVoucher, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoCheckoutItemVoucherViewAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onViewAllVoucherClick(sectionIndex) }
        }

        fun bind(voucher: ViewAllVoucher) {
            binding.tpgViewAll.text = binding.tpgViewAll.context.getString(
                R.string.promo_voucher_placeholder_view_all_voucher,
                voucher.collapsedVoucherCount
            )

        }
    }

}
