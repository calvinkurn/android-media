package com.tokopedia.sellerorder.list.presentation.fragments.tablet

import android.os.Bundle
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts
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

    private var somListOrderListener: SomListClickListener? = null

    override fun onOrderClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        somListOrderListener?.onOrderClicked(order.orderId)
        SomAnalytics.eventClickOrderCard(order.orderStatusId, order.status)
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