package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BuyerOrderDetailMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val transitionQueue = Channel<TransitionQueue>()
    private val onTransitionEnd = Channel<Int>()

    private val transitionListener = object : TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onTransitionEnd.offer(0)
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    }

    init {
        setTransitionListener(transitionListener)
        initTransitionQueue()
        transitionToState(R.id.initial)
    }

    private fun initTransitionQueue() {
        launch {
            transitionQueue.consumeAsFlow()
                .collect {
                    it.onTransitionStart?.invoke()
                    setTransition(getTransition(it.transitionId))
                    transitionToEnd()
                    onTransitionEnd.receive()
                    it.onTransitionCompleted?.invoke()
                }
        }
    }

    private fun startTransitionFromInitialToLoadingState(onTransitionEnd: () -> Unit) {
        launch {
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailInitialToLoading,
                    onTransitionCompleted = onTransitionEnd,
                    onTransitionStart = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).show()
                    })
            )
        }
    }

    private fun startTransitionFromErrorStateToLoadingState(onTransitionEnd: () -> Unit) {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateToErrorStateLoadingIntermediary))
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailErrorStateLoadingIntermediaryToLoading,
                    onTransitionCompleted = onTransitionEnd,
                    onTransitionStart = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).show()
                    }
                )
            )
        }
    }

    private fun startTransitionFromEmptyStateErrorToLoadingState(onTransitionEnd: () -> Unit) {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorToEmptyStateErrorIntermediary))
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailEmptyStateErrorLoadingIntermediaryToLoading,
                    onTransitionCompleted = onTransitionEnd,
                    onTransitionStart = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).show()
                    }
                ))
        }
    }

    private fun startTransitionFromLoadingToShowContentWithStickyButton() {
        launch {
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailLoadingToShowContentWithStickyButton,
                    onTransitionCompleted = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).invisible()
                    })
            )
        }
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToShowContentWithStickyButton() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailShowContentWithoutStickyButtonToShowContentWithStickyButton))
        }
    }

    private fun startTransitionFromErrorStateToShowContentWithStickyButton() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateToErrorStateIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateIntermediaryToShowContentWithStickyButton))
        }
    }

    private fun startTransitionFromEmptyStateErrorToShowContentWithStickyButton() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorToEmptyStateErrorIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorIntermediaryToShowContentWithStickyButton))
        }
    }

    private fun startTransitionFromLoadingToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailLoadingToShowContentWithoutStickyButton,
                    onTransitionCompleted = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).invisible()
                    })
            )
        }
    }

    private fun startTransitionFromShowContentWithStickyButtonToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailShowContentWithStickyButtonToShowContentWithoutStickyButton))
        }
    }

    private fun startTransitionFromErrorStateToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateToErrorStateIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateIntermediaryToShowContentWithoutStickyButton))
        }
    }

    private fun startTransitionFromEmptyStateErrorToShowContentWithoutStickyButton() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorToEmptyStateErrorIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorIntermediaryToShowContentWithoutStickyButton))
        }
    }

    private fun startTransitionFromLoadingToErrorState() {
        launch {
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailLoadingToErrorState,
                    onTransitionCompleted = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).invisible()
                    })
            )
        }
    }

    private fun startTransitionFromShowContentWithStickyButtonToErrorState() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailShowContentWithStickyButtonToErrorStateIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateIntermediaryToErrorState))
        }
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToErrorState() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailShowContentWithoutStickyButtonToErrorStateIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailErrorStateIntermediaryToErrorState))
        }
    }

    private fun startTransitionFromLoadingToEmptyStateError() {
        launch {
            transitionQueue.send(
                TransitionQueue(
                    transitionId = R.id.buyerOrderDetailLoadingToEmptyStateError,
                    onTransitionCompleted = {
                        findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).invisible()
                    })
            )
        }
    }

    private fun startTransitionFromShowContentWithStickyButtonToEmptyStateError() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailShowContentWithStickyButtonToEmptyStateErrorIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorIntermediaryToEmptyStateError))
        }
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToEmptyStateError() {
        launch {
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailShowContentWithoutStickyButtonToEmptyStateErrorIntermediary))
            transitionQueue.send(TransitionQueue(transitionId = R.id.buyerOrderDetailEmptyStateErrorIntermediaryToEmptyStateError))
        }
    }

    fun transitionToLoadingState(onTransitionEnd: () -> Unit) {
        when (currentState) {
            R.id.initial -> startTransitionFromInitialToLoadingState(onTransitionEnd)
            R.id.error_state -> startTransitionFromErrorStateToLoadingState(onTransitionEnd)
            R.id.empty_state_error -> startTransitionFromEmptyStateErrorToLoadingState(
                onTransitionEnd
            )
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
        }
    }

    fun transitionToEmptyStateError() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToEmptyStateError()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToEmptyStateError()
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToEmptyStateError()
        }
    }

    data class TransitionQueue(
        val transitionId: Int,
        val onTransitionCompleted: (() -> Unit)? = null,
        val onTransitionStart: (() -> Unit)? = null
    )
}