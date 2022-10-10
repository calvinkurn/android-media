package com.tokopedia.chatbot.view.customview.chatroom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.unifycomponents.TextFieldUnify2

class BigReplyBox (context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var replyBox: TextFieldUnify2? = null
    private var addAttachmentMenu: ImageView? = null
    private var sendButton: ImageView? = null
    private var parentLayout: ConstraintLayout? = null

    var sendButtonListener : ChatbotSendButtonListener? = null

    init {
        initViewBindings()
        disableSendButton()
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            replyBox = findViewById(R.id.chat_text)
            parentLayout = findViewById(R.id.parent)
            addAttachmentMenu = findViewById(R.id.iv_chat_menu)
            sendButton = findViewById(R.id.send_but)
        }
    }

    fun disableSendButton() {
        sendButtonListener?.disableSendButton()
        sendButton?.setImageResource(R.drawable.ic_chatbot_send_deactivated)
    }

    fun enableSendButton() {
        sendButtonListener?.enableSendButton()
        sendButton?.setImageResource(R.drawable.ic_chatbot_send)
    }

    companion object {
        val LAYOUT = R.layout.chatbot_big_reply_box
    }
}
