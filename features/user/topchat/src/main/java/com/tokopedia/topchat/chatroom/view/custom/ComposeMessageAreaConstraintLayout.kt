package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.chat_common.view.listener.TypingListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.ReplyBoxTextListener
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifyprinciples.Typography

class ComposeMessageAreaConstraintLayout : ConstraintLayout, LifecycleObserver {

    private var composeArea: EditText? = null
    private var errorComposeMsg: Typography? = null

    private var textWatcher: MessageTextWatcher? = null
    private var sendButtontextWatcher: ComposeTextWatcher? = null

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
        composeArea = findViewById(R.id.new_comment)
        errorComposeMsg = findViewById(R.id.tp_error_compose)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanUpReference() {
        composeArea?.removeTextChangedListener(textWatcher)
        composeArea?.removeTextChangedListener(sendButtontextWatcher)
    }

    fun initLayout(
        typingListener: TypingListener?,
        replyBoxTextListener: ReplyBoxTextListener?
    ) {
        initComposeMsgListener(typingListener, replyBoxTextListener)
        initComposeBackground()
    }

    fun onSendMessage() {
        textWatcher?.cancelJob()
    }

    fun getComposedText(): String {
        return composeArea?.text?.toString() ?: ""
    }

    private fun initComposeBackground() {
        val bgComposeArea = ViewUtil.generateBackgroundWithShadow(
            view = composeArea,
            backgroundColor = com.tokopedia.unifyprinciples.R.color.Unify_Background,
            topLeftRadius = R.dimen.dp_topchat_20,
            topRightRadius = R.dimen.dp_topchat_20,
            bottomLeftRadius = R.dimen.dp_topchat_20,
            bottomRightRadius = R.dimen.dp_topchat_20,
            shadowColor = R.color.topchat_dms_chat_bubble_shadow,
            elevation = R.dimen.dp_topchat_2,
            shadowRadius = R.dimen.dp_topchat_1,
            shadowGravity = Gravity.CENTER
        )
        val paddingStart =
            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7).toInt()
        val paddingEnd =
            resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8).toInt()
        val paddingTop = resources.getDimension(R.dimen.dp_topchat_11).toInt()
        val paddingBottom = resources.getDimension(R.dimen.dp_topchat_10).toInt()
        composeArea?.background = bgComposeArea
        composeArea?.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
    }

    private fun initComposeMsgListener(
        typingListener: TypingListener?,
        replyBoxTextListener: ReplyBoxTextListener?
    ) {
        typingListener?.let {
            textWatcher = MessageTextWatcher(typingListener)
            composeArea?.addTextChangedListener(textWatcher)
        }
        replyBoxTextListener?.let {
            sendButtontextWatcher = ComposeTextWatcher(replyBoxTextListener)
            composeArea?.addTextChangedListener(sendButtontextWatcher)
        }
    }

    companion object {
        val LAYOUT = R.layout.partial_compose_message_area
    }
}