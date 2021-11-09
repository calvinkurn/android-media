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
) : MotionLayout(context, attrs, defStyleAttr) {

    private var viewModel: BuyerOrderDetailViewModel? = null
    private var navigator: BuyerOrderDetailNavigator? = null

    private var chatIcon: IconUnify? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindViews()
        setClickListeners()
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

    private fun startTransition(
        start: Int,
        end: Int
    ) {
        setTransition(start, end)
        transitionToEnd()
    }

    private fun startTransitionFromShowChatIconToInitial() {
        startTransition(R.id.show_chat_icon, R.id.initial)
    }

    private fun startTransitionFromInitialToShowChatIcon() {
        startTransition(R.id.initial, R.id.show_chat_icon)
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