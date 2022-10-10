package com.tokopedia.chatbot.view.customview.chatroom

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage

class SmallReplyBox (context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val replyBox: ConstraintLayout? = null
    private val replyBubbleContainer: ReplyBubbleAreaMessage? = null
    private val commentContainer: LinearLayout? = null
    private val commentEditText: EditText? = null
    private val addAttachmentMenu: ImageView? = null
    private val sendButton: ImageView? = null

    companion object {
        val LAYOUT = R.layout.compose_message_area
    }
}
