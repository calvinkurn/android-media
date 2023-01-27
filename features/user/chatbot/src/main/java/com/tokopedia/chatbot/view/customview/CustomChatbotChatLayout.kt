package com.tokopedia.chatbot.view.customview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chatbot.EllipsizeMaker
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.removeUnderLineFromLinkAndSetText
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show

class CustomChatbotChatLayout : ViewGroup {

    var checkMark: ImageView? = null
        private set
    var message: TextView? = null
    private var status: LinearLayout? = null
    private var timeStamp: TextView? = null
    private var hourTime: TextView? = null
    private var readMoreView: TextView? = null

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        initView(context, attrs)
    }

    private fun initAttr(context: Context?, attrs: AttributeSet?) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.CustomChatbotChatLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark = getBoolean(R.styleable.CustomChatbotChatLayout_showCheckMark, DEFAULT_SHOW_CHECK_MARK)
                useMaxWidth = getBoolean(R.styleable.CustomChatbotChatLayout_useMaxWidth, DEFAULT_USE_MAX_WIDTH)
            } finally {
                recycle()
            }
        }
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(LAYOUT, this, true).also {
            message = it.findViewById(R.id.tvMessage)
            status = it.findViewById(R.id.llStatus)
            checkMark = it.findViewById(R.id.ivCheckMark)
            hourTime = it.findViewById(R.id.tvTime)
            readMoreView = it.findViewById(R.id.read_more_text)
        }
        initCheckMarkVisibility()
    }

    private fun initCheckMarkVisibility() {
        if (!showCheckMark) {
            hideReadStatus()
        } else {
            showReadStatus()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        var totalHeight = MeasureSpec.getSize(heightMeasureSpec)

        if (message == null || status == null || totalWidth <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val messageLayout = message?.layoutParams as LayoutParams
        val statusLayout = status?.layoutParams as LayoutParams
        val infoLayout = readMoreView?.layoutParams as LayoutParams

        val maxWidth = totalWidth
        val availableWidth = totalWidth - paddingLeft - paddingRight

        measureChildWithMargins(message, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(status, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(readMoreView, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val messageWidth =
            message!!.measuredWidth + messageLayout.leftMargin + messageLayout.rightMargin
        val messageHeight =
            message!!.measuredHeight + messageLayout.topMargin + messageLayout.bottomMargin
        val statusWidth =
            status!!.measuredWidth + statusLayout.leftMargin + statusLayout.rightMargin
        val statusHeight =
            status!!.measuredHeight + statusLayout.topMargin + statusLayout.bottomMargin
        val infoWidth =
            readMoreView!!.measuredWidth + infoLayout.leftMargin + infoLayout.rightMargin - messageWidth
        val infoHeight =
            readMoreView!!.measuredHeight + infoLayout.topMargin + infoLayout.bottomMargin

        val messageLineCount = message!!.lineCount
        val lastLineWidth: Float = if (messageLineCount > 0) {
            message!!.layout.getLineWidth(messageLineCount - 1)
        } else {
            0f
        }

        totalWidth = paddingLeft + paddingRight + messageWidth
        totalHeight = paddingTop + paddingBottom + messageHeight
        var isAddStatusHeight = false

        if (messageWidth + statusWidth <= availableWidth) {
            totalWidth += statusWidth
        } else if (lastLineWidth + statusWidth > availableWidth) {
            totalHeight += statusHeight
            isAddStatusHeight = true
        }

        if (infoWidth > 0 && readMoreView?.isVisible == true) {
            totalWidth += infoWidth
        }

        if (infoHeight > 0 && readMoreView?.isVisible == true && !isAddStatusHeight) {
            totalHeight += infoHeight
        }

        if (totalWidth > maxWidth || useMaxWidth) {
            totalWidth = maxWidth
        }

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
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
         * Layout info
         */
        if (readMoreView!!.isVisible) {
            val infoLp = readMoreView!!.layoutParams as MarginLayoutParams
            val leftInfo = paddingStart
            val topInfo = topOffset + infoLp.topMargin
            val rightInfo = paddingStart + readMoreView!!.measuredWidth
            val bottomInfo = topInfo + readMoreView!!.measuredHeight
            readMoreView?.layout(
                leftInfo,
                topInfo,
                rightInfo,
                bottomInfo
            )
        }

        /**
         * Layout status
         */
        val leftStatus = measuredWidth - paddingEnd - status!!.measuredWidth
        val topStatus = measuredHeight - paddingBottom - status!!.measuredHeight
        val rightStatus = measuredWidth - paddingEnd
        val bottomStatus = measuredHeight - paddingBottom
        status!!.layout(
            leftStatus,
            topStatus,
            rightStatus,
            bottomStatus
        )
    }

    private fun getVisibleMeasuredWidth(view: View?): Int {
        if (view?.isVisible == false) return 0
        return view?.measuredWidth ?: 0
    }

    private fun getVisibleMeasuredHeight(view: View?): Int {
        if (view?.isVisible == false) return 0
        return view?.measuredHeight ?: 0
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * Per-child layout information associated with [FlexBoxChatLayout].
     */
    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    fun setMessage(msg: String, isSender: Boolean) {
        if (!isSender) {
            message?.removeUnderLineFromLinkAndSetText(msg)
            if (message != null) {
                message!!.post {
                    if (message!!.lineCount >= EllipsizeMaker.MESSAGE_LINE_COUNT) {
                        message!!.maxLines = EllipsizeMaker.MESSAGE_LINE_COUNT
                        EllipsizeMaker.setTruncatedMsg(message!!, msg)
                        readMoreView?.visibility = View.VISIBLE
                        readMoreView?.setOnClickListener {
                            showFullMessage(msg)
                        }
                    } else {
                        readMoreView?.visibility = View.GONE
                    }
                }
            }
        } else {
            message?.text = MethodChecker.fromHtml(msg)
        }
    }

    private fun showFullMessage(message: String) {
        this.message?.maxLines = Int.MAX_VALUE
        this.message?.removeUnderLineFromLinkAndSetText(message)
        readMoreView?.visibility = View.GONE
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

    fun changeReadStatus(readStatus: Drawable?) {
        checkMark?.setImageDrawable(readStatus)
    }

    fun setMovementMethod(movementMethod: ChatLinkHandlerMovementMethod) {
        message?.movementMethod = movementMethod
    }

    fun hideReadMoreView() {
        readMoreView?.hide()
    }

    fun showReadMoreView() {
        readMoreView?.show()
    }

    companion object {
        private const val DEFAULT_USE_MAX_WIDTH = false
        private const val DEFAULT_SHOW_CHECK_MARK = true
        private val LAYOUT = R.layout.customview_chatbot_message_layout
    }
}
