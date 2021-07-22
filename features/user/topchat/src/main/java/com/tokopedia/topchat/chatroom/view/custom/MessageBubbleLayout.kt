package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.message.ReplyBubbleAreaMessage
import kotlin.math.max
import kotlin.math.min


class MessageBubbleLayout : ViewGroup {

    private var fxChat: FlexBoxChatLayout? = null
    private var replyBubbleContainer: ReplyBubbleAreaMessage? = null
    private var showCheckMark = FlexBoxChatLayout.DEFAULT_SHOW_CHECK_MARK
    private var msgOrientation = DEFAULT_MSG_ORIENTATION
    private val radiusMargin = 20f.toPx().toInt()

    constructor(context: Context) : super(context) {
        initConfig(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initConfig(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initConfig(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initConfig(context, attrs)
    }

    init {
        initViewLayout()
    }

    fun bindReplyData(uiModel: Visitable<*>) {
        replyBubbleContainer?.bindReplyData(uiModel)
    }

    private fun initConfig(context: Context?, attrs: AttributeSet?) {
        initViewBinding()
        initAttrs(context, attrs)
        initFlexboxChatLayout()
        initReplyBubbleBackground()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.MessageBubbleConstraintLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark = getBoolean(
                    R.styleable.MessageBubbleConstraintLayout_showCheckMark,
                    FlexBoxChatLayout.DEFAULT_SHOW_CHECK_MARK
                )
                msgOrientation = getInteger(
                    R.styleable.MessageBubbleConstraintLayout_messageOrientation,
                    DEFAULT_MSG_ORIENTATION
                )
            } finally {
                recycle()
            }
        }
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBinding() {
        fxChat = findViewById(R.id.fxChat)
        replyBubbleContainer = findViewById(R.id.cl_reply_container)
    }

    private fun initFlexboxChatLayout() {
        fxChat?.setShowCheckMark(showCheckMark)
    }

    private fun initReplyBubbleBackground() {
        val drawableRes = when (msgOrientation) {
            LEFT_MSG_ORIENTATION -> R.drawable.bg_chat_reply_preview_left_bubble
            RIGHT_MSG_ORIENTATION -> R.drawable.bg_chat_reply_preview_right_bubble
            else -> null
        } ?: return
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        replyBubbleContainer?.background = drawable
    }

    /**
     * Measure it's children, take the max width of a child and set it
     * to the other child.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * Find child max width and calculate layout height
         */
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        var maxChildWidth = 0
        var myHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.isVisible) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                maxChildWidth = max(child.measuredWidth, maxChildWidth)
                maxChildWidth = min(maxWidth, maxChildWidth)
                myHeight += child.measuredHeight
            }
        }
        /**
         * set max width to every child
         */
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childLp = child.layoutParams as MarginLayoutParams
            val childHorizontalMargin = childLp.leftMargin + childLp.rightMargin
            val childWidth = maxChildWidth - childHorizontalMargin
            val widthSpec = MeasureSpec.makeMeasureSpec(
                childWidth,
                MeasureSpec.EXACTLY
            )
            child.measure(widthSpec, heightMeasureSpec)
        }
        /**
         * reduce height by border radius value if [replyBubbleContainer]
         * is visible because part of the [replyBubbleContainer] and [fxChat]
         * view is overlap.
         */
        if (replyBubbleContainer?.isVisible == true) {
            myHeight -= radiusMargin
        }
        /**
         * Set the final width and height of this ViewGroup
         */
        setMeasuredDimension(
            maxChildWidth, myHeight
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var topOffset = 0
        replyBubbleContainer?.let {
            if (it.isVisible) {
                val lp = it.layoutParams as MarginLayoutParams
                topOffset = top + it.measuredHeight
                val leftPos = lp.leftMargin
                val rightPos = leftPos + it.measuredWidth
                it.layout(
                    leftPos, 0, rightPos, topOffset
                )
                topOffset -= radiusMargin
            }
        }
        fxChat?.let {
            it.layout(
                0, topOffset, it.measuredWidth, topOffset + it.measuredHeight
            )
        }
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    /**
     * Per-child layout information associated with [MessageBubbleLayout].
     */
    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    companion object {
        val LAYOUT = R.layout.partial_chat_messsage_bubble

        const val LEFT_MSG_ORIENTATION = 0
        const val RIGHT_MSG_ORIENTATION = 1
        const val DEFAULT_MSG_ORIENTATION = LEFT_MSG_ORIENTATION
    }
}