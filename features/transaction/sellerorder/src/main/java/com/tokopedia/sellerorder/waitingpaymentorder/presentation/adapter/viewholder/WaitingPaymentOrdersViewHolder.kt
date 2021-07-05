package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderProductsAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import kotlinx.android.synthetic.main.item_waiting_payment_orders.view.*

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrdersViewHolder(
        itemView: View?
) : AbstractViewHolder<WaitingPaymentOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders
        const val MAX_ORDER_WHEN_COLLAPSED = 5
        const val RECYCLER_VIEW_ANIMATION_DURATION = 300L
    }

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    private var iconDropdownAnimator: ValueAnimator? = null
    private var recyclerViewAnimator: ValueAnimator? = null
    private var animatorSet: AnimatorSet = AnimatorSet()

    private val animationListener = object: Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator?) {
            itemView?.setHasTransientState(false)
        }

        override fun onAnimationCancel(animation: Animator?) {
            itemView?.setHasTransientState(false)
        }

        override fun onAnimationStart(animation: Animator?) {
            itemView?.setHasTransientState(true)
        }

        override fun onAnimationRepeat(animation: Animator?) {
            itemView?.setHasTransientState(true)
        }
    }

    init {
        animatorSet.addListener(animationListener)
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel?) {
        element?.let { element ->
            with(itemView) {
                tvValuePaymentDeadline.text = element.paymentDeadline
                tvValueBuyerNameAndPlace.text = element.buyerNameAndPlace
                tvToggleCollapseMoreProducts.apply {
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                    updateToggleCollapseText(element.isExpanded)
                    setLoadUnloadMoreClickListener(element)
                }
                icLoadMoreDropDown.apply {
                    iconDropdownAnimator?.end()
                    rotation = if (element.isExpanded) -180f else 0f
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                }
                rvWaitingPaymentOrderProducts.apply {
                    recyclerViewAnimator?.end()
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    val layoutParams = layoutParams
                    layoutParams.height = getRecyclerViewHeight(element.isExpanded, element.productUiModels.size)
                    this.layoutParams = layoutParams
                    this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(element.productUiModels)
                    setHasFixedSize(true)
                }
            }
        }
    }

    private fun getRecyclerViewHeight(isExpanded: Boolean, productUiModelsSize: Int): Int {
        return itemView.run {
            val productCount = if (!isExpanded) productUiModelsSize.coerceAtMost(MAX_ORDER_WHEN_COLLAPSED) else productUiModelsSize
            productCount * (getDimens(R.dimen.waiting_order_product_height))
        }
    }

    private fun setLoadUnloadMoreClickListener(element: WaitingPaymentOrderUiModel) {
        itemView.setOnClickListener {
            animatorSet.end()
            element.isExpanded = !element.isExpanded
            setupRecyclerViewSizeAnimator(
                    itemView.rvWaitingPaymentOrderProducts.height,
                    getRecyclerViewHeight(element.isExpanded, element.productUiModels.size))
            setupDropdownIconAnimator(element.isExpanded)
            updateToggleCollapseText(element.isExpanded)
            animatorSet.playTogether(recyclerViewAnimator, iconDropdownAnimator)
            animatorSet.start()
        }
    }

    private fun setupRecyclerViewSizeAnimator(from: Int, to: Int) {
        recyclerViewAnimator = ValueAnimator.ofInt(from, to)
        recyclerViewAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        recyclerViewAnimator?.addUpdateListener { animation ->
            val layoutParams = itemView.rvWaitingPaymentOrderProducts?.layoutParams
            layoutParams?.let {
                layoutParams.height = animation.animatedValue as Int
                itemView.rvWaitingPaymentOrderProducts?.layoutParams = layoutParams
            }
        }
    }

    private fun setupDropdownIconAnimator(isExpanded: Boolean) {
        if (isExpanded) {
            setupDropdownIconAnimator(0f, -180f)
        } else {
            setupDropdownIconAnimator(-180f, 0f)
        }
    }

    private fun setupDropdownIconAnimator(start: Float, end: Float) {
        iconDropdownAnimator = ValueAnimator.ofFloat(start, end)
        iconDropdownAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        iconDropdownAnimator?.addUpdateListener { animation ->
            itemView.icLoadMoreDropDown?.rotation = animation.animatedValue as Float
        }
    }

    private fun updateToggleCollapseText(expanded: Boolean) {
        itemView.tvToggleCollapseMoreProducts.text = if (expanded) {
            itemView.context.getString(R.string.waiting_payment_orders_less_product)
        } else {
            itemView.context.getString(R.string.waiting_payment_orders_more_products)
        }
    }
}