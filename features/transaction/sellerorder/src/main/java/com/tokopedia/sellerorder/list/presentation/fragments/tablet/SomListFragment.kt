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
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderWrapperUiModel

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
                    putString(SomConsts.FILTER_ORDER_TYPE, bundle.getString(SomConsts.FILTER_ORDER_TYPE))
                }
            }
        }

        private const val SEARCH_ANIMATION_DURATION = 500L
    }

    private var openedOrderId: String = ""

    /*
        when isOpeningOrderDetailAppLink is true means that we're handling order detail applink, in this case
        we need postpone load order list because we need to get invoice from order detail then use that invoice
        to filter order that we need in order list fragment (to make order list fragment only show 1 order which
        showed in order detail), and after we're receiving the response in order list, we must not refresh
        order detail page
     */
    private var isOpeningOrderDetailAppLink: Boolean = false

    /*
        to mark whether we need to update order detail page after refreshing new order data in order list page
        we need to set this value whenever we're trying to get new order data in order list
     */
    private var updateOrderDetail: Boolean = false

    /*
        to mark whether we need to remove order detail page after refreshing new order data in order list page
        if updateOrderDetail is true, ignore the value of hideOrderDetail
        we need to set this value whenever we're trying to get new order data in order list
     */
    private var hideOrderDetail: Boolean = false
    private var somListOrderListener: SomListClickListener? = null

    override fun getAdapterTypeFactory(): SomListAdapterTypeFactory = SomListAdapterTypeFactory(this, this, this)

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
            resetMultiSelectState()
            isLoadingInitialData = true
            somListLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
            toggleBulkAction(false)
            loadTopAdsCategory()
            loadTickers()
            loadWaitingPaymentOrderCounter()
            loadFilters(loadOrders = false)
        }
    }

    override fun onTabClicked(
        quickFilter: SomListFilterUiModel.QuickFilter,
        shouldScrollToTop: Boolean
    ) {
        updateOrderDetail = true
        hideOrderDetail = true
        super.onTabClicked(quickFilter, shouldScrollToTop)
    }

    override fun onClickOrderStatusFilterTab(
        status: SomListFilterUiModel.Status,
        shouldScrollToTop: Boolean
    ) {
        updateOrderDetail = true
        hideOrderDetail = true
        super.onClickOrderStatusFilterTab(status, shouldScrollToTop)
    }

    override fun renderOrderList(data: SomListOrderWrapperUiModel) {
        if (openedOrderId.isNotEmpty()) {
            data.somListOrders.find { it.orderId == openedOrderId }.let { openedOrder ->
                if (openedOrder == null) {
                    updateOrderDetail = false
                } else {
                    openedOrder.isOpen = true
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
        } else {
            updateOrderDetail = false
            hideOrderDetail = false
        }
        super.onRefreshOrderSuccess(result)
        onOrderListChanged()
    }

    override fun onOrderClicked(order: SomListOrderUiModel) {
        selectedOrderId = order.orderId
        openedOrderId = order.orderId
        somListOrderListener?.onOrderClicked(order.orderId)
        notifyOpenOrderDetail(order)
        SomAnalytics.eventClickOrderCard(order.orderStatusId, order.status)
    }

    private fun notifyOpenOrderDetail(order: SomListOrderUiModel) {
        getOpenedOrder().let { openedOrder ->
            if (openedOrder is SomListOrderUiModel && openedOrder.orderId != order.orderId) {
                adapter.notifyItemChanged(
                    adapter.data.indexOf(openedOrder),
                    Bundle().apply {
                        openedOrder.isOpen = false
                        putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, openedOrder.isOpen)
                    }
                )
                adapter.notifyItemChanged(
                    adapter.data.indexOf(order),
                    Bundle().apply {
                        order.isOpen = true
                        putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, order.isOpen)
                    }
                )
            } else if (openedOrder == null) {
                adapter.notifyItemChanged(
                    adapter.data.indexOf(order),
                    Bundle().apply {
                        order.isOpen = true
                        putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, order.isOpen)
                    }
                )
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
        dismissBottomSheets()
        super.onActionCompleted(true, orderId)
    }

    fun applySearchParam(invoice: String) {
        val typingAnimator = ValueAnimator.ofInt(0, invoice.length)
        typingAnimator.duration = SEARCH_ANIMATION_DURATION
        typingAnimator.addUpdateListener { animation ->
            somListHeaderBinding?.searchBarSomList?.searchBarTextField?.setText(invoice.substring(0, animation.animatedValue as Int))
        }
        typingAnimator.start()
    }

    interface SomListClickListener {
        fun onOrderClicked(orderId: String)
        fun closeOrderDetail()
        fun onRefreshSelectedOrder(orderId: String)
    }
}
