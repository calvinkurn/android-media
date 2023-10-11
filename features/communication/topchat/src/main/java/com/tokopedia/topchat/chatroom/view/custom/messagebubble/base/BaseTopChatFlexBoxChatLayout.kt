package com.tokopedia.topchat.chatroom.view.custom.messagebubble.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaMessageAttachment
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.chat_common.R as chat_commonR

abstract class BaseTopChatFlexBoxChatLayout : ViewGroup {

    protected var layoutInflater: LayoutInflater? = null
    protected var flexBoxListener: TopChatFlexBoxListener? = null
    private var checkMark: ImageView? = null
    private var hourTime: TextView? = null

    /**
     * Direct child view
     */
    protected var message: TextView? = null
    protected var status: LinearLayout? = null
    protected var info: TextView? = null
    protected var header: LinearLayout? = null
    protected var icon: IconUnify? = null

    /**
     * Header direct child
     */
    private var headerTitle: Typography? = null
    private var headerCta: Typography? = null
    private var headerDivider: View? = null

    private var showCheckMark = DEFAULT_SHOW_CHECK_MARK
    private var useMaxWidth = DEFAULT_USE_MAX_WIDTH

    @LayoutRes
    abstract fun getLayout(): Int

    constructor(context: Context) : super(context) {
        initConfig(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initConfig(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
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

    private fun initView(context: Context?) {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater?.inflate(getLayout(), this, true)
        view?.let {
            bindInitialView(view)
            bindAdditionalView(view)
            initCheckMarkVisibility()
        }
    }

    private fun bindInitialView(view: View) {
        message = view.findViewById(R.id.tvMessage)
        status = view.findViewById(R.id.llStatus)
        checkMark = view.findViewById(R.id.ivCheckMark)
        hourTime = view.findViewById(R.id.tvTime)
        info = view.findViewById(R.id.txt_info)
        header = view.findViewById(R.id.ll_msg_header)
        icon = view.findViewById(R.id.iu_msg_icon)
        headerTitle = view.findViewById(R.id.tp_header_title)
        headerCta = view.findViewById(R.id.tp_header_cta)
        headerDivider = view.findViewById(R.id.v_header_divider)
    }

    abstract fun bindAdditionalView(view: View)

    /**
     * Check Mark / Read Status
     */
    private fun initCheckMarkVisibility() {
        if (!showCheckMark) {
            hideReadStatus()
        } else {
            showReadStatus()
        }
    }

    private fun showReadStatus() {
        checkMark?.show()
    }

    fun hideReadStatus() {
        checkMark?.gone()
    }

    fun setShowCheckMark(showCheckMark: Boolean) {
        this.showCheckMark = showCheckMark
        initCheckMarkVisibility()
    }

    fun bindChatReadStatus(element: MessageUiModel) {
        if (element.isShowTime && element.isSender && !element.isDeleted()) {
            showReadStatus()
            val imageResource = when {
                element.isDummy -> chat_commonR.drawable.ic_chatcommon_check_rounded_grey
                !element.isRead -> chat_commonR.drawable.ic_chatcommon_check_sent_rounded_grey
                else -> chat_commonR.drawable.ic_chatcommon_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(context, imageResource)
            checkMark?.setImageDrawable(drawable)
        } else {
            hideReadStatus()
        }
    }

    /**
     * Time / Hour Text
     */
    fun setHourTime(time: String) {
        hourTime?.text = time
    }

    /**
     * Message Body
     */

    protected fun setMessageTypeFace(chat: MessageUiModel) {
        val typeface = if (chat.isDeleted()) {
            Typeface.ITALIC
        } else {
            Typeface.NORMAL
        }
        message?.setTypeface(null, typeface)
    }

    open fun setMessageBody(messageUiModel: MessageUiModel) {
        val htmlMessage = MethodChecker.fromHtml(messageUiModel.message)
        setMessageTypeFace(messageUiModel)
        message?.text = htmlMessage
    }

    fun getMessageText(): CharSequence? {
        return message?.text
    }

    fun bindTextColor(msg: MessageUiModel) {
        val textColor = if (msg.isDeleted()) {
            unifyprinciplesR.color.Unify_NN600
        } else {
            unifyprinciplesR.color.Unify_NN950_96
        }
        message?.setTextColor(MethodChecker.getColor(context, textColor))
    }

    fun bindIcon(msg: MessageUiModel) {
        icon?.shouldShowWithAction(msg.isDeleted()) {
            val unifyIcon = getIconUnifyDrawable(
                context,
                IconUnify.BLOCK,
                ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_NN500
                )
            )
            icon?.setImageDrawable(unifyIcon)
        }
    }

    /**
     * Info / Text Label Gray on Bottom Bubble
     */
    fun showInfo(label: String) {
        info?.text = label
        info?.show()
    }

    fun hideInfo() {
        info?.hide()
    }

    /**
     * Header / Additional Text on Top Bubble
     * It has CTA, ex: User's Address from SRW reply
     */
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

    private fun bindHeaderTitle(attachment: HeaderCtaButtonAttachment) {
        headerTitle?.text = attachment.ctaButton.header
    }

    private fun bindHeaderBody(attachment: HeaderCtaButtonAttachment) {
        val htmlMsg = MethodChecker.fromHtml(attachment.ctaButton.body)
        message?.text = htmlMsg
    }

    private fun binDHeaderCtaState(attachment: HeaderCtaButtonAttachment) {
        val ctaColor = when (attachment.ctaButton.status) {
            HeaderCtaMessageAttachment.STATUS_ENABLED ->
                unifyprinciplesR.color.Unify_GN500
            HeaderCtaMessageAttachment.STATUS_DISABLED ->
                unifyprinciplesR.color.Unify_NN950_32
            else -> unifyprinciplesR.color.Unify_NN950_32
        }
        val color = ContextCompat.getColor(context, ctaColor)
        headerCta?.setTextColor(color)
    }

    private fun bindHeaderCtaTitle(attachment: HeaderCtaButtonAttachment) {
        headerCta?.text = attachment.ctaButton.textUrl
    }

    private fun bindHeaderCtaClick(attachment: HeaderCtaButtonAttachment) {
        headerCta?.setOnClickListener {
            flexBoxListener?.changeAddress(attachment)
        }
        headerCta?.isEnabled = attachment.ctaButton.isClickable()
    }

    private fun bindHeaderDivider(shouldHideDivider: Boolean) {
        headerDivider?.showWithCondition(!shouldHideDivider)
    }

    /**
     * Set Touch and Click Listener
     */

    @SuppressLint("ClickableViewAccessibility")
    fun setMessageOnTouchListener(onTouchListener: MessageOnTouchListener) {
        message?.setOnTouchListener(onTouchListener)
    }

    fun setListener(listener: TopChatFlexBoxListener) {
        this.flexBoxListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (message == null || status == null || info == null) {
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
            info, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            status, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            header, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        measureChildWithMargins(
            icon, widthMeasureSpec, 0, heightMeasureSpec, 0
        )

        /**
         * calculate each direct child width & height
         */
        // Message
        val messageWidth = getTotalVisibleWidth(message)
        var messageHeight = getTotalVisibleHeight(message)
        // Info
        val infoWidth = getTotalVisibleWidth(info)
        var infoHeight = getTotalVisibleHeight(info)
        // Status
        val statusWidth = getTotalVisibleWidth(status)
        val statusHeight = getTotalVisibleHeight(status)
        // Header
        val headerWidth = getTotalVisibleWidth(header)
        val headerHeight = getTotalVisibleHeight(header)
        // msg icon
        val iconWidth = getTotalVisibleWidth(icon)
        val iconHeight = getTotalVisibleHeight(icon)

        /**
         * Measure first row dimension
         */
        totalWidth += headerWidth
        totalHeight += headerHeight

        /**
         * Measure second row dimension
         */
        val secondRowWidth = iconWidth + messageWidth + statusWidth
        val secondRowWidthDiff = totalWidth - secondRowWidth
        if (secondRowWidthDiff < 0) {
            totalWidth += abs(secondRowWidthDiff)
        }
        val secondRowHeight = maxOf(messageHeight, statusHeight, iconHeight)
        totalHeight += secondRowHeight
        // check if icon and message is overlap
        val messageMaxWidth = maxAvailableWidth - iconWidth
        if (messageWidth > messageMaxWidth) {
            totalHeight -= messageHeight
            val messageWidthSpec = MeasureSpec.makeMeasureSpec(
                messageMaxWidth, MeasureSpec.EXACTLY
            )
            message!!.measure(messageWidthSpec, heightMeasureSpec)
            messageHeight = getTotalVisibleHeight(message)
            totalHeight += messageHeight
        }
        // Measure msg last line dimension
        var isOverlapped = false
        val msgLineCount = message!!.lineCount
        val msgLastLineWidth: Float = if (msgLineCount > 0) {
            message!!.layout.getLineWidth(msgLineCount - 1)
        } else {
            0f
        }
        val lastLineWidth = msgLastLineWidth + iconWidth + statusWidth - REPLY_WIDTH_OFFSET
        if (lastLineWidth > maxAvailableWidth) {
            totalHeight += statusHeight
            isOverlapped = true
        }

        /**
         * Measure third row dimension
         */
        val thirdRowWidth = infoWidth + statusWidth
        val thirdRowWidthDiff = totalWidth - thirdRowWidth
        if (thirdRowWidthDiff < 0) {
            totalWidth += abs(thirdRowWidthDiff)
        }
        val thirdRowHeight = if (isOverlapped && info!!.isVisible) {
            abs(infoHeight - statusHeight)
        } else {
            infoHeight
        }
        totalHeight += thirdRowHeight
        // Set info width if overlap with [status] layout
        val infoMaxWidth = maxAvailableWidth - statusWidth
        if (infoWidth > infoMaxWidth) {
            totalHeight -= infoHeight
            val infoWidthSpec = MeasureSpec.makeMeasureSpec(
                infoMaxWidth, MeasureSpec.EXACTLY
            )
            info!!.measure(infoWidthSpec, heightMeasureSpec)
            infoHeight = getTotalVisibleHeight(info)
            totalHeight += infoHeight
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
         * Layout icon
         */
        val leftIcon = paddingStart
        val topIcon = topOffset
        val rightIcon = leftIcon + getVisibleMeasuredWidth(icon)
        val bottomIcon = topIcon + getVisibleMeasuredHeight(icon)
        icon?.layout(
            leftIcon,
            topIcon,
            rightIcon,
            bottomIcon
        )

        /**
         * Layout msg
         */
        val leftMsg = rightIcon + getVisibleEndMargin(icon)
        val topMsg = topOffset
        val rightMsg = leftMsg + getVisibleMeasuredWidth(message)
        val bottomMsg = topMsg + getVisibleMeasuredHeight(message)
        message?.layout(
            leftMsg,
            topMsg,
            rightMsg,
            bottomMsg
        )
        topOffset = maxOf(bottomIcon, bottomMsg)

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

    protected fun getTotalVisibleWidth(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return view.measuredWidth + viewLp.leftMargin + viewLp.rightMargin
    }

    protected fun getVisibleMeasuredWidth(view: View?): Int {
        if (view?.isVisible == false) return 0
        return view?.measuredWidth ?: 0
    }

    protected fun getTotalVisibleHeight(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return view.measuredHeight + viewLp.topMargin + viewLp.bottomMargin
    }

    protected fun getVisibleMeasuredHeight(view: View?): Int {
        if (view?.isVisible == false) return 0
        return view?.measuredHeight ?: 0
    }

    protected fun getVisibleEndMargin(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return viewLp.rightMargin
    }

    protected fun getVisibleStartMargin(view: View?): Int {
        val viewLp = view?.layoutParams as? MarginLayoutParams ?: return 0
        if (!view.isVisible) return 0
        return viewLp.leftMargin
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
     * Per-child layout information associated with [BaseTopChatFlexBoxChatLayout].
     */
    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    companion object {
        private const val DEFAULT_USE_MAX_WIDTH = false
        const val DEFAULT_SHOW_CHECK_MARK = true
        const val REPLY_WIDTH_OFFSET = 5
    }
}
