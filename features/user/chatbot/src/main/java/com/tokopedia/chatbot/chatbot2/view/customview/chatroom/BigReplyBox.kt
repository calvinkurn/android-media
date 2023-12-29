package com.tokopedia.chatbot.chatbot2.view.customview.chatroom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.chatbot2.view.listener.ChatbotSendButtonListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.CardUnify

class BigReplyBox(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var replyBox: CardUnify? = null
    private var addAttachmentMenu: ImageView? = null
    private var sendButton: ImageView? = null
    private var parentLayout: ConstraintLayout? = null
    private var isSendButtonEnabled: Boolean = true
    private var replyBoxText: com.tokopedia.unifyprinciples.Typography? = null

    var sendButtonListener: ChatbotSendButtonListener? = null
    var replyBoxClickListener: ReplyBoxClickListener? = null

    init {
        initViewBindings()
        enableSendButton()
        bindClickListeners()
        disableSendButton()
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            replyBox = findViewById(R.id.reply_box)
            parentLayout = findViewById(R.id.parent)
            addAttachmentMenu = findViewById(R.id.iv_chat_menu)
            sendButton = findViewById(R.id.send_button)
            replyBoxText = findViewById(R.id.reply_box_text)
        }
    }

    private fun bindClickListeners() {
        addAttachmentMenu?.setOnClickListener {
            replyBoxClickListener?.onAttachmentMenuClicked()
        }
        replyBox?.setOnClickListener {
            replyBoxClickListener?.goToBigReplyBoxBottomSheet(isError = false)
        }
        sendButton?.setOnClickListener {
            if (isSendButtonEnabled) {
                replyBoxClickListener?.getMessageContentFromBottomSheet(replyBoxText?.text?.toString() ?: "")
            } else {
                replyBoxClickListener?.goToBigReplyBoxBottomSheet(isError = true)
            }
        }
    }

    fun handleAddAttachmentButton(state: Boolean) {
        addAttachmentMenu?.showWithCondition(state)
    }

    fun disableSendButton() {
        sendButtonListener?.disableSendButton()
        isSendButtonEnabled = false
    }

    fun enableSendButton() {
        sendButtonListener?.enableSendButton()
        sendButton?.setImageResource(R.drawable.ic_chatbot_send)
        isSendButtonEnabled = true
    }

    fun setText(text: String) {
        replyBoxText?.text = text
    }

    fun getMessage(): String {
        return replyBoxText?.text?.toString() ?: ""
    }

    fun shouldShowAddAttachmentButton(showAddAttachmentMenu: Boolean) {
        addAttachmentMenu?.showWithCondition(showAddAttachmentMenu)
    }

    companion object {
        val LAYOUT = R.layout.customview_chatbot_big_reply_box
    }
}
