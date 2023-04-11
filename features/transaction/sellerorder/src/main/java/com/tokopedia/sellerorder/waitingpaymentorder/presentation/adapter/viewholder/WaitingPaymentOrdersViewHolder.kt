package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemWaitingPaymentOrdersBinding
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderProductsAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrdersViewHolder(
        itemView: View
) : AbstractViewHolder<WaitingPaymentOrderUiModel>(itemView) {

    companion object {
        private const val MAX_ORDER_WHEN_COLLAPSED = 1
        private const val RECYCLER_VIEW_ANIMATION_DURATION = 300L
        private const val EXPANDED_DROPDOWN_ICON_ROTATION = -180f
        private const val COLLAPSED_DROPDOWN_ICON_ROTATION = 0f

        val LAYOUT = R.layout.item_waiting_payment_orders
    }

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    private var iconDropdownAnimator: ValueAnimator? = null
    private var recyclerViewAnimator: ValueAnimator? = null
    private var animatorSet: AnimatorSet = AnimatorSet()

    private val animationListener = object: Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator) {
            binding?.root?.setHasTransientState(false)
        }

        override fun onAnimationCancel(animation: Animator) {
            binding?.root?.setHasTransientState(false)
        }

        override fun onAnimationStart(animation: Animator) {
            binding?.root?.setHasTransientState(true)
        }

        override fun onAnimationRepeat(animation: Animator) {
            binding?.root?.setHasTransientState(true)
        }
    }

    private val binding by viewBinding<ItemWaitingPaymentOrdersBinding>()

    init {
        animatorSet.addListener(animationListener)
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel?) {
        element?.let { element ->
            binding?.run {
                animatorSet.end()
                tvValuePaymentDeadline.text = element.paymentDeadline
                tvValueBuyerNameAndPlace.text = element.buyerNameAndPlace
                tvToggleCollapseMoreProducts.apply {
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                    updateToggleCollapseText(element.isExpanded)
                    setLoadUnloadMoreClickListener(element)
                }
                icLoadMoreDropDown.apply {
                    iconDropdownAnimator?.end()
                    rotation = if (element.isExpanded) EXPANDED_DROPDOWN_ICON_ROTATION
                    else COLLAPSED_DROPDOWN_ICON_ROTATION
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                }
                rvWaitingPaymentOrderProducts.apply {
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    val layoutParams = binding?.rvWaitingPaymentOrderProducts?.layoutParams
                    layoutParams?.let {
                        it.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        binding?.rvWaitingPaymentOrderProducts?.layoutParams = it
                    }
                    updateProducts(element)
                }
            }
        }
    }

    private fun updateProducts(element: WaitingPaymentOrderUiModel) {
        this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(element.productUiModels.take(if (element.isExpanded) element.productUiModels.size else MAX_ORDER_WHEN_COLLAPSED))
    }

    private fun setLoadUnloadMoreClickListener(element: WaitingPaymentOrderUiModel) {
        binding?.run {
            root.setOnClickListener {
                animatorSet.end()
                element.isExpanded = !element.isExpanded
                rvWaitingPaymentOrderProducts.layoutParams.height = rvWaitingPaymentOrderProducts.height
                updateProducts(element)
                scheduleAnimation(element)
            }
        }
    }

    private fun scheduleAnimation(element: WaitingPaymentOrderUiModel) {
        binding?.rvWaitingPaymentOrderProducts?.run {
            post {
                val initialHeight = height.orZero()
                val targetHeight = getRecyclerViewHeight(element)
                if (initialHeight == targetHeight) {
                    scheduleAnimation(element)
                } else {
                    if (element.isExpanded) element.expandedHeight = targetHeight
                    else element.collapsedHeight = targetHeight
                    setupRecyclerViewSizeAnimator(initialHeight, targetHeight)
                    setupDropdownIconAnimator(element.isExpanded)
                    updateToggleCollapseText(element.isExpanded)
                    animatorSet.playTogether(recyclerViewAnimator, iconDropdownAnimator)
                    animatorSet.start()
                }
            }
        }
    }

    private fun getRecyclerViewHeight(element: WaitingPaymentOrderUiModel): Int {
        return when {
            element.isExpanded && element.expandedHeight != Int.ZERO -> element.expandedHeight
            !element.isExpanded && element.collapsedHeight != Int.ZERO -> element.collapsedHeight
            else -> {
                binding?.run {
                    rvWaitingPaymentOrderProducts.measure(View.MeasureSpec.makeMeasureSpec(
                        root.measuredWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.UNSPECIFIED)
                    rvWaitingPaymentOrderProducts.measuredHeight
                }.orZero()
            }
        }
    }

    private fun setupRecyclerViewSizeAnimator(from: Int, to: Int) {
        recyclerViewAnimator = ValueAnimator.ofInt(from, to)
        recyclerViewAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        recyclerViewAnimator?.addUpdateListener { animation ->
            val layoutParams = binding?.rvWaitingPaymentOrderProducts?.layoutParams
            layoutParams?.let {
                it.height = animation.animatedValue as Int
                binding?.rvWaitingPaymentOrderProducts?.layoutParams = it
            }
        }
    }

    private fun setupDropdownIconAnimator(isExpanded: Boolean) {
        if (isExpanded) {
            setupDropdownIconAnimator(COLLAPSED_DROPDOWN_ICON_ROTATION, EXPANDED_DROPDOWN_ICON_ROTATION)
        } else {
            setupDropdownIconAnimator(EXPANDED_DROPDOWN_ICON_ROTATION, COLLAPSED_DROPDOWN_ICON_ROTATION)
        }
    }

    private fun setupDropdownIconAnimator(start: Float, end: Float) {
        iconDropdownAnimator = ValueAnimator.ofFloat(start, end)
        iconDropdownAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        iconDropdownAnimator?.addUpdateListener { animation ->
            binding?.icLoadMoreDropDown?.rotation = animation.animatedValue as Float
        }
    }

    private fun updateToggleCollapseText(expanded: Boolean) {
        binding?.run {
            tvToggleCollapseMoreProducts.text = if (expanded) {
                root.context.getString(R.string.waiting_payment_orders_less_product)
            } else {
                root.context.getString(R.string.waiting_payment_orders_more_products)
            }
        }
    }
}
