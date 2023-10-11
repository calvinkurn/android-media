package com.tokopedia.topchat.chatroom.view.custom.messagebubble.regular

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatChatroomMessageBubbleLayout
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout


class TopChatChatroomMessageBubbleLayout : BaseTopChatChatroomMessageBubbleLayout {

    private var fxChat: TopChatChatRoomFlexBoxLayout? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun getLayout(): Int {
        return LAYOUT
    }

    override fun getFlexBoxView(): BaseTopChatFlexBoxChatLayout? {
        return fxChat
    }

    override fun initViewBinding() {
        super.initViewBinding()
        fxChat = findViewById(R.id.fxChat)
    }

    companion object {
        private val LAYOUT = R.layout.topchat_chatroom_partial_chat_messsage_bubble
    }
}
