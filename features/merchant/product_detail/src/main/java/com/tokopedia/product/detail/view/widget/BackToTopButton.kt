package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.detail.databinding.WidgetBackToTopBinding
import com.tokopedia.product.detail.view.widget.ProductDetailNavigation.Companion.calculateFirstVisibleItemPosition

class BackToTopButton(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    companion object {
        private const val BUTTON_LABEL = "back-to-top"
        private const val BUTTON_POSITION = 0

        private const val BUTTON_ANIMATION_DURATION = 265L
        private const val BACK_TO_TOP_SHOW_THRESHOLD = 75f
    }

    private val binding = WidgetBackToTopBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root

    private var recyclerView: RecyclerView? = null
    private var listener: NavigationListener? = null
    private var config: ProductDetailNavigation.Configuration? = null

    private val smoothScroller = SmoothScroller(context)
    private val onScrollListener = OnScrollListener()

    private var impressNavigation = false
    private var isVisible = false
    private var enableClick = true
    private var enableBlockingTouch = true

    init {
        addView(view)
        view.setOnClickListener {
            if (!enableClick) return@setOnClickListener
            listener?.onClickBackToTop(BUTTON_POSITION, BUTTON_LABEL)
            smoothScrollToTop()
            if (enableBlockingTouch) enableClick = false
        }
    }

    fun start(
        recyclerView: RecyclerView,
        enableBlockingTouch: Boolean,
        listener: NavigationListener,
        config: ProductDetailNavigation.Configuration
    ) {
        recyclerView.removeOnScrollListener(onScrollListener)

        this.listener = listener
        this.config = config
        recyclerView.addOnScrollListener(onScrollListener)
        this.recyclerView = recyclerView
        this.enableBlockingTouch = enableBlockingTouch
    }

    fun stop(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
        toggle(false)
    }

    fun onClickTab() {
        enableClick = true
    }

    private fun toggle(show: Boolean) {

        if (isVisible == show) return

        val scale = if (show) 1f else 0f

        view.animate().scaleX(scale).scaleY(scale).duration = BUTTON_ANIMATION_DURATION
        isVisible = show

        if (!impressNavigation && show) {
            listener?.onImpressionBackToTop(BUTTON_LABEL)
            impressNavigation = true
        }
    }

    private fun enableTouchScroll(isEnable: Boolean) {
        if (enableBlockingTouch) {
            recyclerView?.suppressLayout(!isEnable)
        } else recyclerView?.suppressLayout(false)
    }

    private fun smoothScrollToTop() {
        recyclerView?.apply {
            smoothScroller.targetPosition = 0
            enableTouchScroll(false)
            layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        val threshold = BACK_TO_TOP_SHOW_THRESHOLD.toPx().toInt()

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                enableTouchScroll(true)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            resolveButtonVisibility(recyclerView)
        }

        private fun resolveButtonVisibility(recyclerView: RecyclerView) {
            val shouldHide = if (config is ProductDetailNavigation.Configuration.Navbar4) {
                val scrollOffset = recyclerView.computeVerticalScrollOffset()
                scrollOffset < threshold
            } else calculateFirstVisibleItemPosition(recyclerView, config?.offsetY.orZero()) == 0
            toggle(!shouldHide)
        }
    }

    private inner class SmoothScroller(context: Context) : LinearSmoothScroller(context) {

        private var isScroll = false

        override fun onSeekTargetStep(dx: Int, dy: Int, state: RecyclerView.State, action: Action) {
            super.onSeekTargetStep(dx, dy, state, action)
            isScroll = true
        }

        override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
            super.onTargetFound(targetView, state, action)
            if (!isScroll) enableTouchScroll(true)
            isScroll = false
            enableClick = true
        }

    }
}
