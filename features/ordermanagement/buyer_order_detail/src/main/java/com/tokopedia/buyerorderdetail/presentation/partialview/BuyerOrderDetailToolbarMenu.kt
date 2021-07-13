package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BuyerOrderDetailToolbarMenu @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), CoroutineScope {
    companion object {
        private const val NO_TRANSITION = -1
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val transitionQueue = MutableStateFlow(NO_TRANSITION)
    private val onTransitionEnd = Channel<Int>()

    private var viewModel: BuyerOrderDetailViewModel? = null
    private var navigator: BuyerOrderDetailNavigator? = null

    private var chatIcon: IconUnify? = null

    private val transitionListener = object : TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onTransitionEnd.offer(0)
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        }
    }

    init {
        setTransitionListener(transitionListener)
        initTransitionQueue()
        transitionToState(R.id.initial)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindViews()
        setClickListeners()
    }

    private fun initTransitionQueue() {
        launch {
            transitionQueue.collect {
                if (it != NO_TRANSITION) {
                    setTransition(getTransition(it))
                    transitionToEnd()
                    onTransitionEnd.receive()
                }
            }
        }
    }

    private fun bindViews() {
        chatIcon = findViewById(R.id.buyerOrderDetailChatMenu)
    }

    private fun setClickListeners() {
        chatIcon?.setOnClickListener {
            viewModel?.buyerOrderDetailResult?.value?.let {
                if (it is Success) {
                    navigator?.goToAskSeller(it.data)
                    BuyerOrderDetailTracker.eventClickChatIcon(
                            it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId,
                            it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId)
                }
            }
        }
    }

    private fun startTransitionFromShowChatIconToInitial() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowChatIconToInitial)
        }
    }

    private fun startTransitionFromInitialToShowChatIcon() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailInitialToShowChatIcon)
        }
    }

    fun transitionToEmpty() {
        when(currentState) {
            R.id.show_chat_icon -> startTransitionFromShowChatIconToInitial()
        }
    }

    fun transitionToShowChatIcon() {
        when (currentState) {
            R.id.initial -> startTransitionFromInitialToShowChatIcon()
        }
    }

    fun setViewModel(viewModel: BuyerOrderDetailViewModel) {
        this.viewModel = viewModel
    }

    fun setNavigator(navigator: BuyerOrderDetailNavigator) {
        this.navigator = navigator
    }
}