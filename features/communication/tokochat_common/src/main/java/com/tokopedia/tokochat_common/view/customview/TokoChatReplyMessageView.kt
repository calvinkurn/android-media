package com.tokopedia.tokochat_common.view.customview

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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatViewUtil
import com.tokopedia.tokochat_common.view.listener.TokoChatReplyTextListener
import com.tokopedia.tokochat_common.view.listener.TokoChatTypingListener
import com.tokopedia.unifyprinciples.Typography
import java.text.NumberFormat
import java.util.*

class TokoChatReplyMessageView : ConstraintLayout, LifecycleObserver {

    private var composeArea: EditText? = null
    private var errorComposeMsg: Typography? = null

    private var textWatcher: TokoChatMessageTextWatcher? = null
    private var sendButtonTextWatcher: TokoChatReplyButtonWatcher? = null
    private var bgComposeArea: Drawable? = null

    private val numberFormat = NumberFormat.getInstance(Locale("in", "ID"))

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
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanUpReference() {
        composeArea?.removeTextChangedListener(textWatcher)
        composeArea?.removeTextChangedListener(sendButtonTextWatcher)
    }

    fun initLayout(
        typingListener: TokoChatTypingListener?,
        replyTextListener: TokoChatReplyTextListener?
    ) {
        initComposeMsgListener(typingListener, replyTextListener)
        initComposeBackground()
        setDefaultComposeBackground()
    }

    fun onSendMessage() {
        textWatcher?.cancelJob()
    }

    fun getComposedText(): String {
        return composeArea?.text?.toString() ?: ""
    }

    private fun setDefaultComposeBackground() {
        val paddingTop = resources.getDimension(com.tokopedia.tokochat_common.R.dimen.tokochat_11dp).toInt()
        val paddingBottom = resources.getDimension(com.tokopedia.tokochat_common.R.dimen.tokochat_10dp).toInt()
        composeArea?.background = bgComposeArea
        composeArea?.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    }

    private fun initComposeBackground() {
        bgComposeArea = TokoChatViewUtil.generateBackgroundWithShadowBtn(
            view = composeArea,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            topLeftRadius = com.tokopedia.tokochat_common.R.dimen.tokochat_20dp,
            topRightRadius = com.tokopedia.tokochat_common.R.dimen.tokochat_20dp,
            bottomLeftRadius = com.tokopedia.tokochat_common.R.dimen.tokochat_20dp,
            bottomRightRadius = com.tokopedia.tokochat_common.R.dimen.tokochat_20dp,
            shadowColor = com.tokopedia.tokochat_common.R.color.tokochat_dms_chat_bubble_shadow,
            elevation = com.tokopedia.tokochat_common.R.dimen.tokochat_1dp,
            shadowRadius = com.tokopedia.tokochat_common.R.dimen.tokochat_1dp,
            shadowGravity = Gravity.NO_GRAVITY,
            strokeColor = com.tokopedia.unifyprinciples.R.color.Unify_NN50,
            strokeWidth = R.dimen.tokochat_1dp,
            //Hide the top shadow
            shadowTop = Int.ZERO,
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
                            com.tokopedia.tokochat_common.R.string.tokochat_desc_max_char_exceeded, formattedOffset
                        ) ?: ""
                        composeArea?.setBackgroundResource(com.tokopedia.tokochat_common.R.drawable.bg_tokochat_error_too_long_msg)
                    }

                    override fun hideTextLimitError() {
                        errorComposeMsg?.hide()
                        setDefaultComposeBackground()
                    }
                })
            composeArea?.addTextChangedListener(sendButtonTextWatcher)
        }
    }

    private fun getFormattedOffset(offset: Int): String {
        return if (offset > MAX_DISPLAYED_OFFSET) {
            "10.000+"
        } else {
            numberFormat.format(offset)
        }
    }

    companion object {
        val LAYOUT = R.layout.tokochat_partial_message_area
        private const val MAX_DISPLAYED_OFFSET = 10_000
    }
}
