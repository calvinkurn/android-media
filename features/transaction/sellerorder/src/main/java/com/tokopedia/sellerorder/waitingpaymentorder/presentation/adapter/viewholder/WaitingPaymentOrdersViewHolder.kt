package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
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
        itemView: View?,
        private val listener: LoadUnloadMoreProductClickListener
) : AbstractViewHolder<WaitingPaymentOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders
        const val MAX_ORDER_WHEN_COLLAPSED = 5
        const val RECYCLER_VIEW_ANIMATION_DURATION = 250L
    }

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    private var iconDropDownAnimator: ValueAnimator? = null

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel?) {
        element?.let { element ->
            with(itemView) {
                tvValuePaymentDeadline.text = element.paymentDeadline
                tvValueBuyerNameAndPlace.text = element.buyerNameAndPlace
                tvToggleCollapseMoreProducts.apply {
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                    updateToggleCollapseText(element.isExpanded)
                    setOnClickListener {
                        element.isExpanded = !element.isExpanded
                        listener.toggleCollapse(element)
                        animateDropDownIcon(element.isExpanded)
                    }
                }
                icLoadMoreDropDown.apply {
                    iconDropDownAnimator?.end()
                    rotation = if (element.isExpanded) -180f else 0f
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                }
                rvWaitingPaymentOrderProducts.apply {
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        itemAnimator?.addDuration = RECYCLER_VIEW_ANIMATION_DURATION
                        itemAnimator?.removeDuration = RECYCLER_VIEW_ANIMATION_DURATION
                        itemAnimator?.changeDuration = RECYCLER_VIEW_ANIMATION_DURATION
                        itemAnimator?.moveDuration = RECYCLER_VIEW_ANIMATION_DURATION
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(getShownProducts(element.isExpanded, element.productUiModels))
                }
            }
        }
    }

    private fun animateDropDownIcon(isExpanded: Boolean) {
        if (isExpanded) {
            animateDropDownIconIcon(0f, -180f)
        } else {
            animateDropDownIconIcon(-180f, 0f)
        }
    }

    private fun animateDropDownIconIcon(start: Float, end: Float) {
        iconDropDownAnimator = ValueAnimator.ofFloat(start, end)
        iconDropDownAnimator?.duration = RECYCLER_VIEW_ANIMATION_DURATION
        iconDropDownAnimator?.addUpdateListener { animation ->
            itemView.icLoadMoreDropDown.rotation = animation.animatedValue as Float
        }
        iconDropDownAnimator?.start()
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel?, payloads: MutableList<Any>) {
        element?.let { element ->
            if (payloads.getOrNull(0) !is Boolean) return
            (itemView as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            itemView.layoutTransition.setInterpolator(LayoutTransition.CHANGING, LinearInterpolator())
            itemView.rvWaitingPaymentOrderProducts.addOneTimeGlobalLayoutListener {
                itemView.layoutTransition.disableTransitionType(LayoutTransition.CHANGING)
            }
            this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(getShownProducts(element.isExpanded, element.productUiModels))
            updateToggleCollapseText(element.isExpanded)
        }
    }

    private fun getShownProducts(isExpanded: Boolean, productUiModels: List<WaitingPaymentOrderUiModel.ProductUiModel>): List<WaitingPaymentOrderUiModel.ProductUiModel> {
        return if (isExpanded) {
            productUiModels
        } else {
            productUiModels.take(MAX_ORDER_WHEN_COLLAPSED)
        }
    }

    private fun updateToggleCollapseText(expanded: Boolean) {
        itemView.tvToggleCollapseMoreProducts.text = if (expanded) {
            itemView.context.getString(R.string.waiting_payment_orders_less_product)
        } else {
            itemView.context.getString(R.string.waiting_payment_orders_more_products)
        }
    }

    interface LoadUnloadMoreProductClickListener {
        fun toggleCollapse(waitingPaymentOrderUiModel: WaitingPaymentOrderUiModel)
    }
}