package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TransactionOrderProgressLayout : LinearLayout {

    private var status: Typography? = null
    private var stateChanger: Typography? = null
    private var descriptionContainer: ConstraintLayout? = null
    private var cardOrderContainer: ConstraintLayout? = null
    private var productThumbnail: ImageView? = null
    private var productName: Typography? = null
    private var estimateTitle: Typography? = null
    private var estimateValue: Typography? = null
    private var actionBtn: UnifyButton? = null

    private var chatOrder: ChatOrderProgress = ChatOrderProgress()
    private var state = State()

    private var canBeRendered = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        initBindView()
    }

    fun renderIfExist() {
        canBeRendered = true
        render(this.chatOrder)
    }

    fun render(chatOrder: ChatOrderProgress) {
        this.chatOrder = chatOrder
        if (!shouldBeRendered()) return
        loadPreviousState()
        renderBackgroundHasBeenSeen()
        renderStateDescription()
        renderStateChangerButton()
        renderOrderStatus()
        renderLayoutVisibility()
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

    private fun renderLayoutVisibility() {
        showWithCondition(chatOrder.enable)
    }

    private fun renderOrderStatus() {
        status?.text = chatOrder.status
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
            renderImageThumbnail()
            renderProductName()
            renderEstimation()
            renderActionButton()
        } else {
            // TODO: impl this
        }
    }

    private fun renderOpenCloseStateChangerButton() {
        stateChanger?.text = state.bodyVisibility
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

    private fun bindClickOpenCloseState() {
        val clickListener = OnClickListener {
            doWhenState(
                    isOpen = { changeState(stateClose) },
                    isClose = { changeState(stateOpen) }
            )
        }
        stateChanger?.setOnClickListener(clickListener)
        status?.setOnClickListener(clickListener)
    }

    private fun loadPreviousState() {
        try {
            val stateJsonString = getPref()?.getString(getPrefOrderPrefKey(), DEFAULT_STATE)
            state = CommonUtil.fromJson(stateJsonString, State::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun renderBackgroundHasBeenSeen() {
        if (!state.haBeenSeen) {
            cardOrderContainer?.setBackgroundColor(colorStateSeen.toInt())
        } else {
            cardOrderContainer?.setBackgroundColor(Color.WHITE)
        }
    }

    private fun saveCurrentState() {
        val stateJsonString = CommonUtil.toJson(state)
        getPref()?.edit()?.putString(getPrefOrderPrefKey(), stateJsonString)?.apply()
    }

    private fun getPref(): SharedPreferences? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getPrefOrderPrefKey(): String {
        return chatOrder.orderId
    }

    private fun openCloseState(): Boolean {
        // TODO: impl this
        return true
    }

    private fun doWhenState(
            isOpen: () -> Unit,
            isClose: () -> Unit
    ) {
        when (state.bodyVisibility) {
            stateOpen -> isOpen()
            stateClose -> isClose()
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

    private fun hideDescription() {
        descriptionContainer?.hide()
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
        productName?.text = chatOrder.name
    }

    private fun renderEstimation() {
        estimateTitle?.text = chatOrder.label.title
        estimateValue?.text = chatOrder.label.value
    }

    private fun renderActionButton() {
        actionBtn?.shouldShowWithAction(chatOrder.hasActionButton) {
            actionBtn?.text = chatOrder.button.label
            actionBtn?.setOnClickListener {
                RouteManager.route(context, chatOrder.button.uri)
            }
        }
    }

    data class State(
            var bodyVisibility: String = DEFAULT_BODY_VISIBILITY,
            var haBeenSeen: Boolean = DEFAULT_HAS_SEEN
    )

    companion object {
        private val LAYOUT = R.layout.partial_transaction_order_progress

        private const val colorStateSeen = 0xFFEBFFEF

        private const val stateOpen = "Tutup"
        private const val stateClose = "Lihat"

        private const val DEFAULT_BODY_VISIBILITY = stateOpen
        private const val DEFAULT_HAS_SEEN = false
        private val DEFAULT_STATE = CommonUtil.toJson(State())

        private const val PREF_NAME = "Chat_TransactionOrderProgressPreference"
    }
}