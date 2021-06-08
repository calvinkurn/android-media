package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class OrderStatusHeaderViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
) : BaseToasterViewHolder<OrderStatusUiModel.OrderStatusHeaderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_status_info_header
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val buyerOrderDetailIndicator = itemView?.findViewById<View>(R.id.buyerOrderDetailIndicator)
    private val labelBuyerOrderDetailPreOrder = itemView?.findViewById<Label>(R.id.labelBuyerOrderDetailPreOrder)
    private val tvBuyerOrderDetailSeeDetail = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailSeeDetail)
    private val tvBuyerOrderDetailStatusOrder = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailStatusOrder)

    init {
        setupSeeOrderStatusDetail()
    }

    private var element: OrderStatusUiModel.OrderStatusHeaderUiModel? = null

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?) {
        element?.let {
            this.element = element
            setupIndicatorColor(it.indicatorColor)
            setupStatusHeader(it.orderStatus)
            setupPreOrderLabel(it.preOrder)
        }
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is OrderStatusUiModel.OrderStatusHeaderUiModel && newItem is OrderStatusUiModel.OrderStatusHeaderUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.element = element
                    if (oldItem.indicatorColor != newItem.indicatorColor) {
                        setupIndicatorColor(newItem.indicatorColor)
                    }
                    if (oldItem.orderStatus != newItem.orderStatus) {
                        setupStatusHeader(newItem.orderStatus)
                    }
                    if (oldItem.preOrder != newItem.preOrder) {
                        setupPreOrderLabel(newItem.preOrder)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupSeeOrderStatusDetail() {
        tvBuyerOrderDetailSeeDetail?.apply {
            setOnClickListener {
                val element = element
                if (element == null || element.orderId.isBlank()) {
                    showToaster(context.getString(R.string.error_message_please_reload_order_detail))
                } else {
                    navigator.goToTrackOrderPage(element.orderId)
                    BuyerOrderDetailTracker.eventClickSeeOrderHistoryDetail(element.orderStatusId, element.orderId)
                }
            }
        }
    }

    private fun setupIndicatorColor(indicatorColor: String) {
        buyerOrderDetailIndicator?.background = Utils.getColoredIndicator(itemView.context, indicatorColor)
    }

    private fun setupStatusHeader(orderStatus: String) {
        tvBuyerOrderDetailStatusOrder?.text = orderStatus
    }

    private fun setupPreOrderLabel(preOrder: OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel) {
        if (preOrder.isPreOrder && preOrder.value.isNotBlank()) {
            labelBuyerOrderDetailPreOrder?.setLabel(itemView.context.getString(R.string.label_pre_order_duration, preOrder.value))
        } else {
            labelBuyerOrderDetailPreOrder?.gone()
        }
    }
}