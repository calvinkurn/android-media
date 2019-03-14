package com.tokopedia.topchat.chatroom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactoryImpl
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherList.adapter.viewholder.MerchantVoucherViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ImageDualAnnouncementViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.SecurityInfoChatViewHolder
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.listener.SecurityInfoListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel

open class TopChatTypeFactoryImpl(
        imageAnnouncementListener: ImageAnnouncementListener,
        chatLinkHandlerListener: ChatLinkHandlerListener,
        imageUploadListener: ImageUploadListener,
        productAttachmentListener: ProductAttachmentListener,
        private val imageDualAnnouncementListener: DualAnnouncementListener,
        private val securityInfoListener: SecurityInfoListener,
        private val merchantVoucherViewListener: MerchantVoucherView.OnMerchantVoucherViewListener
) : BaseChatTypeFactoryImpl(
        imageAnnouncementListener,
        chatLinkHandlerListener,
        imageUploadListener,
        productAttachmentListener
), TopChatTypeFactory {


    override fun type(securityInfoViewModel: SecurityInfoViewModel): Int {
        return SecurityInfoChatViewHolder.LAYOUT
    }

    override fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel): Int {
        return ImageDualAnnouncementViewHolder.LAYOUT
    }

    override fun type(merchantVoucherViewModel: MerchantVoucherViewModel): Int {
        return MerchantVoucherViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SecurityInfoChatViewHolder.LAYOUT -> SecurityInfoChatViewHolder(parent, securityInfoListener)
            ImageDualAnnouncementViewHolder.LAYOUT -> ImageDualAnnouncementViewHolder(parent,
                    imageDualAnnouncementListener)
            MerchantVoucherViewHolder.LAYOUT -> MerchantVoucherViewHolder(parent, merchantVoucherViewListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
