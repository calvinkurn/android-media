package com.tokopedia.sellerorder.list.presentation.fragments.tablet

import android.os.Bundle
import android.text.Editable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.tablet.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.tablet.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.models.OptionalOrderData
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
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

    // to keep order detail view opened even if the order card is not showed on the som list view
    private var keepOpenOrderDetail: Boolean = false

    private var somListOrderListener: SomListClickListener? = null

    override fun getAdapterTypeFactory(): SomListAdapterTypeFactory = SomListAdapterTypeFactory(this, this)

    override fun afterTextChanged(s: Editable?) {
        keepOpenOrderDetail = true
        super.afterTextChanged(s)
    }

    override fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean, fromClickTab: Boolean) {
        if (fromClickTab) {
            keepOpenOrderDetail = false
        }
        super.onTabClicked(status, shouldScrollToTop, fromClickTab)
    }

    override fun renderOrderList(data: List<SomListOrderUiModel>) {
        if (openedOrderId.isNotEmpty()) {
            data.find { it.orderId == openedOrderId }.let { openedOrder ->
                openedOrder?.isOpen = true
            }
        }
        super.renderOrderList(data)
        // only refresh order detail if the response contains the same order that opened in order detail view
        if (data.find { it.orderId == openedOrderId } != null) {
            onOrderListChanged()
        }
    }

    override fun onRefreshOrderSuccess(result: OptionalOrderData) {
        if (result.orderId == openedOrderId) {
            result.order?.isOpen = true
        }
        keepOpenOrderDetail = false
        super.onRefreshOrderSuccess(result)
        onOrderListChanged()
    }

    override fun onOrderClicked(position: Int) {
        adapter.data.getOrNull(position)?.let {
            if (it !is SomListOrderUiModel) return
            keepOpenOrderDetail = false
            selectedOrderId = it.orderId
            openedOrderId = it.orderId
            somListOrderListener?.onOrderClicked(it.orderId)
            notifyOpenOrderDetail(it)
            SomAnalytics.eventClickOrderCard(it.orderStatusId, it.status)
        }
    }

    override fun showBackButton(): Boolean = false

    private fun notifyOpenOrderDetail(order: SomListOrderUiModel) {
        getOpenedOrder().let { openedOrder ->
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

    private fun getOpenedOrder(): Visitable<com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory>? {
        return adapter.data.firstOrNull {
            it is SomListOrderUiModel && it.isOpen
        }
    }

    private fun onOrderListChanged() {
        getOpenedOrder().let { openedOrder ->
            if (openedOrder == null) {
                if (keepOpenOrderDetail) {
                    return
                }
                openedOrderId = ""
                somListOrderListener?.closeOrderDetail()
            } else {
                openedOrderId = (openedOrder as SomListOrderUiModel).orderId
                somListOrderListener?.onRefreshSelectedOrder(openedOrderId)
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
        fun closeOrderDetail()
        fun onRefreshSelectedOrder(orderId: String)
    }
}