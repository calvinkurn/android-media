package com.tokopedia.buyerorderdetail.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify

class BuyerOrderDetailMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    private var onTransitionStarted: (() -> Unit)? = null
    private var onTransitionCompleted: (() -> Unit)? = null

    private val transitionListener = object : TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            onTransitionStarted?.invoke()
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onTransitionCompleted?.invoke()
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    }

    init {
        setTransitionListener(transitionListener)
    }

    private fun startTransition(
        start: Int,
        end: Int,
        onTransitionStarted: (() -> Unit)? = null,
        onTransitionEnd: (() -> Unit)? = null
    ) {
        setTransition(start, end)
        this.onTransitionStarted = onTransitionStarted
        this.onTransitionCompleted = onTransitionEnd
        transitionToEnd()
    }

    private fun startTransitionFromInitialToLoadingState() {
        startTransition(R.id.initial, R.id.loading, {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.show()
        })
    }

    private fun startTransitionFromErrorStateToLoadingState() {
        startTransition(R.id.error_state, R.id.error_state_loading_intermediary, {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.show()
        }, {
            startTransition(R.id.error_state_loading_intermediary, R.id.loading)
        })
    }

    private fun startTransitionFromEmptyStateErrorToLoadingState() {
        startTransition(R.id.empty_state_error, R.id.empty_state_error_loading_intermediary, {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.show()
        }, {
            startTransition(R.id.empty_state_error_loading_intermediary, R.id.loading)
        })
    }

    private fun startTransitionFromLoadingToShowContentWithStickyButton(onTransitionEnd: () -> Unit) {
        startTransition(R.id.loading, R.id.show_content_with_sticky_button, onTransitionEnd = {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).invisible()
            onTransitionEnd()
        })
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToShowContentWithStickyButton(
        onTransitionEnd: () -> Unit
    ) {
        startTransition(
            R.id.show_content_without_sticky_button,
            R.id.show_content_with_sticky_button,
            onTransitionEnd = onTransitionEnd
        )
    }

    private fun startTransitionFromLoadingToShowContentWithoutStickyButton() {
        startTransition(R.id.loading, R.id.show_content_without_sticky_button, onTransitionEnd = {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail).invisible()
        })
    }

    private fun startTransitionFromShowContentWithStickyButtonToShowContentWithoutStickyButton() {
        startTransition(
            R.id.show_content_with_sticky_button,
            R.id.show_content_without_sticky_button
        )
    }

    private fun startTransitionFromLoadingToErrorState() {
        startTransition(R.id.loading, R.id.error_state, onTransitionEnd = {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.invisible()
        })
    }

    private fun startTransitionFromShowContentWithStickyButtonToErrorState() {
        startTransition(
            start = R.id.show_content_with_sticky_button,
            end = R.id.show_content_error_state_intermediary
        )
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToErrorState() {
        startTransition(
            start = R.id.show_content_without_sticky_button,
            end = R.id.show_content_error_state_intermediary
        )
    }

    private fun startTransitionFromLoadingToEmptyStateError() {
        startTransition(R.id.loading, R.id.empty_state_error, onTransitionEnd = {
            findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.invisible()
        })
    }

    private fun startTransitionFromShowContentWithStickyButtonToEmptyStateError() {
        startTransition(
            start = R.id.show_content_with_sticky_button,
            end = R.id.show_content_empty_state_error_intermediary
        )
    }

    private fun startTransitionFromShowContentWithoutStickyButtonToEmptyStateError() {
        startTransition(
            start = R.id.show_content_without_sticky_button,
            end = R.id.show_content_empty_state_error_intermediary
        )
    }

    fun transitionToLoadingState() {
        when (currentState) {
            R.id.initial -> startTransitionFromInitialToLoadingState()
            R.id.error_state -> startTransitionFromErrorStateToLoadingState()
            R.id.empty_state_error -> startTransitionFromEmptyStateErrorToLoadingState()
            else -> {
                progress = 1f
                findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.show()
                onTransitionStarted = null
                onTransitionCompleted = null
                transitionToState(R.id.loading)
            }
        }
    }

    fun transitionToShowContentWithStickyButton(onTransitionEnd: () -> Unit) {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToShowContentWithStickyButton(onTransitionEnd)
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToShowContentWithStickyButton(onTransitionEnd)
            R.id.show_content_with_sticky_button -> onTransitionEnd()
            else -> {
                progress = 1f
                findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.invisible()
                onTransitionStarted = null
                onTransitionCompleted = onTransitionEnd
                transitionToState(R.id.show_content_with_sticky_button)
            }
        }
    }

    fun transitionToShowContentWithoutStickyButton(onTransitionEnd: () -> Unit) {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToShowContentWithoutStickyButton()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToShowContentWithoutStickyButton()
            R.id.show_content_without_sticky_button -> onTransitionEnd()
            else -> {
                progress = 1f
                findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.invisible()
                onTransitionStarted = null
                onTransitionCompleted = onTransitionEnd
                transitionToState(R.id.show_content_without_sticky_button)
            }
        }
    }

    fun transitionToErrorState() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToErrorState()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToErrorState()
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToErrorState()
            else -> {
                progress = 1f
                findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.invisible()
                onTransitionStarted = null
                onTransitionCompleted = null
                transitionToState(R.id.error_state)
            }
        }
    }

    fun transitionToEmptyStateError() {
        when (currentState) {
            R.id.loading -> startTransitionFromLoadingToEmptyStateError()
            R.id.show_content_with_sticky_button -> startTransitionFromShowContentWithStickyButtonToEmptyStateError()
            R.id.show_content_without_sticky_button -> startTransitionFromShowContentWithoutStickyButtonToEmptyStateError()
            else -> {
                progress = 1f
                findViewById<LoaderUnify>(R.id.loaderBuyerOrderDetail)?.invisible()
                onTransitionStarted = null
                onTransitionCompleted = null
                transitionToState(R.id.empty_state_error)
            }
        }
    }

    fun isStickyActionButtonsShowed() = currentState == R.id.show_content_with_sticky_button
}
