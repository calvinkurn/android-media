package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemTncBinding
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherViewAllBinding
import com.tokopedia.promousage.domain.entity.list.TermAndCondition
import com.tokopedia.promousage.domain.entity.list.ViewAllVoucher
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.setHyperlinkText

class TermAndConditionDelegateAdapter(
    private val onTermAndConditionHyperlinkClick : () -> Unit
) : DelegateAdapter<TermAndCondition, TermAndConditionDelegateAdapter.ViewHolder>(TermAndCondition::class.java) {


    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemTncBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: TermAndCondition, viewHolder: ViewHolder) {
        viewHolder.bind()
    }

    inner class ViewHolder(private val binding: PromoUsageItemTncBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tpgTnc.setHyperlinkText(
                fullText = binding.tpgTnc.context.getString(R.string.promo_voucher_view_tnc).orEmpty(),
                hyperlinkSubstring = binding.tpgTnc.context.getString(R.string.promo_voucher_tnc).orEmpty(),
                ignoreCase = true,
                onHyperlinkClick = { onTermAndConditionHyperlinkClick() }
            )
        }
    }

}
