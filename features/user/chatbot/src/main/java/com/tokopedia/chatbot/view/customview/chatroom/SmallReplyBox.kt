package com.tokopedia.chatbot.view.customview.chatroom

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
import com.tokopedia.chatbot.chatbot2.view.customview.chatroom.listener.ReplyBoxClickListener
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import com.tokopedia.chatbot.view.listener.ChatbotSendButtonListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.TextAreaUnify2

class SmallReplyBox(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var replyBox: ConstraintLayout? = null
    private var replyBubbleContainer: ReplyBubbleAreaMessage? = null
    private var commentContainer: LinearLayout? = null
    var commentEditText: TextAreaUnify2? = null
    private var addAttachmentMenu: ImageView? = null
    private var guideline: Guideline? = null
    var sendButton: ImageView? = null
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

    fun getReplyBubbleContainer(): ReplyBubbleAreaMessage? {
        return replyBubbleContainer
    }

    fun getGuidelineForReplyBubble(): Guideline? {
        return guideline
    }

    fun getSmallReplyBoxSendButton(): ImageView? {
        return sendButton
    }

    fun getAddAttachmentMenu(): ImageView? {
        return addAttachmentMenu
    }

    private fun initViewBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            replyBox = findViewById(R.id.reply_box)
            replyBubbleContainer = findViewById(R.id.reply_bubble_container)
            commentEditText = findViewById(R.id.new_comment)
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

    fun bindCommentTextBackground() {
        val replyEditTextBg = ViewUtil.generateBackgroundWithShadow(
            commentContainer,
            R.color.chatbot_dms_left_message_bg,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
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
        return commentEditText?.editText?.text.toString() ?: ""
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
