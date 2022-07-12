package com.tokopedia.sellerorder.list.presentation.adapter.viewholders.tablet

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListOrderViewHolder(
        itemView: View,
        listener: SomListOrderItemListener
) : com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder(itemView, listener) {

    companion object {
        const val TOGGLE_OPEN = "toggle_open"
    }

    private var fadeOutAnimation: ValueAnimator? = null
    private var fadeInAnimation: ValueAnimator? = null

    override fun bind(element: SomListOrderUiModel?) {
        fadeInAnimation?.cancel()
        fadeOutAnimation?.cancel()
        super.bind(element)
    }

    override fun bind(element: SomListOrderUiModel?, payloads: MutableList<Any>) {
        fadeInAnimation?.cancel()
        fadeOutAnimation?.cancel()
        payloads.firstOrNull()?.let {
            if (it is Bundle) {
                if (it.containsKey(TOGGLE_OPEN)) {
                    element?.let {
                        setupOrderCard(it)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun setupOrderCard(element: SomListOrderUiModel) {
        binding?.run {
            if ((element.multiSelectEnabled && element.cancelRequest != 0 && element.cancelRequestStatus != 0)) {
                cardSomOrder.animateFadeOut()
            } else {
                cardSomOrder.animateFadeIn()
            }
            if (element.isOpen) {
                somOrderListOpenIndicator?.show()
            } else {
                somOrderListOpenIndicator?.gone()
            }
            root.setOnClickListener {
                if (element.multiSelectEnabled) touchCheckBox(element)
                else listener.onOrderClicked(element)
            }
        }
    }

    override fun setupQuickActionButton(element: SomListOrderUiModel) {
        // Noop, tablet mode doesn't have quick action button
    }

    override fun onBindFinished(element: SomListOrderUiModel) {
        // Noop, tablet mode doesn't have quick action button
    }

    private fun View?.animateFade(start: Float, end: Float): ValueAnimator {
        return ValueAnimator.ofFloat(start, end).apply {
            duration = 300L
            addUpdateListener { value ->
                this@animateFade?.alpha = value.animatedValue as Float
            }
            start()
        }
    }

    private fun View.animateFadeOut() {
        if (fadeOutAnimation?.isRunning == true) return
        fadeInAnimation?.cancel()
        fadeOutAnimation = animateFade(alpha, 0.5f)
    }

    private fun View.animateFadeIn() {
        if (fadeInAnimation?.isRunning == true) return
        fadeOutAnimation?.cancel()
        fadeInAnimation = animateFade(alpha, 1f)
    }
}