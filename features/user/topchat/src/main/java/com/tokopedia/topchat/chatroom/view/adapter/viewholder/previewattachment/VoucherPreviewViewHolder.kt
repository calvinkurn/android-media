package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.View
import android.widget.ImageView
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableVoucherPreview
import kotlinx.android.synthetic.main.item_voucher_preview.view.*

class VoucherPreviewViewHolder(itemView: View, attachmentItemPreviewListener: AttachmentItemPreviewListener)
    : AttachmentPreviewViewHolder<SendableVoucherPreview>(itemView, attachmentItemPreviewListener) {

    override fun getButtonView(itemView: View): ImageView? {
        return itemView.iv_close
    }

    override fun bind(model: SendableVoucherPreview) {
        super.bind(model)
        bindVoucherView(model)
    }

    private fun bindVoucherView(model: SendableVoucherPreview) {
        val voucherModel = MerchantVoucherViewModel(model.voucher).apply {
            this.isPublic = model.isPublic()
        }
        itemView.voucher?.setData(voucherModel,
            false, MerchantVoucherView.SOURCE_TOPCHAT)
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_preview
    }
}