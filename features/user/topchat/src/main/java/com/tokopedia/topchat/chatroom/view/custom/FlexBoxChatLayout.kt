package com.tokopedia.topchat.chatroom.view.custom

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaMessageAttachment
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener


class FlexBoxChatLayout : ViewGroup {

    var listener: Listener? = null
    var checkMark: ImageView? = null
        private set
    private var timeStamp: TextView? = null
    private var hourTime: TextView? = null

    /**
     * Direct child view
     */
    var message: TextView? = null
        private set
    var status: LinearLayout? = null
        private set
    var info: TextView? = null
        private set
    var header: LinearLayout? = null
        private set

    /**
     * Header direct child
     */
    var headerTitle: Typography? = null
        private set
    var headerCta: Typography? = null
        private set
    var headerDivider: View? = null
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initConfig(context, attrs)
    }

    interface Listener {
        fun changeAddress(attachment: HeaderCtaButtonAttachment)
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
            R.styleable.FlexBoxChatLayout,
            0,
            0
        )?.apply {
            try {
                showCheckMark =
                    getBoolean(R.styleable.FlexBoxChatLayout_showCheckMark, DEFAULT_SHOW_CHECK_MARK)
                useMaxWidth =
                    getBoolean(R.styleable.FlexBoxChatLayout_useMaxWidth, DEFAULT_USE_MAX_WIDTH)
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
            info = it.findViewById(R.id.txt_info)
            header = it.findViewById(R.id.ll_msg_header)
            headerTitle = it.findViewById(R.id.tp_header_title)
            headerCta = it.findViewById(R.id.tp_header_cta)
            headerDivider = it.findViewById(R.id.v_header_divider)
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

    fun setMessage(msg: CharSequence?) {
        message?.text = msg
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

    @SuppressLint("ClickableViewAccessibility")
    fun setMessageOnTouchListener(onTouchListener: MessageOnTouchListener) {
        message?.setOnTouchListener(onTouchListener)
    }

    fun hideInfo() {
        info?.hide()
    }

    fun showInfo(label: String) {
        info?.text = label
        info?.show()
    }

    fun getMsg(): String {
        return message?.text.toString()
    }

    fun renderHeaderAttachment(
        attachment: Any?,
        shouldHideDivider: Boolean
    ) {
        header?.show()
        when (attachment) {
            is HeaderCtaButtonAttachment -> renderCtaHeader(
                attachment,
                shouldHideDivider
            )
            else -> header?.hide()
        }
    }

    fun hideAttachmentHeader() {
        header?.hide()
    }

    private fun renderCtaHeader(
        attachment: HeaderCtaButtonAttachment,
        shouldHideDivider: Boolean
    ) {
        bindHeaderTitle(attachment)
        bindHeaderBody(attachment)
        bindHeaderCta(attachment)
        bindHeaderDivider(shouldHideDivider)
    }

    private fun bindHeaderCta(attachment: HeaderCtaButtonAttachment) {
        if (attachment.ctaButton.hasVisibleCta()) {
            headerCta?.show()
            bindHeaderCtaTitle(attachment)
            binDHeaderCtaState(attachment)
            bindHeaderCtaClick(attachment)
        } else {
            headerCta?.hide()
        }
    }

    private fun bindHeaderDivider(shouldHideDivider: Boolean) {
        headerDivider?.showWithCondition(!shouldHideDivider)
    }

    private fun bindHeaderBody(attachment: HeaderCtaButtonAttachment) {
        val htmlMsg = MethodChecker.fromHtml(attachment.ctaButton.body)
        setMessage(htmlMsg)
    }

    private fun bindHeaderTitle(attachment: HeaderCtaButtonAttachment) {
        headerTitle?.text = attachment.ctaButton.header
    }

    private fun binDHeaderCtaState(attachment: HeaderCtaButtonAttachment) {
        val ctaColor = when (attachment.ctaButton.status) {
            HeaderCtaMessageAttachment.STATUS_ENABLED ->
                com.tokopedia.unifyprinciples.R.color.Unify_G500
            HeaderCtaMessageAttachment.STATUS_DISABLED ->
                com.tokopedia.unifyprinciples.R.color.Unify_N700_32
            else -> com.tokopedia.unifyprinciples.R.color.Unify_N700_32
        }
        val color = ContextCompat.getColor(context, ctaColor)
        headerCta?.setTextColor(color)
    }

    private fun bindHeaderCtaTitle(attachment: HeaderCtaButtonAttachment) {
        headerCta?.text = attachment.ctaButton.textUrl
    }

    private fun bindHeaderCtaClick(attachment: HeaderCtaButtonAttachment) {
        headerCta?.setOnClickListener {
            listener?.changeAddress(attachment)
        }
        headerCta?.isEnabled = attachment.ctaButton.isClickable()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (message == null || status == null || info == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val maxAvailableWidth = widthSpecSize - paddingLeft - paddingRight
        var widthFilled = 0
        var totalWidth = paddingLeft + paddingRight
        var totalHeight = paddingTop + paddingBottom

        /**
         * get measurement and layout params of direct child
         */
        measureChildWithMargins(
            message, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            info, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            status, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            header, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        val messageLp = message?.layoutParams as MarginLayoutParams
        val infoLp = info?.layoutParams as MarginLayoutParams
        val statusLp = status?.layoutParams as MarginLayoutParams
        val headerLp = header?.layoutParams as MarginLayoutParams

        /**
         * calculate each direct child width & height
         */
        // Message
        val messageWidth = message!!.measuredWidth + messageLp.leftMargin +
                messageLp.rightMargin
        val messageHeight = message!!.measuredHeight + messageLp.topMargin +
                messageLp.bottomMargin
        // Info
        val infoWidth = info!!.measuredWidth + infoLp.leftMargin + infoLp.rightMargin
        val infoHeight = info!!.measuredHeight + infoLp.topMargin + infoLp.bottomMargin
        // Status
        val statusWidth = status!!.measuredWidth + statusLp.leftMargin +
                statusLp.rightMargin
        val statusHeight = status!!.measuredHeight + statusLp.topMargin +
                statusLp.bottomMargin
        // Header
        val headerWidth = header!!.measuredWidth + headerLp.leftMargin +
                headerLp.rightMargin
        val headerHeight = header!!.measuredHeight + headerLp.topMargin +
                headerLp.bottomMargin

        /**
         * Measure header dimension
         */
        if (header!!.isVisible) {
            totalWidth += headerWidth
            totalHeight += headerHeight
            widthFilled = headerWidth
        }

        /**
         * Measure message dimension
         */
        val msgAndHeaderWidthDiff = messageWidth - widthFilled
        if (msgAndHeaderWidthDiff > 0) {
            totalWidth += msgAndHeaderWidthDiff
            widthFilled = messageWidth
        }
        totalHeight += messageHeight

        /**
         * Measure info layout
         */
        if (info!!.isVisible) {
            val infoAndMsgWidthDiff = infoWidth - widthFilled
            if (infoAndMsgWidthDiff > 0) {
                totalWidth += infoAndMsgWidthDiff
                widthFilled = infoWidth
            }
            totalHeight += infoHeight

            // Set info width if overlap with [status] layout
            val infoMaxWidth = maxAvailableWidth - statusWidth
            if (infoWidth > infoMaxWidth) {
                val infoWidthSpec = MeasureSpec.makeMeasureSpec(
                    infoMaxWidth, MeasureSpec.EXACTLY
                )
                info!!.measure(infoWidthSpec, heightMeasureSpec)
            }
        }

        /**
         * measure status dimension
         */
        if (info!!.isVisible) {
            val footerWidth = infoWidth + statusWidth
            val footerAndMessageWidthDiff = footerWidth - widthFilled
            if (footerAndMessageWidthDiff > 0) {
                totalWidth += footerAndMessageWidthDiff
                widthFilled = footerWidth
            }
            val statusAndInfoHeightDiff = statusHeight - infoHeight
            if (statusAndInfoHeightDiff > 0) {
                totalHeight += statusAndInfoHeightDiff
            }
        } else {
            val msgLineCount = message!!.lineCount
            val msgLastLineWidth: Float = if (msgLineCount > 0) {
                message!!.layout.getLineWidth(msgLineCount - 1)
            } else {
                0f
            }
            val lastLineAndStatusWidth = msgLastLineWidth + statusWidth
            val offset = 5
            if (lastLineAndStatusWidth < maxAvailableWidth + offset) {
                totalWidth += statusWidth
            } else {
                totalHeight += statusHeight
            }
        }

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec),
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var topOffset = paddingTop

        /**
         * Layout Header
         */
        if (header!!.isVisible) {
            val leftHeader = paddingStart
            val topHeader = topOffset
            val rightHeader = paddingStart + header!!.measuredWidth
            val bottomHeader = topHeader + header!!.measuredHeight
            header?.layout(
                leftHeader,
                topHeader,
                rightHeader,
                bottomHeader
            )
            topOffset = bottomHeader
        }

        /**
         * Layout msg
         */
        val leftMsg = paddingStart
        val topMsg = topOffset
        val rightMsg = paddingStart + message!!.measuredWidth
        val bottomMsg = topMsg + message!!.measuredHeight
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
        if (info!!.isVisible) {
            val infoLp = info!!.layoutParams as MarginLayoutParams
            val leftInfo = paddingStart
            val topInfo = topOffset + infoLp.topMargin
            val rightInfo = paddingStart + info!!.measuredWidth
            val bottomInfo = topInfo + info!!.measuredHeight
            info?.layout(
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
     * Per-child layout information associated with [FlexBoxChatLayout].
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
        val LAYOUT = R.layout.flexbox_chat_message
    }
}