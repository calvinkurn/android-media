package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemPromoTncBinding
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.toSpannableHtmlString

internal class PromoTncDelegateAdapter(
    private val onClickPromoTnc: (PromoTncItem) -> Unit
) : DelegateAdapter<PromoTncItem, PromoTncDelegateAdapter.ViewHolder>(PromoTncItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemPromoTncBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoTncItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    override fun bindViewHolder(
        item: PromoTncItem,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bind(item)
    }

    internal inner class ViewHolder(
        private val binding: PromoUsageItemPromoTncBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromoTncItem) {
            with(binding) {
                val tncLabel = tpgTncLabel.context.getString(R.string.promo_usage_label_view_tnc)
                tpgTncLabel.text = tncLabel.toSpannableHtmlString(tpgTncLabel.context)
                tpgTncLabel.setOnClickListener {
                    onClickPromoTnc(item)
                }
            }
        }
    }
}
