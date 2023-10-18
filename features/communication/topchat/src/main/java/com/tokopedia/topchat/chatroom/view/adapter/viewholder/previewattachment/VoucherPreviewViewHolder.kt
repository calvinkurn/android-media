package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.View
import android.widget.ImageView
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.SendableVoucherPreviewUiModel

class VoucherPreviewViewHolder(
    itemView: View,
    attachmentItemPreviewListener: AttachmentItemPreviewListener
) : AttachmentPreviewViewHolder<SendableVoucherPreviewUiModel>(
    itemView,
    attachmentItemPreviewListener
) {

    private val voucher: MerchantVoucherView = itemView.findViewById(R.id.voucher)
    private val btnClose: ImageView = itemView.findViewById(R.id.iv_close)

    override fun getButtonView(itemView: View): ImageView {
        return btnClose
    }

    override fun bind(model: SendableVoucherPreviewUiModel) {
        super.bind(model)
        bindVoucherView(model)
    }

    private fun bindVoucherView(model: SendableVoucherPreviewUiModel) {
        val voucherModel = model.merchantVoucherViewModel
        voucher.setData(voucherModel, false)
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_preview
    }
}
