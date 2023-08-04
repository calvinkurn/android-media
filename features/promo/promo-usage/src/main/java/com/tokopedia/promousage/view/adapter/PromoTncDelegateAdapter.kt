package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemTncBinding
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.unifycomponents.HtmlLinkHelper

internal class PromoTncDelegateAdapter(
    private val onClickPromoTnc: (PromoTncItem) -> Unit
) : DelegateAdapter<PromoTncItem, PromoTncDelegateAdapter.ViewHolder>(PromoTncItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemTncBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoTncItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    internal inner class ViewHolder(
        private val binding: PromoUsageItemTncBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromoTncItem) {
            with(binding) {
                val tncLabel = tpgTncLabel.context.getString(R.string.promo_voucher_view_tnc_label)
                tpgTncLabel.text = HtmlLinkHelper(tpgTncLabel.context, tncLabel).spannedString
                tpgTncLabel.setOnClickListener {
                    onClickPromoTnc(item)
                }
            }
        }
    }
}
