package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.os.Handler
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderProductsAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import kotlinx.android.synthetic.main.item_waiting_payment_orders.view.*

class WaitingPaymentOrdersViewHolder(itemView: View?) : AbstractViewHolder<WaitingPaymentOrder>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders
    }

    private val adapter: WaitingPaymentOrderProductsAdapter by lazy {
        WaitingPaymentOrderProductsAdapter(WaitingPaymentOrderProductsAdapterTypeFactory())
    }

    private var products: List<WaitingPaymentOrder.Product> = emptyList()

    override fun bind(element: WaitingPaymentOrder?) {
        element?.let { element ->
            products = element.products
            with(itemView) {
                tvValuePaymentDeadline.text = element.paymentDeadline
                tvValueBuyerNameAndPlace.text = element.buyerNameAndPlace
                tvToggleCollapseMoreProducts.apply {
                    showWithCondition(element.products.size > 5)
                    updateToggleCollapseText(element.isExpanded)
                    setOnClickListener {
                        itemView.tvToggleCollapseMoreProducts.isClickable = false
                        if (!element.isExpanded) {
                            showMoreProducts()
                            element.isExpanded = true
                        } else {
                            collapseMoreProducts()
                            element.isExpanded = false
                        }
                    }
                }
                rvWaitingPaymentOrderProducts.apply {
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(getShownProducts(element))
                }
            }
        }
    }

    private fun updateToggleCollapseText(expanded: Boolean) {
        itemView.tvToggleCollapseMoreProducts.text = if (expanded) {
            "Lebih Sedikit"
        } else {
            "Tampilkan Lebih Banyak"
        }
    }

    private fun getShownProducts(element: WaitingPaymentOrder): List<WaitingPaymentOrder.Product> {
        return if (element.isExpanded) {
            products
        } else {
            products.take(5)
        }
    }

    private fun collapseMoreProducts() {
        val diff = adapter.dataSize - 5
        val delay = (500 / diff).toLong().coerceAtMost(33)
        for (i in 1..adapter.dataSize - 5) {
            Handler().postDelayed({
                adapter.updateProducts(products.take(products.size - i))
                if (i == adapter.dataSize - 5) {
                    itemView.tvToggleCollapseMoreProducts.isClickable = true
                }
            }, (delay * i))
        }
        updateToggleCollapseText(false)
    }

    private fun showMoreProducts() {
        val diff = products.size - 5
        val delay = (500 / diff).toLong().coerceAtMost(33)
        for (i in 6..products.size) {
            Handler().postDelayed({
                adapter.updateProducts(products.take(i))
                if (i == products.size) {
                    itemView.tvToggleCollapseMoreProducts.isClickable = true
                }
            }, (delay * (i - diff)))
        }
        updateToggleCollapseText(true)
    }
}