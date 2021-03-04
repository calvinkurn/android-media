package com.tokopedia.topchat.chatroom.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TransactionOrderProgressLayout : LinearLayout {

    interface Listener {
        fun getAnalytic(): TopChatAnalytics
    }

    private var status: Typography? = null
    private var stateChanger: Typography? = null
    private var descriptionContainer: ConstraintLayout? = null
    private var cardOrderContainer: ConstraintLayout? = null
    private var productThumbnail: ImageView? = null
    private var productName: Typography? = null
    private var estimateTitle: Typography? = null
    private var estimateValue: Typography? = null
    private var actionBtn: UnifyButton? = null
    private var listener: Listener? = null
    private var chatOrder: ChatOrderProgress = ChatOrderProgress()
    private var state = State()
    private var canBeRendered = false
    private val analytics: TopChatAnalytics? get() = listener?.getAnalytic()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        initBindView()
    }

    fun shouldRefreshOnStart(): Boolean = chatOrder.isCtaFinish

    fun renderIfExist() {
        canBeRendered = true
        render(this.listener, this.chatOrder)
    }

    fun render(listener: Listener?, chatOrder: ChatOrderProgress) {
        assignFields(listener, chatOrder)
        if (!shouldBeRendered()) return
        loadPreviousState()
        renderBackgroundColor()
        renderStateDescription()
        renderStateChangerButton()
        renderOrderStatus()
        renderLayoutVisibility()
        renderHasBeenSeen()
        saveCurrentState()
        analytics?.eventViewOrderProgress(chatOrder)
    }

    private fun assignFields(listener: Listener?, chatOrder: ChatOrderProgress) {
        this.chatOrder = chatOrder
        this.listener = listener
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initBindView() {
        status = findViewById(R.id.tp_order_status)
        stateChanger = findViewById(R.id.tp_order_visibility)
        descriptionContainer = findViewById(R.id.cl_description_container)
        cardOrderContainer = findViewById(R.id.card_order_container)
        productThumbnail = findViewById(R.id.iv_order_thumbnail)
        productName = findViewById(R.id.tp_order_name)
        estimateTitle = findViewById(R.id.tp_estimate_label)
        estimateValue = findViewById(R.id.tp_estimate_value)
        actionBtn = findViewById(R.id.btn_action_label)
    }

    private fun shouldBeRendered(): Boolean {
        return canBeRendered && chatOrder.isNotEmpty() && chatOrder.enable.also { shouldShow ->
            if (!shouldShow) hide()
        }
    }

    private fun loadPreviousState() {
        try {
            val stateJsonString = getPref()?.getString(getPrefOrderPrefKey(), DEFAULT_STATE)
            state = CommonUtil.fromJson(stateJsonString, State::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun renderBackgroundColor() {
        val colorRes = if (!state.hasSeen) {
            State.COLOR_NOT_SEEN
        } else {
            State.COLOR_SEEN
        }
        val color = MethodChecker.getColor(context, colorRes)
        cardOrderContainer?.setBackgroundColor(color)
    }

    private fun renderStateDescription() {
        doWhenState(
                isOpen = { showDescription() },
                isClose = { hideDescription() }
        )
    }

    /**
     * Check if the implementation is one of the 2:
     * 1. if transaction is not finished user can open/close
     * 2. if transaction is finished. closed state only
     */
    private fun renderStateChangerButton() {
        if (openCloseState()) {
            renderOpenCloseStateChangerButton()
            bindClickOpenCloseState()
            bindClickCardDescription()
            renderImageThumbnail()
            renderProductName()
            renderEstimation()
            renderActionButton()
        } else {
            unbindClickStatus()
            hideDescription()
            renderCloseStateChangerButton()
            bindClickCloseState()
        }
    }

    private fun renderOrderStatus() {
        status?.text = chatOrder.status
    }

    private fun renderLayoutVisibility() {
        showWithCondition(chatOrder.enable)
    }

    private fun renderHasBeenSeen() {
        if (!state.hasSeen) {
            renderFirstTimeSeen()
        }
    }

    private fun saveCurrentState() {
        val stateJsonString = CommonUtil.toJson(state)
        getPref()?.edit()?.putString(getPrefOrderPrefKey(), stateJsonString)?.apply()
    }

    private fun openCloseState(): Boolean {
        return !chatOrder.isStateFinished()
    }

    private fun bindClickOpenCloseState() {
        val clickListener = OnClickListener {
            doWhenState(
                    isOpen = { changeState(State.CLOSE) },
                    isClose = { changeState(State.OPEN) }
            )
        }
        stateChanger?.setOnClickListener(clickListener)
        status?.setOnClickListener(clickListener)
    }

    private fun bindClickCardDescription() {
        descriptionContainer?.setOnClickListener {
            analytics?.eventClickOrderProgressCardDescription()
            RouteManager.route(context, chatOrder.uri)
        }
    }

    private fun renderImageThumbnail() {
        ImageHandler.loadImageRounded2(
                context,
                productThumbnail,
                chatOrder.imageUrl,
                8f.toPx()
        )
    }

    private fun renderProductName() {
        productName?.text = MethodChecker.fromHtml(chatOrder.name)
    }

    @SuppressLint("SetTextI18n")
    private fun renderEstimation() {
        estimateTitle?.text = chatOrder.label.title + ":"
        estimateValue?.text = chatOrder.label.value
    }

    private fun renderActionButton() {
        actionBtn?.shouldShowWithAction(chatOrder.hasActionButton) {
            actionBtn?.text = chatOrder.button.label
            actionBtn?.setOnClickListener {
                analytics?.eventClickCtaButton(chatOrder)
                if (handleTrackClick()) return@setOnClickListener
                RouteManager.route(context, chatOrder.button.uri)
            }
        }
    }

    private fun handleTrackClick(): Boolean {
        if (chatOrder.isCtaTrack) {
            val uri = UriUtil.buildUri(ApplinkConst.ORDER_TRACKING, chatOrder.orderId)
            RouteManager.route(context, uri)
            return true
        }
        return false
    }

    private fun renderCloseStateChangerButton() {
        renderChangerButtonText(chatOrder.button.label)
        renderChangerButtonTextColor()
        renderChangerButtonCompoundDrawable(R.drawable.ic_topchat_arrow_small_right)
    }

    private fun bindClickCloseState() {
        stateChanger?.setOnClickListener {
            analytics?.eventClickOrderProgressBuyAgain()
            RouteManager.route(context, chatOrder.button.uri)
        }
    }

    private fun renderFirstTimeSeen() {
        state.seen()
    }

    private fun renderChangerButtonTextColor() {
        stateChanger?.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    private fun renderOpenCloseStateChangerButton() {
        renderChangerButtonText()
        renderChangerButtonCompoundDrawable()
    }

    private fun renderChangerButtonCompoundDrawable(@DrawableRes customIcon: Int? = null) {
        if (customIcon != null) {
            stateChanger?.setCompoundDrawablesWithIntrinsicBounds(0, 0, customIcon, 0)
            return
        }
        doWhenState(
                isOpen = {
                    stateChanger?.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_topchat_arrow_small_up, 0
                    )
                },
                isClose = {
                    stateChanger?.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_topchat_arrow_small_down, 0
                    )
                }
        )
    }

    private fun renderChangerButtonText(custom: String? = null) {
        if (custom != null) {
            stateChanger?.text = custom
        } else {
            stateChanger?.text = state.bodyVisibility
        }
    }

    private fun getPref(): SharedPreferences? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getPrefOrderPrefKey(): String {
        return chatOrder.orderId
    }

    private fun doWhenState(
            isOpen: () -> Unit,
            isClose: () -> Unit
    ) {
        when (state.bodyVisibility) {
            State.OPEN -> isOpen()
            State.CLOSE -> isClose()
        }
    }

    private fun changeState(desiredState: String) {
        if (state.bodyVisibility == desiredState) return
        state.bodyVisibility = desiredState
        renderOpenCloseStateChangerButton()
        renderStateDescription()
    }

    private fun showDescription() {
        descriptionContainer?.show()
    }

    private fun unbindClickStatus() {
        status?.setOnClickListener(null)
    }

    private fun hideDescription() {
        descriptionContainer?.hide()
    }

    data class State(
            var bodyVisibility: String = DEFAULT_BODY_VISIBILITY,
            var hasSeen: Boolean = DEFAULT_HAS_SEEN
    ) {
        fun seen() {
            this.hasSeen = true
        }

        companion object {
            val COLOR_NOT_SEEN = com.tokopedia.unifyprinciples.R.color.Unify_G100
            var COLOR_SEEN = com.tokopedia.unifyprinciples.R.color.Unify_N0

            const val OPEN = "Tutup"
            const val CLOSE = "Lihat"

            private const val DEFAULT_BODY_VISIBILITY = OPEN
            private const val DEFAULT_HAS_SEEN = false
        }
    }

    companion object {
        private val LAYOUT = R.layout.partial_transaction_order_progress
        private val DEFAULT_STATE = CommonUtil.toJson(State())
        private const val PREF_NAME = "Chat_TransactionOrderProgressPreference"
    }
}