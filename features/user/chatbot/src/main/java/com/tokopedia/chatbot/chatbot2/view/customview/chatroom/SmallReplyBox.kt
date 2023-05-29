package com.tokopedia.chatbot.chatbot2.view.customview.chatroom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener

class SmallReplyBox(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var replyBox: ConstraintLayout? = null
    private var replyBubbleContainer: ReplyBubbleAreaMessage? = null
    private var commentContainer: LinearLayout? = null
    var commentEditText: EditText? = null
    private var addAttachmentMenu: ImageView? = null
    private var guideline: Guideline? = null
    var sendButton: ImageView? = null

    private var textWatcher: TextWatcher? = null
    var listener: ChatbotSendButtonListener? = null

    init {
        initViewBindings()
        textWatcher = getTextWatcherForMessage()
    }

    fun getGuidelineForReplyBubble(): Guideline? {
        return guideline
    }

    fun getSmallReplyBoxSendButton(): ImageView? {
        return sendButton
    }

    fun getReplyBubbleContainer(): ReplyBubbleAreaMessage? {
        return replyBubbleContainer
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            replyBox = findViewById(R.id.reply_box)
            replyBubbleContainer = findViewById(R.id.reply_bubble_container)
            commentContainer = findViewById(R.id.new_comment_container)
            commentEditText = findViewById(R.id.new_comment)
            addAttachmentMenu = findViewById(R.id.iv_chat_menu)
            sendButton = findViewById(R.id.send_but)
            guideline = findViewById(R.id.guideline_reply_bubble)
        }
    }

    fun clearChatText() {
        commentEditText?.setText("")
    }

    fun addTextChangedListener() {
        commentEditText?.addTextChangedListener(textWatcher)
    }

    fun removeTextChangedListener() {
        commentEditText?.removeTextChangedListener(textWatcher)
    }

    fun bindCommentTextBackground() {
        val replyEditTextBg = ViewUtil.generateBackgroundWithShadow(
            commentContainer,
            R.color.chatbot_dms_left_message_bg,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
        )
        val paddingStart = context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)?.toInt() ?: 16
        val paddingEnd = context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8)?.toInt() ?: 48
        val paddingTop = context?.resources?.getDimension(R.dimen.dp_chatbot_11)?.toInt() ?: 11
        val paddingBottom = context?.resources?.getDimension(R.dimen.dp_chatbot_10)?.toInt() ?: 10
        commentContainer?.background = replyEditTextBg
        commentContainer?.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    }

    fun getMessage(): String {
        return commentEditText?.text.toString()
    }

    private fun getTextWatcherForMessage(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (getMessage().isNotEmpty()) {
                    listener?.enableSendButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (getMessage().isEmpty()) {
                    listener?.disableSendButton()
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.customview_chatbot_small_reply_box
    }
}
