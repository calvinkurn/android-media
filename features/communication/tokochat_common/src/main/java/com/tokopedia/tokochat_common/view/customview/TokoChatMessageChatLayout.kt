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
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleBaseUiModel
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
    var icon: IconUnify? = null
        private set
    var info: Typography? = null
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
            info = it.findViewById(R.id.tokochat_tv_msg_info)
            icon = it.findViewById(R.id.tokochat_icon_msg)
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

    fun setMessage(msg: TokoChatMessageBubbleBaseUiModel, text: CharSequence?) {
        when {
            msg.isBanned() -> {
                val bannedText = context.getString(R.string.tokochat_title_sender_chat_banned)
                message?.text = bannedText
            }
            msg.isDeleted() -> {
                val deletedText = context.getString(R.string.tokochat_title_chat_deleted)
                message?.text = deletedText
            }
            msg.isNormal() -> message?.text = text
        }
    }

    fun setMessageTypeFace(msg: TokoChatMessageBubbleBaseUiModel) {
        val typeface = if (msg.isDeleted() || msg.isBanned()) {
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

    fun bindInfo(msg: TokoChatMessageBubbleBaseUiModel) {
        if (msg.isBanned()) {
            bindInfoBanned()
        } else if (msg.hasLabel()) {
            bindInfoNormal(msg)
        } else {
            info?.hide()
        }
    }

    private fun bindInfoBanned() {
        info?.text = context.getString(R.string.tokochat_title_check_tnc)
        info?.setTextColor(MethodChecker.getColor(
            context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        info?.setOnClickListener {
            RouteManager.route(context, TokoChatUrlUtil.TNC)
        }
        info?.show()
    }

    private fun bindInfoNormal(msg: TokoChatMessageBubbleBaseUiModel) {
        info?.text = msg.label
        info?.show()
    }

    fun bindIcon(msg: TokoChatMessageBubbleBaseUiModel) {
        icon?.shouldShowWithAction(msg.isDeleted() || msg.isBanned()) {
            val unifyIcon = getIconUnifyDrawable(
                context,
                IconUnify.BLOCK,
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN500
                )
            )
            icon?.setImageDrawable(unifyIcon)
        }
    }

    fun bindTextColor(msg: TokoChatMessageBubbleBaseUiModel) {
        val textColor = if (msg.isDeleted() || msg.isBanned()) {
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN800
        }
        message?.setTextColor(MethodChecker.getColor(context, textColor))
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
        // msg icon
        val iconWidth = getTotalVisibleWidth(icon)
        val iconHeight = getTotalVisibleHeight(icon)

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
            message?.measure(messageWidthSpec, heightMeasureSpec)
            messageHeight = getTotalVisibleHeight(message)
            totalHeight += messageHeight
        }
        // Measure msg last line dimension
        var isOverlapped = false
        val msgLineCount = message?.lineCount ?: 0
        val msgLastLineWidth: Float = if (msgLineCount > 0) {
            message?.layout?.getLineWidth(msgLineCount - 1) ?: 0f
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
        val thirdRowHeight = if (isOverlapped && info?.isVisible == true) {
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
            info?.measure(infoWidthSpec, heightMeasureSpec)
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
        info?.let {
            if (it.isVisible) {
                val infoLp = it.layoutParams as MarginLayoutParams
                val leftInfo = paddingStart
                val topInfo = topOffset + infoLp.topMargin
                val rightInfo = paddingStart + it.measuredWidth
                val bottomInfo = topInfo + it.measuredHeight
                it.layout(
                    leftInfo,
                    topInfo,
                    rightInfo,
                    bottomInfo
                )
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
