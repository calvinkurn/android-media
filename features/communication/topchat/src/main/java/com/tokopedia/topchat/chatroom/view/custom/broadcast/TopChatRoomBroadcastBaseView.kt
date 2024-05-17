package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastBannerListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastBaseBinding

class TopChatRoomBroadcastBaseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TopchatChatroomBroadcastBaseBinding

    init {
        binding = TopchatChatroomBroadcastBaseBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    private var bannerListener: TopChatRoomBroadcastBannerListener? = null
    private var deferredAttachment: DeferredViewHolderAttachment? = null
    private var uiModel: TopChatRoomBroadcastUiModel? = null

    fun setListener(
        bannerListener: TopChatRoomBroadcastBannerListener,
        deferredAttachment: DeferredViewHolderAttachment
    ) {
        this.bannerListener = bannerListener
        this.deferredAttachment = deferredAttachment
        setBannerListener()
    }

    fun bind(uiModel: TopChatRoomBroadcastUiModel) {
        this.uiModel = uiModel
        bindBannerAttachment(uiModel)
        bindCountdown(uiModel)
        bindMessageAttachment(uiModel)
    }

    private fun bindBannerAttachment(uiModel: TopChatRoomBroadcastUiModel) {
        val bannerAttachment = uiModel.banner
        if (bannerAttachment != null) {
            bindSyncBanner(bannerAttachment)
            if (!bannerAttachment.isHideBanner) {
                binding.topchatChatroomBroadcastIvBanner.show()
                binding.topchatChatroomBroadcastIvBanner.loadImage(bannerAttachment.imageUrl)
                impressBannerAttachment(bannerAttachment)
            } else {
                binding.topchatChatroomBroadcastIvBanner.hide()
            }
        } else {
            binding.topchatChatroomBroadcastIvBanner.hide()
        }
    }

    private fun bindSyncBanner(banner: ImageAnnouncementUiModel) {
        if (!banner.isLoading) return
        deferredAttachment?.let {
            val chatAttachments = deferredAttachment?.getLoadedChatAttachments()
            val attachment = chatAttachments?.get(banner.attachmentId) ?: return
            if (attachment is ErrorAttachment) {
                banner.syncError()
            } else {
                banner.updateData(attachment.parsedAttributes)
            }
        }
    }

    private fun impressBannerAttachment(bannerAttachment: ImageAnnouncementUiModel) {
        if (!bannerAttachment.isLoading) {
            binding.topchatChatroomBroadcastIvBanner.addOnImpressionListener(
                bannerAttachment.impressHolder
            ) {
                impressBanner(bannerAttachment)
            }
        }
    }

    private fun impressBanner(bannerUiModel: ImageAnnouncementUiModel) {
        uiModel?.let {
            bannerUiModel.impressHolder
            bannerListener?.onImpressionBroadcastBanner(bannerUiModel, it)
        }
    }

    private fun bindCountdown(uiModel: TopChatRoomBroadcastUiModel) {
        val bannerAttachment = uiModel.banner
        if (bannerAttachment != null) {
            binding.topchatChatroomBroadcastCountdown.show()
            binding.topchatChatroomBroadcastCountdown.renderState(bannerAttachment)
        } else {
            binding.topchatChatroomBroadcastCountdown.hide()
        }
    }

    private fun bindMessageAttachment(uiModel: TopChatRoomBroadcastUiModel) {
        val messageAttachment = uiModel.messageUiModel
        if (messageAttachment != null) {
            binding.topchatChatroomBroadcastTvMessage.show()
            binding.topchatChatroomBroadcastTvMessage.text = messageAttachment.message
        } else {
            binding.topchatChatroomBroadcastTvMessage.hide()
        }
    }

    private fun setBannerListener() {
        binding.topchatChatroomBroadcastIvBanner.setOnClickListener { _ ->
            val banner = uiModel?.banner
            if (banner != null && uiModel != null) {
                bannerListener?.onClickBroadcastBanner(banner, uiModel!!)
            }
        }
    }

    fun cleanUp() {
        binding.topchatChatroomBroadcastIvBanner.clearImage()
    }
}
