package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
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
            LayoutInflater.from(context), this)
    }

    fun bind(uiModel: TopChatRoomBroadcastUiModel) {
        bindBannerAttachment(uiModel)
        bindCountdown(uiModel)
        bindMessageAttachment(uiModel)
    }

    private fun bindBannerAttachment(uiModel: TopChatRoomBroadcastUiModel) {
        val bannerAttachment = uiModel.banner
        if (bannerAttachment != null) {
            binding.topchatChatroomBroadcastIvBanner.show()
            binding.topchatChatroomBroadcastIvBanner.loadImageWithoutPlaceholder(bannerAttachment.imageUrl)
        } else {
            binding.topchatChatroomBroadcastIvBanner.hide()
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

    fun cleanUp() {
        binding.topchatChatroomBroadcastIvBanner.clearImage()
    }
}
