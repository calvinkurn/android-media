package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderProductsAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import kotlinx.android.synthetic.main.item_waiting_payment_orders.view.*

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrdersViewHolder(itemView: View?) : AbstractViewHolder<WaitingPaymentOrder>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders
        const val MAX_ORDER_WHEN_COLLAPSED = 5
    }

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    private var products: List<WaitingPaymentOrder.Product> = emptyList()

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrder?) {
        element?.let { element ->
            products = element.products
            with(itemView) {
                tvValuePaymentDeadline.text = element.paymentDeadline
                tvValueBuyerNameAndPlace.text = element.buyerNameAndPlace
                tvToggleCollapseMoreProducts.apply {
                    showWithCondition(element.products.size > MAX_ORDER_WHEN_COLLAPSED)
                    updateToggleCollapseText(element.isExpanded)
                    setOnClickListener {
                        itemView.waitingPaymentOrderContainer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        if (!element.isExpanded) {
                            showMoreProducts()
                            element.isExpanded = true
                        } else {
                            collapseMoreProducts()
                            element.isExpanded = false
                        }
                        itemView.waitingPaymentOrderContainer.addOneTimeGlobalLayoutListener {
                            itemView.waitingPaymentOrderContainer.layoutTransition.disableTransitionType(LayoutTransition.CHANGING)
                        }
                    }
                }
                rvWaitingPaymentOrderProducts.apply {
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(getShownProducts(element))
                    itemAnimator?.addDuration = 500
                    itemAnimator?.removeDuration = 500
                    itemAnimator?.changeDuration = 500
                    itemAnimator?.moveDuration = 500
                }
            }
        }
    }

    private fun updateToggleCollapseText(expanded: Boolean) {
        itemView.tvToggleCollapseMoreProducts.text = if (expanded) {
            itemView.context.getString(R.string.waiting_payment_orders_less_product)
        } else {
            itemView.context.getString(R.string.waiting_payment_orders_more_products)
        }
    }

    private fun getShownProducts(element: WaitingPaymentOrder): List<WaitingPaymentOrder.Product> {
        return if (element.isExpanded) {
            products
        } else {
            products.take(MAX_ORDER_WHEN_COLLAPSED)
        }
    }

    private fun collapseMoreProducts() {
//        val diff = adapter.dataSize - MAX_ORDER_WHEN_COLLAPSED
//        val delay = (COLLAPSE_EXPAND_DURATION / diff).coerceAtMost(FRAME_TIME)
//        for (i in 1..adapter.dataSize - MAX_ORDER_WHEN_COLLAPSED) {
//            Handler().postDelayed({
//                adapter.updateProducts(products.take(products.size - i))
//                if (i == MAX_ORDER_WHEN_COLLAPSED) {
//                    itemView.tvToggleCollapseMoreProducts.isClickable = true
//                }
//            }, (delay * i))
//        }
        adapter.updateProducts(products.take(MAX_ORDER_WHEN_COLLAPSED))
        updateToggleCollapseText(false)
    }

    private fun showMoreProducts() {
//        val diff = products.size - MAX_ORDER_WHEN_COLLAPSED
//        val delay = (COLLAPSE_EXPAND_DURATION / diff).coerceAtMost(FRAME_TIME)
//        for (i in (MAX_ORDER_WHEN_COLLAPSED + 1)..products.size) {
//            Handler().postDelayed({
//                adapter.updateProducts(products.take(i))
//                if (i == products.size) {
//                    itemView.tvToggleCollapseMoreProducts.isClickable = true
//                }
//            }, (delay * (i - diff)))
//        }
        adapter.updateProducts(products)
        updateToggleCollapseText(true)
    }
}