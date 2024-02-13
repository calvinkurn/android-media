package com.tokopedia.topchat.chatroom.view.adapter.viewholder.ordercancellation

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomBubbleBackgroundGenerator
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopChatRoomOrderCancellationListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomOrderCancellationUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomOrderCancellationItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatRoomOrderCancellationViewHolder(
    itemView: View,
    private val adapterListener: AdapterListener,
    orderCancellationListener: TopChatRoomOrderCancellationListener
) : BaseChatViewHolder<TopChatRoomOrderCancellationUiModel>(itemView) {

    private val binding: TopchatChatroomOrderCancellationItemBinding? by viewBinding()
    private var uiModel: TopChatRoomOrderCancellationUiModel? = null

    private val bgLeft by lazy {
        TopChatRoomBubbleBackgroundGenerator.generateLeftBg(binding?.topchatChatroomLayoutOrderCancellation)
    }
    private val bgRight by lazy {
        TopChatRoomBubbleBackgroundGenerator.generateRightBg(binding?.topchatChatroomLayoutOrderCancellation)
    }

    private val topMarginOpposite: Int = getOppositeMargin(itemView.context)
    private val bubbleToScreenMargin: Int = 12.dpToPx(itemView.resources.displayMetrics)

    init {
        binding?.topchatChatroomLayoutOrderCancellation?.setOnClickListener {
            orderCancellationListener.onClickOrderCancellationWidget(uiModel)
        }
    }

    override fun bind(uiModel: TopChatRoomOrderCancellationUiModel) {
        this.uiModel = uiModel
        super.bind(uiModel)
        bindTitle(uiModel)
        bindMargin(uiModel)
        if (uiModel.isSender) {
            // Right msg
            bindSenderBackground()
        } else {
            // Left msg
            bindReceiverBackground()
        }
    }

    private fun bindTitle(uiModel: TopChatRoomOrderCancellationUiModel) {
        binding?.topchatChatroomLayoutOrderCancellation?.setText(uiModel.title)
    }

    private fun bindMargin(uiModel: TopChatRoomOrderCancellationUiModel) {
        if (adapterListener.isOpposite(absoluteAdapterPosition, uiModel.isSender)) {
            binding?.topchatChatroomLlContainerOrderCancellation?.setMargin(0, topMarginOpposite, 0, 0)
        } else {
            binding?.topchatChatroomLlContainerOrderCancellation?.setMargin(0, 0, 0, 0)
        }
    }

    private fun bindSenderBackground() {
        bindMsgGravity(Gravity.END)
        paddingRightMsg()
        bindBackground(bgRight)
    }

    private fun bindReceiverBackground() {
        bindMsgGravity(Gravity.START)
        paddingLeftMsg()
        bindBackground(bgLeft)
    }

    private fun bindMsgGravity(gravity: Int) {
        bindLayoutGravity(gravity)
        bindGravity(gravity)
    }

    private fun paddingRightMsg() {
        binding?.topchatChatroomLlContainerOrderCancellation?.let {
            it.setPadding(
                0,
                it.paddingTop,
                bubbleToScreenMargin,
                it.paddingBottom
            )
        }
    }

    private fun paddingLeftMsg() {
        binding?.topchatChatroomLlContainerOrderCancellation?.let {
            it.setPadding(
                bubbleToScreenMargin,
                it.paddingTop,
                0,
                it.paddingBottom
            )
        }
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = binding?.topchatChatroomLayoutOrderCancellation?.layoutParams as LinearLayout.LayoutParams
        containerLp.gravity = gravity
        binding?.topchatChatroomLayoutOrderCancellation?.layoutParams = containerLp
    }

    private fun bindGravity(gravity: Int) {
        binding?.topchatChatroomLlContainerOrderCancellation?.gravity = gravity
    }

    private fun bindBackground(drawable: Drawable?) {
        binding?.topchatChatroomLayoutOrderCancellation?.background = drawable
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topchat_chatroom_order_cancellation_item
    }
}
