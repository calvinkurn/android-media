package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.data.DummyData
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherCodeBinding
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.util.composite.DelegateAdapter

class PromoInputCodeDelegateAdapter(
    private val onApplyVoucherCodeCtaClick: () -> Unit
) : DelegateAdapter<PromoInputItem, PromoInputCodeDelegateAdapter.ViewHolder>(
    PromoInputItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherCodeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoInputItem, viewHolder: ViewHolder) {
        viewHolder.bind()
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onApplyVoucherCodeCtaClick() }
        }

        fun bind() {
            handleVoucherFound()
        }

        private fun handleVoucherFound() {
            binding.run {
                userInputVoucherView.visible()
                userInputVoucherView.bind(DummyData.attemptedPromo)
            }
        }

        private fun handleVoucherError() {
            binding.userInputVoucherView.gone()
        }
    }
}
