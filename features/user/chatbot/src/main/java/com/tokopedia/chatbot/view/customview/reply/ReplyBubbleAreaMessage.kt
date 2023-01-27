package com.tokopedia.chatbot.view.customview.reply

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.util.getTextFromHtml
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.toPx

class ReplyBubbleAreaMessage : ConstraintLayout {

    private var title: TextView? = null
    private var description: TextView? = null
    private var closeBtn: ImageView? = null
    private var replyIcon: IconUnify? = null
    var referredMsg: ParentReply? = null
    var listener: Listener? = null
        private set

    interface Listener {
        fun getUserName(): String
        fun showReplyOption(messageUiModel: MessageUiModel)
        fun goToBubble(parentReply: ParentReply)
        fun resetGuidelineForReplyBubble()
    }

    fun setReplyListener(listener: Listener) {
        this.listener = listener
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val bgRight = ViewUtil.generateBackgroundWithShadow(
        view = this,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_GN100,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        elevation = R.dimen.dp_chatbot_2,
        shadowRadius = R.dimen.dp_chatbot_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_GN50,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )

    private val bgLeft = ViewUtil.generateBackgroundWithShadow(
        view = this,
        backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_N50,
        topLeftRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        topRightRadius = com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4,
        bottomLeftRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        bottomRightRadius = com.tokopedia.unifyprinciples.R.dimen.unify_space_0,
        shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        elevation = R.dimen.dp_chatbot_2,
        shadowRadius = R.dimen.dp_chatbot_1,
        shadowGravity = Gravity.CENTER,
        strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_N0,
        strokeWidth = getStrokeWidthSenderDimenRes()
    )

    init {
        initLayout()
        initViewBinding()
        updateCloseButtonState(true)
    }

    private fun initLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        title = findViewById(R.id.reply_from)
        description = findViewById(R.id.reply_message)
        closeBtn = findViewById(R.id.close_btn)
        replyIcon = findViewById(R.id.reply_icon)
    }

    fun updateReplyButtonState(toShow: Boolean) {
        if (toShow) {
            replyIcon?.show()
        } else {
            replyIcon?.hide()
        }
        if (replyIcon?.isVisible == true) {
            title?.setMargin(
                left = 8.toPx(),
                top = 10.toPx(),
                right = 15.toPx(),
                bottom = 0
            )
        }
    }

    private fun bindParentReplyData(parentReply: ParentReply, message: String, from: String?) {
        referTo(parentReply)
        setTitle(from)
        setReplyMsg(message)
    }

    private fun bindClick(parentReply: ParentReply) {
        setOnClickListener {
            if (!parentReply.isExpired) {
                listener?.goToBubble(parentReply)
            }
        }
    }

    private fun referTo(parentReply: ParentReply) {
        referredMsg = parentReply
    }

    // Requirement from BE, Changing this value will break the UI
    private fun setTitle(sender: String?) {
        sender ?: return
        if (sender == ChatbotConstant.TANYA) {
            title?.text = ChatbotConstant.TOKOPEDIA_CARE
        } else {
            title?.text = sender
        }
    }

    private fun setReplyMsg(msg: String?) {
        msg ?: return
        description?.text = MethodChecker.fromHtml(msg)
    }

    fun updateCloseButtonState(enableCloseButton: Boolean) {
        if (enableCloseButton) {
            closeBtn?.show()
            closeBtn?.setOnClickListener {
                clearReferTo()
                listener?.resetGuidelineForReplyBubble()
                hide()
            }
        } else {
            closeBtn?.hide()
        }
    }

    private fun clearReferTo() {
        referredMsg = null
    }

    private fun getStrokeWidthSenderDimenRes(): Int {
        return R.dimen.dp_chatbot_2
    }

    fun updateBackground(orientation: Int) {
        if (orientation == LEFT_ORIENTATION) {
            background = bgLeft
        } else if (orientation == RIGHT_ORIENTATION) {
            background = bgRight
        } else {
            background = bgRight
        }
    }

    fun composeMsg(title: String?, msg: String?, parentReply: ParentReply?) {
        setTitle(title)
        setReplyMsg(msg)
        if (parentReply != null) {
            bindClick(parentReply)
        }
    }

    fun composeReplyData(
        referredMsg: BaseChatUiModel,
        text: CharSequence,
        enableCloseButton: Boolean = false,
        userName: String?
    ) {
        val parentReply = ParentReply(
            attachmentId = referredMsg.attachmentId,
            attachmentType = referredMsg.attachmentType,
            senderId = referredMsg.fromUid ?: "",
            replyTime = referredMsg.replyTime ?: "",
            mainText = referredMsg.message.getTextFromHtml(),
            subText = "",
            imageUrl = referredMsg.getReferredImageUrl(),
            localId = referredMsg.localId,
            source = "chat",
            replyId = referredMsg.replyId,
            name = userName ?: ""
        )
        bindParentReplyData(parentReply, referredMsg.message, userName)
        updateCloseButtonState(enableCloseButton)
        show()
    }

    private fun renderMessageText(message: String): String {
        return getTextFromHtml(message).toString()
    }

    private fun getTextFromHtml(htmlText: String?): CharSequence {
        val charSequence: CharSequence = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlText)
        }
        return charSequence
    }

    companion object {
        val LAYOUT = R.layout.customview_chatbot_reply_bubble
        const val LEFT_ORIENTATION = 0
        const val RIGHT_ORIENTATION = 1
    }
}
