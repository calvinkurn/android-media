package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.attachment

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.databinding.TokochatItemAttachmentMenuBinding
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatAttachmentMenuListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatAttachmentMenuUiModel
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatAttachmentType
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatAttachmentMenuViewHolder(
    itemView: View,
    private val listener: TokoChatAttachmentMenuListener?
) : BaseViewHolder(itemView) {

    private val binding: TokochatItemAttachmentMenuBinding? by viewBinding()

    fun bind(attachmentMenu: TokoChatAttachmentMenuUiModel) {
        setViews(attachmentMenu)
        setOnClickListener(attachmentMenu)
    }

    private fun setViews(attachmentMenu: TokoChatAttachmentMenuUiModel) {
        binding?.tokochatIconAttachmentMenu?.loadImage(attachmentMenu.icon)
        binding?.tokochatTvAttachmentMenuTitle?.text = attachmentMenu.title
    }

    private fun setOnClickListener(attachmentMenu: TokoChatAttachmentMenuUiModel) {
        itemView.setOnClickListener {
            // Prepared for other types of attachment
            when (attachmentMenu.type) {
                TokoChatAttachmentType.IMAGE_ATTACHMENT -> listener?.onClickImageAttachment()
            }
        }
    }
}
