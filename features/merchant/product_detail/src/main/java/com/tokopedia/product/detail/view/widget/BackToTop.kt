package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.databinding.WidgetBackToTopBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class BackToTop(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    companion object {
        private const val BUTTON_LABEL = "back-to-top"
        private const val BUTTON_POSITION = 0
    }

    private val binding = WidgetBackToTopBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root

    private var recyclerView: RecyclerView? = null
    private var listener: DynamicProductDetailListener? = null

    private val smoothScroller = SmoothScroller(context)
    private val onScrollListener = OnScrollListener()

    private var impressNavigation = false
    private var isVisibile = false

    init {
        addView(view)
        view.setOnClickListener {
            listener?.onClickProductDetailnavigation(BUTTON_POSITION, BUTTON_LABEL)
            smoothScrollToTop()
            isClickable = false
        }
    }

    fun start(recyclerView: RecyclerView, listener: DynamicProductDetailListener) {
        recyclerView.removeOnScrollListener(onScrollListener)

        this.listener = listener
        recyclerView.addOnScrollListener(onScrollListener)
        this.recyclerView = recyclerView
    }

    fun stop(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
        toggle(false)
    }

    private fun toggle(show: Boolean) {

        if (isVisibile == show) return

        val scale = if (show) 1f else 0f

        view.animate().scaleX(scale).scaleY(scale).duration = 265
        isVisibile = show

        if (!impressNavigation && show) {
            listener?.onImpressBackToTop(BUTTON_LABEL)
            impressNavigation = true
        }
    }

    private fun enableTouchScroll(isEnabled: Boolean) {
        recyclerView?.suppressLayout(!isEnabled)
    }

    private fun smoothScrollToTop() {
        recyclerView?.apply {
            smoothScroller.targetPosition = 0
            enableTouchScroll(false)
            layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                enableTouchScroll(true)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            resolveButtonVisibility(recyclerView)
        }

        private fun resolveButtonVisibility(recyclerView: RecyclerView) {
            val layoutManager = recyclerView.layoutManager
            if (layoutManager !is LinearLayoutManager) return

            if (layoutManager.findFirstVisibleItemPosition() == 0) {
                toggle(false)
                isClickable = true
            } else toggle(true)
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
        }

    }
}