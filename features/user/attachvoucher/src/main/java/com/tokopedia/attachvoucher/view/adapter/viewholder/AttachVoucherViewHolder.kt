package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.databinding.ItemAttachVoucherBinding
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.utils.view.binding.viewBinding

class AttachVoucherViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<VoucherUiModel>(itemView) {

    private val binding: ItemAttachVoucherBinding? by viewBinding()

    interface Listener {
        fun checkCurrentItem(element: VoucherUiModel, position: Int)
        fun uncheckPreviousItem()
        fun isChecked(element: VoucherUiModel): Boolean
    }

    override fun bind(voucher: VoucherUiModel) {
        bindState(voucher)
        bindVoucherTitle(voucher)
        bindVoucherStatus(voucher)
        bindVoucherView(voucher)
        bindClick(voucher)
        bindPublicityStatus(voucher)
    }

    override fun bind(element: VoucherUiModel?, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val payload = payloads[0]
            if (payload == PAYLOAD_UNCHECK) stateUnchecked()
        } else {
            super.bind(element, payloads)
        }
    }

    private fun bindState(voucher: VoucherUiModel) {
        if (listener.isChecked(voucher)) {
            stateChecked()
        } else {
            stateUnchecked()
        }
    }

    private fun bindVoucherTitle(voucher: VoucherUiModel) {
        binding?.title?.text = voucher.voucherName
    }

    private fun bindVoucherStatus(voucher: VoucherUiModel) {
        val validDate = DateFormatUtils.getFormattedDate(voucher.validThru, "dd MMM yyyy")
        val status = itemView.context?.getString(
            R.string.desc_attachvoucher_status, validDate, voucher.remainingQuota)

        binding?.validStatus?.text = status
    }

    private fun bindVoucherView(voucher: VoucherUiModel) {
        val voucherModel = MerchantVoucherViewModel(voucher)
        binding?.voucher?.setData(voucherModel, hasActionButton = false)
    }

    private fun bindClick(voucher: VoucherUiModel) {
        binding?.clContainer?.setOnClickListener {
            toggle(voucher)
        }
    }

    private fun bindPublicityStatus(voucher: VoucherUiModel) {
        val status = if (voucher.isPublic == 1) {
            STATUS_PUBLIC
        } else STATUS_PRIVATE
        binding?.voucherPublicityStatus?.setLabel(status)
    }

    private fun toggle(voucher: VoucherUiModel) {
        binding?.let {
            it.rbSelect.apply {
                val checkState = !isChecked
                isChecked = checkState
                if (isChecked) {
                    listener.uncheckPreviousItem()
                    listener.checkCurrentItem(voucher, adapterPosition)
                    stateChecked()
                } else {
                    listener.uncheckPreviousItem()
                }
            }
        }
    }

    private fun stateChecked() {
        val color = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G100)
        binding?.let {
            it.clContainer.setBackgroundColor(color)
            it.rbSelect.isChecked = true
        }
    }

    private fun stateUnchecked() {
        binding?.let {
            it.clContainer.setBackgroundColor(Color.TRANSPARENT)
            it.rbSelect.isChecked = false
        }
    }

    companion object {
        val LAYOUT = R.layout.item_attach_voucher

        const val PAYLOAD_UNCHECK = "uncheck"
        private const val STATUS_PUBLIC = "Publik"
        private const val STATUS_PRIVATE = "Khusus"
    }
}