package com.tokopedia.tokochat_common.view.customview

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.MAX_MESSAGE_IN_BUBBLE
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs

class TokoChatMessageChatLayout : ViewGroup {

    var checkMark: ImageUnify? = null
        private set
    var hourTime: Typography? = null
        private set
    var message: Typography? = null
        private set
    var status: LinearLayout? = null
        private set
    var readMore: Typography? = null
        private set

    private var showCheckMark = DEFAULT_SHOW_CHECK_MARK
    private var useMaxWidth = DEFAULT_USE_MAX_WIDTH

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

    override fun setBackground(background: Drawable?) {
        val pl = paddingLeft
        val pt = paddingTop
        val pr = paddingRight
        val pb = paddingBottom
        super.setBackground(background)
        setPadding(pl, pt, pr, pb)
    }

    private fun initConfig(context: Context?, attrs: AttributeSet?) {
        initAttr(context, attrs)
        initView(context)
    }

    private fun initAttr(context: Context?, attrs: AttributeSet?) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.TokoChatMessageChatLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark =
                    getBoolean(R.styleable.TokoChatMessageChatLayout_tokochatShowCheckMark, DEFAULT_SHOW_CHECK_MARK)
                useMaxWidth =
                    getBoolean(R.styleable.TokoChatMessageChatLayout_tokochatUseMaxWidth, DEFAULT_USE_MAX_WIDTH)
            } finally {
                recycle()
            }
        }
    }

    private fun initView(context: Context?) {
        LayoutInflater.from(context).inflate(LAYOUT, this, true).also {
            message = it.findViewById(R.id.tokochat_tv_msg)
            status = it.findViewById(R.id.tokochat_layout_msg_status)
            checkMark = it.findViewById(R.id.tokochat_iv_msg_check_mark)
            hourTime = it.findViewById(R.id.tokochat_tv_msg_time)
            readMore = it.findViewById(R.id.tokochat_tv_msg_read_more)
        }
        initCheckMarkVisibility()
    }

    fun setShowCheckMark(showCheckMark: Boolean) {
        this.showCheckMark = showCheckMark
        initCheckMarkVisibility()
    }

    private fun initCheckMarkVisibility() {
        if (!showCheckMark) {
            hideReadStatus()
        } else {
            showReadStatus()
        }
    }

    fun setMessage(text: CharSequence?) {
        val textLength = text?.length?: Int.ZERO
        if (textLength > MAX_MESSAGE_IN_BUBBLE) {
            message?.text = text?.substring(Int.ZERO, MAX_MESSAGE_IN_BUBBLE)
        } else {
            message?.text = text
        }
    }

    fun setMessageTypeFace(msg: TokoChatMessageBubbleUiModel) {
        val typeface = if (msg.isNotSupported) {
            Typeface.ITALIC
        } else {
            Typeface.NORMAL
        }
        message?.setTypeface(null, typeface)
    }

    fun setHourTime(time: String) {
        hourTime?.text = time
    }

    private fun showReadStatus() {
        checkMark?.show()
    }

    private fun hideReadStatus() {
        checkMark?.hide()
    }

    fun bindReadMore(msg: TokoChatMessageBubbleUiModel, onClickReadMore: () -> Unit) {
        if (msg.messageText.length > MAX_MESSAGE_IN_BUBBLE) {
            readMore?.show()
            readMore?.setOnClickListener {
                onClickReadMore()
            }
        } else {
            readMore?.hide()
        }
    }

    fun bindTextColor(msg: TokoChatMessageBubbleUiModel) {
        val textColor = if (msg.isNotSupported) {
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN800
        }
        message?.setTextColor(MethodChecker.getColor(context, textColor))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (message == null || status == null || readMore == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val maxAvailableWidth = widthSpecSize - paddingLeft - paddingRight
        var totalWidth = 0
        var totalHeight = 0

        /**
         * get measurement and layout params of direct child
         */
        measureChildWithMargins(
            message, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            readMore, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            status, widthMeasureSpec, 0, heightMeasureSpec, 0
        )

        /**
         * calculate each direct child width & height
         */
        // Message
        val messageWidth = getTotalVisibleWidth(message)
        var messageHeight = getTotalVisibleHeight(message)
        // readMore
        val readMoreWidth = getTotalVisibleWidth(readMore)
        var readMoreHeight = getTotalVisibleHeight(readMore)
        // Status
        val statusWidth = getTotalVisibleWidth(status)
        val statusHeight = getTotalVisibleHeight(status)

        /**
         * Measure second row dimension
         */
        val secondRowWidth = messageWidth + statusWidth
        val secondRowWidthDiff = totalWidth - secondRowWidth
        if (secondRowWidthDiff < 0) {
            totalWidth += abs(secondRowWidthDiff)
        }
        val secondRowHeight = maxOf(messageHeight, statusHeight)
        totalHeight += secondRowHeight
        // check if icon and message is overlap
        if (messageWidth > maxAvailableWidth) {
            totalHeight -= messageHeight
            val messageWidthSpec = MeasureSpec.makeMeasureSpec(
                maxAvailableWidth, MeasureSpec.EXACTLY
            )
            message?.measure(messageWidthSpec, heightMeasureSpec)
            messageHeight = getTotalVisibleHeight(message)
            totalHeight += messageHeight
        }

        /**
         * Measure third row dimension if any
         */
        if (readMore?.isVisible == true) {
            val thirdRowWidth = readMoreWidth + statusWidth
            val thirdRowWidthDiff = totalWidth - thirdRowWidth
            if (thirdRowWidthDiff < 0) {
                totalWidth += abs(thirdRowWidthDiff)
            }
            val thirdRowHeight = maxOf(readMoreHeight, statusHeight)
            totalHeight += thirdRowHeight
            // Set readMore width if overlap with [status] layout
            val readMoreMaxWidth = maxAvailableWidth - statusWidth
            if (readMoreWidth > readMoreMaxWidth) {
                totalHeight -= readMoreHeight
                val readMoreWidthSpec = MeasureSpec.makeMeasureSpec(
                    readMoreMaxWidth, MeasureSpec.EXACTLY
                )
                readMore?.measure(readMoreWidthSpec, heightMeasureSpec)
                readMoreHeight = getTotalVisibleHeight(readMore)
                totalHeight += readMoreHeight
            }
        }

        /**
         * Measure forth row dimension
         * Measure msg last line dimension
         */
        var msgLastLineWidth = 0f
        val msgLineCount: Int = message?.lineCount ?: 0
        if (msgLineCount > 0) {
            msgLastLineWidth = message?.layout?.getLineWidth(msgLineCount - 1) ?: 0f
        }
        val lastLineWidth = msgLastLineWidth + statusWidth - REPLY_WIDTH_OFFSET
        if (lastLineWidth > maxAvailableWidth || readMore?.isVisible == true) {
            totalHeight += statusHeight
        }

        totalWidth += (paddingLeft + paddingRight)
        totalHeight += (paddingTop + paddingBottom)

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec),
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var topOffset = paddingTop

        /**
         * Layout msg
         */
        val leftMsg = paddingStart
        val topMsg = topOffset
        val rightMsg = leftMsg + getVisibleMeasuredWidth(message)
        val bottomMsg = topMsg + getVisibleMeasuredHeight(message)
        message?.layout(
            leftMsg,
            topMsg,
            rightMsg,
            bottomMsg
        )
        topOffset = bottomMsg

        /**
         * Layout readMore
         */
        readMore?.let {
            if (it.isVisible) {
                val readMoreLp = it.layoutParams as MarginLayoutParams
                val leftReadMore = paddingStart
                val topReadMore = topOffset + readMoreLp.topMargin
                val rightReadMore = paddingStart + it.measuredWidth
                val bottomReadMore = topReadMore + it.measuredHeight
                it.layout(
                    leftReadMore,
                    topReadMore,
                    rightReadMore,
                    bottomReadMore
                )
                topOffset += bottomReadMore
            }
        }

        /**
         * Layout status
         */
        status?.let {
            val leftStatus = measuredWidth - paddingEnd - it.measuredWidth
            val topStatus = measuredHeight - paddingBottom - it.measuredHeight
            val rightStatus = measuredWidth - paddingEnd
            val bottomStatus = measuredHeight - paddingBottom
            it.layout(
                leftStatus,
                topStatus,
                rightStatus,
                bottomStatus
            )
        }
    }

    private fun getTotalVisibleWidth(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return view.measuredWidth + viewLp.leftMargin + viewLp.rightMargin
    }

    private fun getVisibleMeasuredWidth(view: View?): Int {
        if (view?.isVisible == false) return 0
        return view?.measuredWidth ?: 0
    }

    private fun getTotalVisibleHeight(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return view.measuredHeight + viewLp.topMargin + viewLp.bottomMargin
    }

    private fun getVisibleMeasuredHeight(view: View?): Int {
        if (view?.isVisible == false) return 0
        return view?.measuredHeight ?: 0
    }

    private fun getVisibleEndMargin(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return viewLp.rightMargin
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
     * Per-child layout information associated with [TokoChatMessageChatLayout].
     */
    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    companion object {
        const val DEFAULT_USE_MAX_WIDTH = false
        const val DEFAULT_SHOW_CHECK_MARK = true
        const val REPLY_WIDTH_OFFSET = 5
        val LAYOUT = R.layout.tokochat_partial_message_text
    }
}
