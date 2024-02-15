package com.tokopedia.chatbot.chatbot2.view.customview.chatroom.replybox

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.SlowModeSendButton
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.toPx

class SmallReplyBox(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var replyBox: ConstraintLayout? = null
    var replyBubbleContainer: ReplyBubbleAreaMessage? = null
        private set
    var commentEditText: TextAreaUnify2? = null
        private set
    var addAttachmentMenu: ImageView? = null
        private set
    var guideline: Guideline? = null
        private set
    var sendButton: SlowModeSendButton? = null
        private set
    var replyBoxClickListener: ReplyBoxClickListener? = null

    private var textWatcher: TextWatcher? = null
    var listener: ChatbotSendButtonListener? = null

    init {
        initViewBindings()
        textWatcher = getTextWatcherForMessage()
    }

    fun getMessageView(): EditText? {
        return commentEditText?.editText
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            replyBox = findViewById(R.id.reply_box)
            replyBubbleContainer = findViewById(R.id.reply_bubble_container)
            commentEditText = findViewById(R.id.new_comment)
            setEditTextPadding()
            addAttachmentMenu = commentEditText?.icon1
            sendButton = findViewById(R.id.send_but)
            guideline = findViewById(R.id.guideline_reply_bubble)
        }
        context?.resources?.getString(R.string.cb_bot_reply_text)?.toBlankOrString()
            ?.let { setHint(it) }
        addAttachmentMenu?.setOnClickListener {
            replyBoxClickListener?.onAttachmentMenuClicked()
        }
    }

    private fun setEditTextPadding() {
        commentEditText?.editText?.apply {
            this.setPadding(
                this.paddingLeft,
                this.paddingTop,
                ICON_1_PADDING.toPx() + this.paddingRight,
                this.paddingBottom
            )
        }
    }

    fun setHint(hint: String) {
        with(commentEditText) {
            this?.labelText?.text = ""
            this?.labelText?.hide()
            this?.setPlaceholder(hint)
        }
    }

    fun showCoachMark(videoUploadOnBoarding: VideoUploadOnBoarding) {
        videoUploadOnBoarding.showVideoBubbleOnBoarding(
            addAttachmentMenu,
            context
        )
    }

    fun clearChatText() {
        commentEditText?.editText?.setText("")
    }

    fun addTextChangedListener() {
        commentEditText?.editText?.addTextChangedListener(textWatcher)
    }

    fun removeTextChangedListener() {
        commentEditText?.editText?.removeTextChangedListener(textWatcher)
    }

    fun getMessage(): String {
        return commentEditText?.editText?.text.toString().orEmpty()
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
        val LAYOUT = R.layout.customview_chatbot_small_reply_box_2

        const val ICON_1_PADDING = 32
    }
}
