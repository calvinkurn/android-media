package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.View
import android.widget.ImageView
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
        val voucherModel = model.merchantVoucherViewModel
        itemView.voucher?.setData(voucherModel, false)
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_preview
    }
}