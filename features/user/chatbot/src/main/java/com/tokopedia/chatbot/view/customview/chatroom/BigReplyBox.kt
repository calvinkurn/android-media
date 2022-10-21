package com.tokopedia.chatbot.view.customview.chatroom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.unifycomponents.CardUnify

class BigReplyBox (context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var replyBox: CardUnify? = null
    private var addAttachmentMenu: ImageView? = null
    private var sendButton: ImageView? = null
    private var parentLayout: ConstraintLayout? = null
    private var replyBoxText: com.tokopedia.unifyprinciples.Typography? = null

    var sendButtonListener : ChatbotSendButtonListener? = null
    var replyBoxClickListener : ReplyBoxClickListener? = null

    init {
        initViewBindings()
        disableSendButton()
        bindClickListeners()
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            replyBox = findViewById(R.id.reply_box)
            parentLayout = findViewById(R.id.parent)
            addAttachmentMenu = findViewById(R.id.iv_chat_menu)
            sendButton = findViewById(R.id.send_but)
            replyBoxText = findViewById(R.id.reply_box_text)
        }
    }

    private fun bindClickListeners() {
        addAttachmentMenu?.setOnClickListener {
            replyBoxClickListener?.onAttachmentMenuClicked()
        }
        replyBox?.setOnClickListener {
            replyBoxClickListener?.goToBigReplyBoxBottomSheet()
        }
    }

    private fun disableSendButton() {
        sendButtonListener?.disableSendButton()
        sendButton?.setImageResource(R.drawable.ic_chatbot_send_deactivated)
    }

    fun enableSendButton() {
        sendButtonListener?.enableSendButton()
        sendButton?.setImageResource(R.drawable.ic_chatbot_send)
    }

    fun setText(text: String) {
        replyBoxText?.text = text
    }

    companion object {
        val LAYOUT = R.layout.chatbot_big_reply_box
    }
}


