package com.tokopedia.attachvoucher.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import kotlinx.android.synthetic.main.item_attach_voucher.view.*

class AttachVoucherViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<VoucherUiModel>(itemView) {

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
        itemView.title?.text = voucher.voucherName
    }

    private fun bindVoucherStatus(voucher: VoucherUiModel) {
        val validDate = DateFormatUtils.getFormattedDate(voucher.validThru, "dd MMM yyyy")
        val status = itemView.context?.getString(R.string.desc_attachvoucher_status, validDate)

        itemView.validStatus?.text = status
    }

    private fun bindVoucherView(voucher: VoucherUiModel) {
        val voucherModel = MerchantVoucherViewModel(voucher)
        itemView.voucher?.setData(voucherModel, hasActionButton = false)
    }

    private fun bindClick(voucher: VoucherUiModel) {
        itemView.clContainer?.setOnClickListener {
            toggle(voucher)
        }
    }

    private fun toggle(voucher: VoucherUiModel) {
        itemView.rbSelect?.apply {
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

    private fun stateChecked() {
        val color = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G100)
        itemView.clContainer?.setBackgroundColor(color)
        itemView.rbSelect?.isChecked = true
    }

    private fun stateUnchecked() {
        itemView.clContainer?.setBackgroundColor(Color.TRANSPARENT)
        itemView.rbSelect?.isChecked = false
    }

    companion object {
        val LAYOUT = R.layout.item_attach_voucher

        const val PAYLOAD_UNCHECK = "uncheck"
    }
}