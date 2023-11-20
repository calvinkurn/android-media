package com.tokopedia.tokochat.common.view.chatroom.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.MAX_DISPLAYED_OFFSET
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.MAX_DISPLAYED_STRING
import com.tokopedia.tokochat.common.util.TokoChatViewUtil
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.ELEVEN_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.FORTY_EIGHT_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.ONE_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.TEN_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.TWENTY_DP
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.ZERO_DP
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatReplyAreaListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatTypingListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import java.text.NumberFormat
import java.util.*

class TokoChatReplyMessageView : ConstraintLayout, LifecycleObserver {

    var composeArea: EditText? = null
    private var errorComposeMsg: Typography? = null
    private var attachmentButton: IconUnify? = null

    private var textWatcher: TokoChatMessageTextWatcher? = null
    private var sendButtonTextWatcher: TokoChatReplyButtonWatcher? = null
    private var bgComposeArea: Drawable? = null

    private val numberFormat = NumberFormat.getInstance(Locale("in", "ID"))

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        initViewLayout()
        initViewBind()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBind() {
        composeArea = findViewById(R.id.tokochat_tf_new_comment)
        errorComposeMsg = findViewById(R.id.tokochat_tv_error_message)
        attachmentButton = findViewById(R.id.tokochat_icon_chat_menu)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanUpReference() {
        composeArea?.removeTextChangedListener(textWatcher)
        composeArea?.removeTextChangedListener(sendButtonTextWatcher)
    }

    fun initLayout(
        typingListener: TokoChatTypingListener?,
        replyTextListener: TokoChatReplyTextListener?,
        replyAreaListener: TokoChatReplyAreaListener?
    ) {
        initComposeMsgListener(typingListener, replyTextListener)
        initComposeBackground()
        setDefaultComposeBackground()
        initReplyAreaListener(replyAreaListener)
    }

    fun onSendMessage() {
        textWatcher?.cancelJob()
    }

    fun getComposedText(): String {
        return composeArea?.text?.toString() ?: ""
    }

    private fun setDefaultComposeBackground() {
        val paddingTop = ELEVEN_DP.toPx()
        val paddingBottom = TEN_DP.toPx()
        val paddingEnd = FORTY_EIGHT_DP.toPx()
        composeArea?.background = bgComposeArea
        composeArea?.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    }

    private fun initComposeBackground() {
        bgComposeArea = TokoChatViewUtil.generateBackgroundWithShadow(
            view = composeArea,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            topLeftRadiusValue = TWENTY_DP,
            topRightRadiusValue = TWENTY_DP,
            bottomLeftRadiusValue = TWENTY_DP,
            bottomRightRadiusValue = TWENTY_DP,
            shadowColor = com.tokopedia.unifyprinciples.R.color.Unify_Static_Black_20,
            elevationValue = ONE_DP,
            shadowRadiusValue = ONE_DP,
            shadowGravity = Gravity.NO_GRAVITY,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_NN50,
            strokeWidthValue = ONE_DP,
            // Hide the top shadow
            shadowTop = ZERO_DP,
            isInsetElevation = false
        )
    }

    private fun initComposeMsgListener(
        typingListener: TokoChatTypingListener?,
        replyTextListener: TokoChatReplyTextListener?
    ) {
        typingListener?.let {
            textWatcher = TokoChatMessageTextWatcher(typingListener)
            composeArea?.addTextChangedListener(textWatcher)
        }
        replyTextListener?.let {
            sendButtonTextWatcher = TokoChatReplyButtonWatcher(
                it,
                object : TokoChatReplyButtonWatcher.Listener {
                    override fun onComposeTextLimitExceeded(offset: Int) {
                        val formattedOffset = getFormattedOffset(offset)
                        errorComposeMsg?.show()
                        errorComposeMsg?.text = context?.getString(
                            R.string.tokochat_desc_max_char_exceeded,
                            formattedOffset
                        ) ?: ""
                        composeArea?.setBackgroundResource(R.drawable.bg_tokochat_error_too_long_msg)
                    }

                    override fun hideTextLimitError() {
                        errorComposeMsg?.hide()
                        setDefaultComposeBackground()
                    }
                }
            )
            composeArea?.addTextChangedListener(sendButtonTextWatcher)
        }
        composeArea?.onFocusChangeListener = OnFocusChangeListener { _, isFocused ->
            replyTextListener?.onReplyAreaFocusChanged(isFocused)
        }
    }

    private fun initReplyAreaListener(
        replyAreaListener: TokoChatReplyAreaListener?
    ) {
        attachmentButton?.showWithCondition(
            replyAreaListener?.shouldShowAttachmentButton() == true
        )
        attachmentButton?.setOnClickListener {
            replyAreaListener?.onClickAttachmentButton()
        }
        composeArea?.clearFocus()
        composeArea?.setOnFocusChangeListener { _, _ ->
            replyAreaListener?.trackClickComposeArea()
        }
    }

    private fun getFormattedOffset(offset: Int): String {
        return if (offset > MAX_DISPLAYED_OFFSET) {
            MAX_DISPLAYED_STRING
        } else {
            numberFormat.format(offset)
        }
    }

    fun setTracker(trackOnClickComposeArea: () -> Unit) {
        composeArea?.setOnFocusChangeListener { _, _ ->
            trackOnClickComposeArea()
        }
    }

    companion object {
        val LAYOUT = R.layout.tokochat_partial_message_area
    }
}
