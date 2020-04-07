package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.unifyprinciples.Typography

class TransactionOrderProgressLayout : LinearLayout {

    private var status: Typography? = null
    private var stateChanger: Typography? = null
    private var descriptionContainer: ConstraintLayout? = null
    private var state: String = stateClose

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        initBindView()
        initPreviousState()
    }

    private fun initViewLayout() {
        View.inflate(context, R.layout.partial_transaction_order_progress, this)
    }

    private fun initBindView() {
        status = findViewById(R.id.tp_order_status)
        stateChanger = findViewById(R.id.tp_order_visibility)
        descriptionContainer = findViewById(R.id.cl_description_container)
    }

    fun render(chatOrder: ChatOrderProgress) {
        if (!shouldBeRendered(chatOrder)) return
        renderStateDescription()
        renderStateChangerButton(chatOrder)
        renderOrderStatus(chatOrder)
        renderLayoutVisibility(chatOrder)
    }

    private fun shouldBeRendered(chatOrder: ChatOrderProgress): Boolean {
        return chatOrder.enable.also { shouldShow ->
            if (!shouldShow) hide()
        }
    }

    private fun renderLayoutVisibility(chatOrder: ChatOrderProgress) {
        showWithCondition(chatOrder.enable)
    }

    private fun renderOrderStatus(chatOrder: ChatOrderProgress) {
        status?.text = chatOrder.status
    }

    private fun renderStateDescription() {
        when (state) {
            stateOpen -> showDescription()
            stateClose -> hideDescription()
        }
    }

    /**
     * Check if the implementation is one of the 2:
     * 1. if transaction is not finished user can open/close
     * 2. if transaction is finished. closed state only
     */
    private fun renderStateChangerButton(chatOrder: ChatOrderProgress) {
        if (openCloseState(chatOrder)) {
            renderOpenCloseStateChangerButton()
            bindClickOpenCloseState(chatOrder)
        } else {
            // TODO: impl this
        }
    }

    private fun renderOpenCloseStateChangerButton() {
        stateChanger?.text = state
        when (state) {
            stateOpen -> {
                stateChanger?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_topchat_arrow_small_up, 0)
            }
            stateClose -> {
                stateChanger?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_topchat_arrow_small_down, 0)
            }
        }
    }

    private fun bindClickOpenCloseState(chatOrder: ChatOrderProgress) {
        stateChanger?.setOnClickListener {
            when (state) {
                stateOpen -> changeState(stateClose)
                stateClose -> changeState(stateOpen)
            }
        }
    }

    private fun openCloseState(chatOrder: ChatOrderProgress): Boolean {
        // TODO: impl this
        return true
    }

    private fun initPreviousState() {
        // TODO: impl this
    }

    private fun changeState(desiredState: String) {
        if (state == desiredState) return
        state = desiredState
        renderOpenCloseStateChangerButton()
        renderStateDescription()
    }

    private fun showDescription() {
        descriptionContainer?.show()
    }

    private fun hideDescription() {
        descriptionContainer?.hide()
    }

    companion object {
        private const val stateEmpty = "empty"
        private const val stateNew_order = "new_order"
        private const val stateOngoing = "on_going"
        private const val stateFinish = "finish"

        private const val stateOpen = "Tutup"
        private const val stateClose = "Lihat"
    }
}