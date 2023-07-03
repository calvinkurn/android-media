package com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment

import android.view.View
import android.widget.ImageView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.SendableVoucherPreviewUiModel
import com.tokopedia.topchat.databinding.ItemVoucherPreviewBinding
import com.tokopedia.utils.view.binding.viewBinding

class VoucherPreviewViewHolder(
    itemView: View,
    attachmentItemPreviewListener: AttachmentItemPreviewListener
) : AttachmentPreviewViewHolder<SendableVoucherPreviewUiModel>(
    itemView,
    attachmentItemPreviewListener
) {

    private val binding: ItemVoucherPreviewBinding? by viewBinding()

    override fun getButtonView(itemView: View): ImageView? {
        return binding?.ivClose
    }

    override fun bind(model: SendableVoucherPreviewUiModel) {
        super.bind(model)
        bindVoucherView(model)
    }

    private fun bindVoucherView(model: SendableVoucherPreviewUiModel) {
        val voucherModel = model.merchantVoucherViewModel
        binding?.voucher?.setData(voucherModel, false)
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_preview
    }
}
