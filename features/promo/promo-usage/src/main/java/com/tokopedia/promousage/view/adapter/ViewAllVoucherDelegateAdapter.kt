package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherViewAllBinding
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class ViewAllVoucherDelegateAdapter(
    private val section: PromoAccordionItem,
    private val onViewAllVoucherClick: (PromoAccordionItem) -> Unit
) : DelegateAdapter<PromoAccordionViewAllItem, ViewAllVoucherDelegateAdapter.ViewHolder>(PromoAccordionViewAllItem::class.java) {


    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherViewAllBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoAccordionViewAllItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherViewAllBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onViewAllVoucherClick(section) }
        }

        fun bind(voucher: PromoAccordionViewAllItem) {
            binding.tpgViewAll.text = binding.tpgViewAll.context.getString(
                R.string.promo_voucher_placeholder_view_all_voucher,
                voucher.promoCount
            )
        }
    }

}
