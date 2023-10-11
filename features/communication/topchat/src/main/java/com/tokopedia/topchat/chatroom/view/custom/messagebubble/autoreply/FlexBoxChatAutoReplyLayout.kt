package com.tokopedia.topchat.chatroom.view.custom.messagebubble.autoreply

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.TopChatAutoReplyAdapter
import com.tokopedia.topchat.chatroom.view.custom.messagebubble.base.BaseTopChatFlexBoxChatLayout
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomAutoReplyListBinding
import timber.log.Timber
import kotlin.math.abs


class FlexBoxChatAutoReplyLayout : BaseTopChatFlexBoxChatLayout {

    /**
     * Auto reply child
     */
    private var autoReplyConstraintLayout: ConstraintLayout? = null
    private var autoReplyBinding: TopchatChatroomAutoReplyListBinding? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun getLayout(): Int {
        return LAYOUT
    }

    override fun bindAdditionalView(view: View) {
        autoReplyConstraintLayout = view.findViewById(R.id.topchat_chatroom_cl_auto_reply)
        layoutInflater?.let {
            autoReplyBinding = TopchatChatroomAutoReplyListBinding.inflate(it, this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (message == null ||
            status == null ||
            info == null ||
            autoReplyConstraintLayout == null
        ) {
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
            autoReplyConstraintLayout,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0
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
        if (autoReplyConstraintLayout!!.isVisible) {
            val leftAutoReply = paddingStart
            val topAutoReply = topOffset
            val rightAutoReply = paddingStart + autoReplyConstraintLayout!!.measuredWidth
            val bottomAutoReply = topAutoReply + autoReplyConstraintLayout!!.measuredHeight
            autoReplyConstraintLayout!!.layout(
                leftAutoReply,
                topAutoReply,
                rightAutoReply,
                bottomAutoReply
            )
            topOffset = bottomAutoReply
        }

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

    /**
     * Set Message Body
     * If source is auto_reply, check for JSON
     */
    override fun setMessageBody(messageUiModel: MessageUiModel) {
        if (messageUiModel.isFromAutoReply()) {
            setAutoReplyMessageBody(messageUiModel)
        } else {
            setRegularMessageBody(messageUiModel)
        }
    }

    private fun setRegularMessageBody(messageUiModel: MessageUiModel) {
        super.setMessageBody(messageUiModel)
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
        message?.text = welcomeMessage?.getMessage()
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
            autoReplyBinding?.topchatTvAutoReplyReadMore?.hide()
        }
    }

    private fun bindAutoReplyText(
        welcomeMessage: TopChatAutoReplyItemUiModel?,
        autoReplyItemList: List<TopChatAutoReplyItemUiModel>
    ) {
        autoReplyBinding?.topchatTvAutoReplyReadMore?.setOnClickListener {
            welcomeMessage?.let {
                flexBoxListener?.onClickReadMoreAutoReply(it, autoReplyItemList)
            }
        }
        autoReplyBinding?.topchatTvAutoReplyReadMore?.show()
    }

    companion object {
        private val LAYOUT = R.layout.topchat_chatroom_partial_flexbox_chat_bubble_auto_reply
    }
}

