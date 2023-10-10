package com.tokopedia.topchat.chatroom.view.custom.messagebubble.autoreply

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaMessageAttachment
import com.tokopedia.topchat.chatroom.view.adapter.TopChatAutoReplyAdapter
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.topchat.chatroom.view.adapter.util.MessageOnTouchListener
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomAutoReplyListBinding
import timber.log.Timber
import kotlin.math.abs


class FlexBoxChatAutoReplyLayout : ViewGroup {

    private var layoutInflater: LayoutInflater? = null
    private var listener: Listener? = null
    private var checkMark: ImageView? = null
    private var hourTime: TextView? = null

    /**
     * Direct child view
     */
    private var message: TextView? = null
    private var status: LinearLayout? = null
    private var info: TextView? = null
    private var header: LinearLayout? = null
    private var icon: IconUnify? = null

    private var autoReplyConstraintLayout: ConstraintLayout? = null
    private var autoReplyBinding: TopchatChatroomAutoReplyListBinding? = null

    /**
     * Header direct child
     */
    private var headerTitle: Typography? = null
    private var headerCta: Typography? = null
    private var headerDivider: View? = null

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
        fun onClickReadMoreAutoReply(
            welcomeMessage: TopChatAutoReplyItemUiModel,
            list: List<TopChatAutoReplyItemUiModel>
        )
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
        layoutInflater = LayoutInflater.from(context)
        layoutInflater?.let { inflater ->
            inflater.inflate(LAYOUT, this, true)?.also {
                message = it.findViewById(R.id.tvMessage)
                status = it.findViewById(R.id.llStatus)
                checkMark = it.findViewById(R.id.ivCheckMark)
                hourTime = it.findViewById(R.id.tvTime)
                info = it.findViewById(R.id.txt_info)
                header = it.findViewById(R.id.ll_msg_header)
                icon = it.findViewById(R.id.iu_msg_icon)
                headerTitle = it.findViewById(R.id.tp_header_title)
                headerCta = it.findViewById(R.id.tp_header_cta)
                headerDivider = it.findViewById(R.id.v_header_divider)
                autoReplyConstraintLayout = it.findViewById(R.id.topchat_chatroom_cl_auto_reply)
            }
            autoReplyBinding = TopchatChatroomAutoReplyListBinding.inflate(inflater, this)
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

    private fun setMessage(msg: CharSequence?) {
        message?.text = msg
    }

    private fun setMessageTypeFace(chat: MessageUiModel) {
        val typeface = if (chat.isDeleted()) {
            Typeface.ITALIC
        } else {
            Typeface.NORMAL
        }
        message?.let {
            it.setTypeface(null, typeface)
        }
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
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            HeaderCtaMessageAttachment.STATUS_DISABLED ->
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
            else -> com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
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

    fun bindIcon(msg: MessageUiModel) {
        icon?.shouldShowWithAction(msg.isDeleted()) {
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

    fun bindTextColor(msg: MessageUiModel) {
        val textColor = if (msg.isDeleted()) {
            com.tokopedia.unifyprinciples.R.color.Unify_NN600
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        }
        message?.setTextColor(MethodChecker.getColor(context, textColor))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (message == null || status == null || info == null || autoReplyConstraintLayout == null) {
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
        measureChildWithMargins(
            autoReplyConstraintLayout, widthMeasureSpec, 0, heightMeasureSpec, 0
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
        // CL Auto Reply
        val clAutoReplyWidth = getTotalVisibleWidth(autoReplyConstraintLayout)
        val clAutoReplyHeight = getTotalVisibleHeight(autoReplyConstraintLayout)


        /**
         * Measure first row dimension
         */
        totalWidth += headerWidth
        totalHeight += headerHeight

        /**
         * Measure second row dimension
         */
        val secondRowWidth = maxOf(
            iconWidth + messageWidth + statusWidth,
            statusWidth + clAutoReplyWidth
        )
        val secondRowWidthDiff = totalWidth - secondRowWidth
        if (secondRowWidthDiff < 0) {
            totalWidth += abs(secondRowWidthDiff)
        }
        val secondRowHeight = maxOf(
            messageHeight, statusHeight, iconHeight
        )
        // Add secondRowHeight and clAutoReplyHeight, clAutoReplyHeight should be 0 when not visible
        totalHeight += secondRowHeight + clAutoReplyHeight
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
         * Auto reply Layout
         */
        val leftAutoReply = paddingStart
        val topAutoReply = bottomMsg + AUTO_REPLY_OFFSET
        val rightAutoReply = paddingStart + getVisibleMeasuredWidth(autoReplyConstraintLayout)
        val bottomAutoReply = topAutoReply + getVisibleMeasuredHeight(autoReplyConstraintLayout)
        autoReplyConstraintLayout!!.layout(
            leftAutoReply,
            topAutoReply,
            rightAutoReply,
            bottomAutoReply
        )

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

    private fun getVisibleStartMargin(view: View?): Int {
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
     * Per-child layout information associated with [FlexBoxChatLayout].
     */
    class LayoutParams : MarginLayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

    /**
     * Set Message Body
     * If source is auto_reply, check for JSON
     */
    fun setMessageBody(messageUiModel: MessageUiModel) {
        if (messageUiModel.isFromAutoReply()) {
            setAutoReplyMessageBody(messageUiModel)
        } else {
            setRegularMessageBody(messageUiModel)
        }
    }

    private fun setRegularMessageBody(messageUiModel: MessageUiModel) {
        val htmlMessage = MethodChecker.fromHtml(messageUiModel.message)
        setMessageTypeFace(messageUiModel)
        setMessage(htmlMessage)
        autoReplyConstraintLayout?.hide()
    }

    private fun setAutoReplyMessageBody(messageUiModel: MessageUiModel) {
        try {
            val listType = object : TypeToken<List<TopChatAutoReplyItemUiModel>>() {}.type
            val result = Gson().fromJson<List<TopChatAutoReplyItemUiModel>>(
                messageUiModel.message, listType
            )
            bindAutoReplyView(messageUiModel = messageUiModel, autoReplyList = result)
            autoReplyConstraintLayout?.show()
        } catch (throwable: Throwable) {
            /**
             * If fail then the message is history message
             * New Auto Reply should be in JSON format
             * Render with regular message body
             */
            Timber.d(throwable)
            setRegularMessageBody(messageUiModel)
        }
    }

    private fun bindAutoReplyView(
        messageUiModel: MessageUiModel,
        autoReplyList: List<TopChatAutoReplyItemUiModel>
    ) {
        val welcomeMessage = autoReplyList.firstOrNull {
            it.getIcon() == null // Check only for welcome message
        }
        val autoReplyItemList = autoReplyList.filter {
            it.getIcon() != null // Check only for auto reply item
        }
        bindAutoReplyMessage(
            welcomeMessage = welcomeMessage,
            messageUiModel = messageUiModel,
            shouldLimitWelcomeMessage = autoReplyItemList.isNotEmpty()
        )
        bindAutoReplyRecyclerView(
            welcomeMessage = welcomeMessage,
            autoReplyItemList = autoReplyItemList
        )
    }

    private fun bindAutoReplyMessage(
        welcomeMessage: TopChatAutoReplyItemUiModel?,
        messageUiModel: MessageUiModel,
        shouldLimitWelcomeMessage: Boolean
    ) {
        setMessageTypeFace(messageUiModel)
        setMessage(welcomeMessage?.getMessage())
        if (shouldLimitWelcomeMessage) {
            message?.maxLines = 3
            message?.ellipsize = TextUtils.TruncateAt.END
        }
    }

    private fun bindAutoReplyRecyclerView(
        welcomeMessage: TopChatAutoReplyItemUiModel?,
        autoReplyItemList: List<TopChatAutoReplyItemUiModel>
    ) {
        if (autoReplyItemList.isNotEmpty()) {
            val adapter = TopChatAutoReplyAdapter()
            autoReplyBinding?.topchatChatroomRvAutoReply?.adapter = adapter
            autoReplyBinding?.topchatChatroomRvAutoReply?.layoutManager =
                LinearLayoutManager(context)
            autoReplyBinding?.topchatChatroomRvAutoReply?.itemAnimator = null
            adapter.updateItem(autoReplyItemList)
            autoReplyBinding?.topchatChatroomRvAutoReply?.show()
            bindAutoReplyText(welcomeMessage, autoReplyItemList)
        } else {
            autoReplyBinding?.topchatChatroomRvAutoReply?.hide()
            autoReplyBinding?.topchatTvAutoReplyDesc?.hide()
        }
    }

    private fun bindAutoReplyText(
        welcomeMessage: TopChatAutoReplyItemUiModel?,
        autoReplyItemList: List<TopChatAutoReplyItemUiModel>
    ) {
        autoReplyBinding?.topchatTvAutoReplyDesc?.setOnClickListener {
            welcomeMessage?.let {
                listener?.onClickReadMoreAutoReply(it, autoReplyItemList)
            }
        }
        autoReplyBinding?.topchatTvAutoReplyDesc?.show()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    companion object {
        const val DEFAULT_USE_MAX_WIDTH = false
        const val DEFAULT_SHOW_CHECK_MARK = true
        const val REPLY_WIDTH_OFFSET = 5
        const val AUTO_REPLY_OFFSET = 10
        val LAYOUT = R.layout.topchat_chatroom_partial_flexbox_chat_bubble_auto_reply
    }
}

