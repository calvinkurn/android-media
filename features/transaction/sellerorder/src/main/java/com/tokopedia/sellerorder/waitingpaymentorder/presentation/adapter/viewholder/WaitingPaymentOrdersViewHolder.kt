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
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.RecyclerViewItemChanges
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
        const val RECYCLER_VIEW_ANIMATION_DURATION = 300L
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
                    setLoadUnloadMoreClickListener(element.isExpanded, element.productUiModels)
                }
                icLoadMoreDropDown.apply {
                    iconDropDownAnimator?.end()
                    rotation = if (element.isExpanded) -180f else 0f
                    showWithCondition(element.productUiModels.size > MAX_ORDER_WHEN_COLLAPSED)
                }
                rvWaitingPaymentOrderProducts.apply {
                    if (adapter == null) {
                        isNestedScrollingEnabled = false
                        adapter = this@WaitingPaymentOrdersViewHolder.adapter
                    }
                    this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(getShownProducts(element.isExpanded, element.productUiModels))
                }
            }
        }
    }

    @Suppress("NAME_SHADOWING")
    override fun bind(element: WaitingPaymentOrderUiModel?, payloads: MutableList<Any>) {
        val changes = payloads.getOrNull(0)
        if (changes !is RecyclerViewItemChanges<*>) return
        val oldData = changes.oldData as WaitingPaymentOrderUiModel
        val newData = changes.newData as WaitingPaymentOrderUiModel

        if (oldData.isExpanded != newData.isExpanded) {
            itemView.rvWaitingPaymentOrderProducts.addOneTimeGlobalLayoutListener {
                (itemView as ViewGroup).layoutTransition.disableTransitionType(LayoutTransition.CHANGING)
            }
            setLoadUnloadMoreClickListener(newData.isExpanded, newData.productUiModels)
        }
    }

    private fun setLoadUnloadMoreClickListener(isExpanded: Boolean, productUiModels: List<WaitingPaymentOrderUiModel.ProductUiModel>) {
        itemView.setOnClickListener {
            (itemView as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            itemView.layoutTransition.setInterpolator(LayoutTransition.CHANGING, LinearInterpolator())
            this@WaitingPaymentOrdersViewHolder.adapter.updateProducts(getShownProducts(!isExpanded, productUiModels))
            listener.toggleCollapse(adapterPosition, !isExpanded)
            animateDropDownIcon(!isExpanded)
            updateToggleCollapseText(!isExpanded)
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
        fun toggleCollapse(position: Int, isExpanded: Boolean)
    }
}