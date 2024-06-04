package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.clearImage
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastStatusBinding
import com.tokopedia.chat_common.R as chat_commonR

class TopChatRoomBroadcastStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TopchatChatroomBroadcastStatusBinding

    init {
        binding = TopchatChatroomBroadcastStatusBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    fun bindStatus(uiModel: TopChatRoomBroadcastUiModel) {
        val message = uiModel.messageUiModel
        if (message != null) {
            val shouldShowStatusIcon = message.isShowTime && message.isSender && !message.isDeleted()
            val shouldShowTime = message.isShowTime && !message.isDeleted()
            binding.apply {
                topchatChatroomBroadcastIvStatus.apply {
                    if (shouldShowStatusIcon) {
                        show()
                        val imageResource = when {
                            message.isDummy -> chat_commonR.drawable.ic_chatcommon_check_rounded_grey
                            !message.isRead -> chat_commonR.drawable.ic_chatcommon_check_sent_rounded_grey
                            else -> chat_commonR.drawable.ic_chatcommon_check_read_rounded_green
                        }
                        setImageDrawable(MethodChecker.getDrawable(context, imageResource))
                    } else {
                        hide()
                    }
                }

                topchatChatroomBroadcastTvStatus.apply {
                    if (shouldShowTime) {
                        show()
                        text = ChatTimeConverter.getHourTime(message.replyTime)
                    } else {
                        hide()
                    }
                }
            }
        } else {
            binding.topchatChatroomBroadcastIvStatus.hide()
        }
    }

    fun cleanUp() {
        binding.topchatChatroomBroadcastIvStatus.clearImage()
    }
}
