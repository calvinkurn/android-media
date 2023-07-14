package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherCodeBinding
import com.tokopedia.promousage.domain.entity.VoucherSource
import com.tokopedia.promousage.domain.entity.VoucherState
import com.tokopedia.promousage.domain.entity.VoucherType
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.domain.entity.list.VoucherCode
import com.tokopedia.promousage.util.composite.DelegateAdapter

class VoucherCodeDelegateAdapter(
    private val onApplyVoucherCodeCtaClick: () -> Unit
) : DelegateAdapter<VoucherCode, VoucherCodeDelegateAdapter.ViewHolder>(VoucherCode::class.java) {


    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherCodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: VoucherCode, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onApplyVoucherCodeCtaClick() }
        }

        fun bind(voucher: VoucherCode) {
            handleVoucherFound()
        }

        private fun handleVoucherFound() {
            val voucher = Voucher(
                100,
                100_000,
                "Cashback - Voucher Code User Input",
                "2 hari",
                "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
                VoucherType.CASHBACK,
                VoucherState.Selected,
                VoucherSource.UserInput("TOKOPEDIAXBCA"),
                false
            )

            binding.run {
                userInputVoucherView.visible()
                userInputVoucherView.bind(voucher)
            }
        }

        private fun handleVoucherError() {
            binding.userInputVoucherView.gone()
        }
    }


}
