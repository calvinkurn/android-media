package com.tokopedia.tokochat_common.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokochat_common.R
import kotlin.math.max
import kotlin.math.min

class TokoChatMessageBubbleLayout : ViewGroup {

    private var bodyMsgContainerLayout: LinearLayout? = null
    private var msgChatLayout: TokoChatMessageChatLayout? = null
    private var showCheckMark = TokoChatMessageChatLayout.DEFAULT_SHOW_CHECK_MARK
    private var msgOrientation = DEFAULT_MSG_ORIENTATION

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

    fun setMsgGravity(gravity: Int) {
        val gravityAttr = when (gravity) {
            Gravity.START -> LEFT_MSG_ORIENTATION
            Gravity.END -> RIGHT_MSG_ORIENTATION
            else -> DEFAULT_MSG_ORIENTATION
        }
        msgOrientation = gravityAttr
    }

    private fun initConfig(context: Context?, attrs: AttributeSet?) {
        initViewBinding()
        initAttrs(context, attrs)
        initFlexboxChatLayout()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.TokoChatMessageBubbleLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark = getBoolean(
                    R.styleable.TokoChatMessageBubbleLayout_tokochatShowCheckMark,
                    TokoChatMessageChatLayout.DEFAULT_SHOW_CHECK_MARK
                )
                msgOrientation = getInteger(
                    R.styleable.TokoChatMessageBubbleLayout_tokochatMessageOrientation,
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
        bodyMsgContainerLayout = findViewById(R.id.tokochat_layout_body_message_container)
        msgChatLayout = findViewById(R.id.tokochat_layout_message_chat)
    }

    private fun initFlexboxChatLayout() {
        msgChatLayout?.setShowCheckMark(showCheckMark)
    }

    /**
     * Measure it's children, take the max width of a child and set it
     * to the other child.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val additionalWidthSpace = 10

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
            var childWidth = if (maxChildWidth + additionalWidthSpace > widthSpecSize) {
                maxChildWidth
            } else {
                maxChildWidth + additionalWidthSpace
            }
            childWidth -= childHorizontalMargin
            val widthSpec = MeasureSpec.makeMeasureSpec(
                childWidth,
                MeasureSpec.EXACTLY
            )
            child.measure(widthSpec, heightMeasureSpec)
        }

        /**
         * Set the final width and height of this ViewGroup
         */
        setMeasuredDimension(
            resolveSize(maxChildWidth + additionalWidthSpace, widthMeasureSpec),
            resolveSize(myHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        msgChatLayout?.let {
            it.layout(
                0, 0, it.measuredWidth, it.measuredHeight
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
     * Per-child layout information associated with [TokoChatMessageBubbleLayout].
     */
    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    companion object {
        val LAYOUT = R.layout.tokochat_partial_message_bubble

        const val LEFT_MSG_ORIENTATION = 0
        const val RIGHT_MSG_ORIENTATION = 1
        const val DEFAULT_MSG_ORIENTATION = LEFT_MSG_ORIENTATION
    }
}
