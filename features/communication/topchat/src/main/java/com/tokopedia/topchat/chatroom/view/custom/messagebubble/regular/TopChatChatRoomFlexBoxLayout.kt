package com.tokopedia.topchat.chatroom.view.custom.messagebubble.regular

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout


class TopChatChatRoomFlexBoxLayout : BaseTopChatFlexBoxChatLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return LAYOUT
    }

    override fun bindAdditionalView(view: View) {
        // no-op
    }

    companion object {
        private val LAYOUT = R.layout.topchat_chatroom_partial_flexbox_chat_bubble
    }
}
