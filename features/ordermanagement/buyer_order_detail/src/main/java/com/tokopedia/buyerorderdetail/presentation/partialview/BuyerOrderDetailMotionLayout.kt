package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.buyerorderdetail.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BuyerOrderDetailMotionLayout @JvmOverloads constructor(
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

    private var onTransitionCompleted = {}

    private val transitionListener = object : TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onTransitionCompleted()
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

    private fun startTransitionFromInitialToLoadingState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailInitialToLoading)
        }
    }

    private fun startTransitionFromErrorStateToLoadingState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateToLoading)
        }
    }

    private fun startTransitionFromEmptyStateErrorToLoadingState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorToLoading)
        }
    }

    private fun startTransitionFromLoadingToShowContentWithStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailLoadingToShowContentWithStickyButton)
        }
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToShowContentWithStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowContentWithoutStickyButtonToShowContentWithStickyButton)
        }
    }

    private fun startTransitionFromErrorStateToShowContentWithStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateToErrorStateIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateIntermediaryToShowContentWithStickyButton)
        }
    }

    private fun startTransitionFromEmptyStateErrorToShowContentWithStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorToEmptyStateErrorIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorIntermediaryToShowContentWithStickyButton)
        }
    }

    private fun startTransitionFromLoadingToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailLoadingToShowContentWithoutStickyButton)
        }
    }

    private fun startTransitionFromShowContentWithStickyButtonToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowContentWithStickyButtonToShowContentWithoutStickyButton)
        }
    }

    private fun startTransitionFromErrorStateToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateToErrorStateIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateIntermediaryToShowContentWithoutStickyButton)
        }
    }

    private fun startTransitionFromEmptyStateErrorToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorToEmptyStateErrorIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorIntermediaryToShowContentWithoutStickyButton)
        }
    }

    private fun startTransitionFromLoadingToErrorState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailLoadingToErrorState)
        }
    }

    private fun startTransitionFromShowContentWithStickyButtonToErrorState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowContentWithStickyButtonToErrorStateIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateIntermediaryToErrorState)
        }
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToErrorState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowContentWithoutStickyButtonToErrorStateIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateIntermediaryToErrorState)
        }
    }

    private fun startTransitionFromEmptyStateErrorToErrorState() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorToErrorState)
        }
    }

    private fun startTransitionFromLoadingToEmptyStateError() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailLoadingToEmptyStateError)
        }
    }

    private fun startTransitionFromShowContentWithStickyButtonToEmptyStateError() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowContentWithStickyButtonToEmptyStateErrorIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorIntermediaryToEmptyStateError)
        }
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToEmptyStateError() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailShowContentWithoutStickyButtonToEmptyStateErrorIntermediary)
            transitionQueue.emit(R.id.buyerOrderDetailEmptyStateErrorIntermediaryToEmptyStateError)
        }
    }

    private fun startTransitionFromErrorStateToEmptyStateError() {
        launch {
            transitionQueue.emit(R.id.buyerOrderDetailErrorStateToEmptyStateError)
        }
    }

    fun transitionToLoadingState() {
        when (currentState) {
            R.id.initial -> startTransitionFromInitialToLoadingState()
            R.id.error_state -> startTransitionFromErrorStateToLoadingState()
            R.id.empty_state_error -> startTransitionFromEmptyStateErrorToLoadingState()
        }
    }

    fun transitionToShowContentWithStickyButton() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToShowContentWithStickyButton()
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToShowContentWithStickyButton()
            R.id.error_state -> startTransitionFromErrorStateToShowContentWithStickyButton()
            R.id.empty_state_error -> startTransitionFromEmptyStateErrorToShowContentWithStickyButton()
        }
    }

    fun transitionToShowContentWithoutStickyButton() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToShowContentWithoutStickyButton()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToShowContentWithoutStickyButton()
            R.id.error_state -> startTransitionFromErrorStateToShowContentWithoutStickyButton()
            R.id.empty_state_error -> startTransitionFromEmptyStateErrorToShowContentWithoutStickyButton()
        }
    }

    fun transitionToErrorState() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToErrorState()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToErrorState()
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToErrorState()
            R.id.empty_state_error -> startTransitionFromEmptyStateErrorToErrorState()
        }
    }

    fun transitionToEmptyStateError() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToEmptyStateError()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToEmptyStateError()
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToEmptyStateError()
            R.id.error_state -> startTransitionFromErrorStateToEmptyStateError()
        }
    }

    fun setOnTransitionCompleted(action: () -> Unit) {
        onTransitionCompleted = action
    }
}