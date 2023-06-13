package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.iconunify.IconUnify

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
            viewModel?.buyerOrderDetailUiState?.value?.let { uiState ->
                if (uiState is BuyerOrderDetailUiState.HasData) {
                    navigator?.goToAskSeller(
                        orderStatusUiModel = uiState.orderStatusUiState.data,
                        productListUiModel = uiState.productListUiState.data,
                        paymentInfoUiModel = uiState.paymentInfoUiState.data
                    )
                    BuyerOrderDetailTracker.eventClickChatIcon(
                        orderStatusCode = uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderStatusId,
                        orderId = uiState.orderStatusUiState.data.orderStatusHeaderUiModel.orderId
                    )
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
