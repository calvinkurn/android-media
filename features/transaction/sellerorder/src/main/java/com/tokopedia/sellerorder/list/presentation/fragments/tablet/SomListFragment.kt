package com.tokopedia.sellerorder.list.presentation.fragments.tablet

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.tablet.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.tablet.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.models.OptionalOrderData
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import kotlinx.android.synthetic.main.fragment_som_list.*

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
                    putString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID, bundle.getString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID))
                    putInt(SomConsts.FILTER_ORDER_TYPE, bundle.getInt(SomConsts.FILTER_ORDER_TYPE))
                }
            }
        }
    }

    private var openedOrderId: String = ""

    // to keep order detail view opened even if the order card is not showed on the som list view
    private var isOpeningOrderDetailAppLink: Boolean = false
    private var updateOrderDetail: Boolean = false
    private var hideOrderDetail: Boolean = false

    private var somListOrderListener: SomListClickListener? = null

    override fun getAdapterTypeFactory(): SomListAdapterTypeFactory = SomListAdapterTypeFactory(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        isOpeningOrderDetailAppLink = !arguments?.getString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID).isNullOrEmpty()
        super.onCreate(savedInstanceState)
    }

    override fun afterTextChanged(s: Editable?) {
        updateOrderDetail = true
        hideOrderDetail = false
        super.afterTextChanged(s)
    }

    override fun onSwipeRefresh() {
        updateOrderDetail = true
        hideOrderDetail = true
        super.onSwipeRefresh()
    }

    override fun onReceiveRefreshOrderRequest(orderId: String, invoice: String) {
        updateOrderDetail = true
        hideOrderDetail = true
        super.onReceiveRefreshOrderRequest(orderId, invoice)
    }

    override fun onActionCompleted(refreshOrder: Boolean, orderId: String) {
        if (refreshOrder) {
            updateOrderDetail = true
            hideOrderDetail = true
        }
        super.onActionCompleted(refreshOrder, orderId)
    }

    override fun loadData(page: Int) {
        if (page > 0) {
            updateOrderDetail = true
            hideOrderDetail = false
        }
        super.loadData(page)
    }

    override fun loadAllInitialData() {
        if (!isOpeningOrderDetailAppLink) {
            super.loadAllInitialData()
        } else {
            openedOrderId = arguments?.getString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID).orEmpty()
            viewModel.isMultiSelectEnabled = false
            resetOrderSelectedStatus()
            isLoadingInitialData = true
            somListLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
            loadTopAdsCategory()
            loadTickers()
            loadWaitingPaymentOrderCounter()
            loadFilters(loadOrders = false)
        }
    }

    override fun onTabClicked(status: SomListFilterUiModel.Status, shouldScrollToTop: Boolean, fromClickTab: Boolean) {
        if (fromClickTab) {
            updateOrderDetail = true
            hideOrderDetail = true
        }
        super.onTabClicked(status, shouldScrollToTop, fromClickTab)
    }

    override fun renderOrderList(data: List<SomListOrderUiModel>) {
        if (openedOrderId.isNotEmpty()) {
            data.find { it.orderId == openedOrderId }.let { openedOrder ->
                if (openedOrder != null) {
                    openedOrder.isOpen = true
                } else {
                    updateOrderDetail = false
                }
            }
        }
        super.renderOrderList(data)
        onOrderListChanged()
        isOpeningOrderDetailAppLink = false
    }

    override fun onRefreshOrderSuccess(result: OptionalOrderData) {
        if (result.orderId == openedOrderId) {
            result.order?.isOpen = true
            updateOrderDetail = result.order != null
            adapter.data.find { it is SomListOrderUiModel && it.orderId == openedOrderId }?.let {
                result.order?.isChecked = (it as SomListOrderUiModel).isChecked
            }
        }
        super.onRefreshOrderSuccess(result)
        onOrderListChanged()
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
        if (updateOrderDetail && !isOpeningOrderDetailAppLink) {
            somListOrderListener?.onRefreshSelectedOrder(openedOrderId)
        } else if (hideOrderDetail) {
            openedOrderId = ""
            somListOrderListener?.closeOrderDetail()
        }
    }

    fun setSomListOrderListener(listener: SomListClickListener) {
        this.somListOrderListener = listener
    }

    fun refreshSelectedOrder(orderId: String) {
        updateOrderDetail = true
        hideOrderDetail = true
        super.onActionCompleted(true, orderId)
    }

    fun applySearchParam(invoice: String) {
        val typingAnimator = ValueAnimator.ofInt(0, invoice.length)
        typingAnimator.duration = 500
        typingAnimator.addUpdateListener { animation ->
            searchBarSomList?.searchBarTextField?.setText(invoice.substring(0, animation.animatedValue as Int))
        }
        typingAnimator.start()
    }

    interface SomListClickListener {
        fun onOrderClicked(orderId: String)
        fun closeOrderDetail()
        fun onRefreshSelectedOrder(orderId: String)
    }
}