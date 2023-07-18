package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.adapter.OrderStatusLabelsAdapter
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
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

    private val labelsAdapter = OrderStatusLabelsAdapter()

    init {
        setupOrderStatusLabelsRecyclerView()
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?) {
        element?.let {
            setupIndicatorColor(it.indicatorColor)
            setupStatusHeader(it.orderStatus)
            setupPreOrderLabel(it.preOrder)
            setupOrderStatusLabels(it.labels)
            setupSeeOrderStatusDetail(it)
            setupSeeDetailVisibility(it.orderId)
        }
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusHeaderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OrderStatusUiModel.OrderStatusHeaderUiModel && newItem is OrderStatusUiModel.OrderStatusHeaderUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.indicatorColor != newItem.indicatorColor) {
                        setupIndicatorColor(newItem.indicatorColor)
                    }
                    if (oldItem.orderStatus != newItem.orderStatus) {
                        setupStatusHeader(newItem.orderStatus)
                    }
                    if (oldItem.preOrder != newItem.preOrder) {
                        setupPreOrderLabel(newItem.preOrder)
                    }
                    if (oldItem.labels != newItem.labels) {
                        setupOrderStatusLabels(newItem.labels)
                    }
                    setupSeeOrderStatusDetail(newItem)
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupOrderStatusLabelsRecyclerView() {
        val rv = itemView.findViewById<RecyclerView>(R.id.rv_buyer_order_detail_order_status_labels)
        rv.layoutManager = FlexboxLayoutManager(itemView.context).apply {
            alignItems = AlignItems.FLEX_START
        }
        rv.adapter = labelsAdapter
    }

    private fun setupSeeOrderStatusDetail(element: OrderStatusUiModel.OrderStatusHeaderUiModel) {
        if (isEnableOrderStatusDetail()) {
            tvBuyerOrderDetailSeeDetail?.apply {
                show()
                setOnClickListener {
                    if (element.orderId.isBlank()) {
                        showToaster(context.getString(R.string.error_message_please_reload_order_detail))
                    } else {
                        navigator.goToTrackOrderPage(element.orderId)
                        BuyerOrderDetailTracker.eventClickSeeOrderHistoryDetail(
                            orderStatusCode = element.orderStatusId,
                            orderId = element.orderId
                        )
                    }
                }
            }
        } else {
            tvBuyerOrderDetailSeeDetail?.hide()
        }
    }

    private fun isEnableOrderStatusDetail(): Boolean {
        val remoteConfigImpl = FirebaseRemoteConfigImpl(itemView.context)
        return remoteConfigImpl.getBoolean(RemoteConfigKey.IS_ENABLE_ORDER_STATUS_DETAIL_TEST, true)
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

    private fun setupOrderStatusLabels(labels: List<String>) {
        labelsAdapter.setLabels(labels)
    }

    private fun setupSeeDetailVisibility(orderId: String) {
        tvBuyerOrderDetailSeeDetail?.showWithCondition(orderId.isNotBlank() && orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID)
    }
}
