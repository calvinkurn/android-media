package com.tokopedia.sellerorder.list.presentation.fragments.tablet

import android.os.Bundle
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.tablet.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.tablet.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.models.OptionalOrderData
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListFragment : com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomListFragment {
            return SomListFragment().apply {
                arguments = Bundle().apply {
                    putString(SomConsts.FILTER_STATUS_ID, bundle.getString(SomConsts.FILTER_STATUS_ID))
                    putBoolean(SomConsts.FROM_WIDGET_TAG, bundle.getBoolean(SomConsts.FROM_WIDGET_TAG))
                    putString(SomConsts.TAB_ACTIVE, bundle.getString(SomConsts.TAB_ACTIVE))
                    putString(SomConsts.TAB_STATUS, bundle.getString(SomConsts.TAB_STATUS))
                    putString(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH, bundle.getString(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH))
                    putInt(SomConsts.FILTER_ORDER_TYPE, bundle.getInt(SomConsts.FILTER_ORDER_TYPE))
                }
            }
        }
    }

    private var openedOrderId: String = ""

    private var somListOrderListener: SomListClickListener? = null

    override fun getAdapterTypeFactory(): SomListAdapterTypeFactory = SomListAdapterTypeFactory(this, this)

    override fun renderOrderList(data: List<SomListOrderUiModel>) {
        if (openedOrderId.isNotEmpty()) {
            data.find { it.orderId == openedOrderId }?.isOpen = true
        }
        super.renderOrderList(data)
    }

    override fun onRefreshOrderSuccess(result: OptionalOrderData) {
        if (result.orderId == openedOrderId) {
            result.order?.isOpen = true
        }
        super.onRefreshOrderSuccess(result)
    }

    override fun onOrderClicked(position: Int) {
        adapter.data.getOrNull(position)?.let {
            if (it !is SomListOrderUiModel) return
            selectedOrderId = it.orderId
            openedOrderId = it.orderId
            somListOrderListener?.onOrderClicked(it.orderId)
            notifyOpenOrderDetail(it)
            SomAnalytics.eventClickOrderCard(it.orderStatusId, it.status)
        }
    }

    override fun onActionCompleted(refreshOrder: Boolean, orderId: String) {
        super.onActionCompleted(refreshOrder, orderId)
        if (refreshOrder) {
            somListOrderListener?.onRefreshSelectedOrder(selectedOrderId)
        }
    }

    override fun onReceiveRefreshOrderRequest(orderId: String, invoice: String) {
        super.onReceiveRefreshOrderRequest(orderId, invoice)
        somListOrderListener?.onRefreshSelectedOrder(orderId)
    }

    override fun showBackButton(): Boolean = false

    private fun notifyOpenOrderDetail(order: SomListOrderUiModel) {
        adapter.data.firstOrNull {
            it is SomListOrderUiModel && it.isOpen
        }.let { openedOrder ->
            if (openedOrder is SomListOrderUiModel && openedOrder.orderId != order.orderId) {
                openedOrder.isOpen = false
                order.isOpen = true
                adapter.notifyItemChanged(adapter.data.indexOf(openedOrder), Bundle().apply {
                    putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, openedOrder.isOpen)
                })
                adapter.notifyItemChanged(adapter.data.indexOf(order), Bundle().apply {
                    putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, order.isOpen)
                })
            } else if (openedOrder == null) {
                order.isOpen = true
                adapter.notifyItemChanged(adapter.data.indexOf(order), Bundle().apply {
                    putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, order.isOpen)
                })
            }
        }
    }

    fun setSomListOrderListener(listener: SomListClickListener) {
        this.somListOrderListener = listener
    }

    fun refreshSelectedOrder(orderId: String) {
        super.onActionCompleted(true, orderId)
    }

    interface SomListClickListener {
        fun onOrderClicked(orderId: String)
        fun onRefreshSelectedOrder(orderId: String)
    }
}