package com.tokopedia.sellerorder.list.presentation.fragments.tablet

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.tablet.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.tablet.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.models.OptionalOrderData
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

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
        private const val COACHMARK_NO_POSITION = -1
        private const val COACHMARK_INDEX_ITEM_FILTER = 0
        private const val COACHMARK_INDEX_ITEM_WAITING_PAYMENT = 1
        private const val COACHMARK_INDEX_ITEM_BULK_ACCEPT = 2
        private const val COACHMARK_ITEM_COUNT_SELLERAPP = 3
        private const val COACHMARK_ITEM_COUNT_MAINAPP = 2
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

    override val coachMarkItemCount: Int
        get() = if (GlobalConfig.isSellerApp()) COACHMARK_ITEM_COUNT_SELLERAPP else COACHMARK_ITEM_COUNT_MAINAPP
    override val newOrderCoachMarkItemPosition: Int
        get() = COACHMARK_NO_POSITION
    override val filterChipCoachMarkItemPosition: Int
        get() = COACHMARK_INDEX_ITEM_FILTER
    override val waitingPaymentCoachMarkItemPosition: Int
        get() = COACHMARK_INDEX_ITEM_WAITING_PAYMENT
    override val bulkProcessCoachMarkItemPosition: Int
        get() = COACHMARK_INDEX_ITEM_BULK_ACCEPT

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

    override fun onTabClicked(status: SomListFilterUiModel.OrderType, shouldScrollToTop: Boolean, fromClickTab: Boolean) {
        if (fromClickTab) {
            updateOrderDetail = true
            hideOrderDetail = true
        }
        super.onTabClicked(status, shouldScrollToTop, fromClickTab)
    }

    override fun onClickOrderStatusFilterTab(
        status: SomListFilterUiModel.Status,
        shouldScrollToTop: Boolean,
        fromClickTab: Boolean
    ) {
        if (fromClickTab) {
            updateOrderDetail = true
            hideOrderDetail = true
        }
        super.onClickOrderStatusFilterTab(status, shouldScrollToTop, fromClickTab)
    }

    override fun renderOrderList(data: List<SomListOrderUiModel>) {
        if (openedOrderId.isNotEmpty()) {
            data.find { it.orderId == openedOrderId }.let { openedOrder ->
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

    override fun onSuccessGetFilter(result: Success<SomListFilterUiModel>, realtimeDataChangeCount: Int): Int {
        val result = super.onSuccessGetFilter(result, realtimeDataChangeCount)
        context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK)) {
                setCoachMarkStepListener()
                CoachMarkPreference.setShown(context, SHARED_PREF_NEW_SOM_LIST_COACH_MARK, true)
                shouldShowCoachMark = true
                reshowStatusFilterCoachMark()
            }
            return@let
        }
        return result
    }

    override fun createCoachMarkItems(firstNewOrderView: View?): ArrayList<CoachMark2Item> {
        return arrayListOf<CoachMark2Item>().apply {
            somListBinding?.sortFilterSomList?.let {
                add(CoachMark2Item(it, getString(R.string.som_list_coachmark_sort_filter_title), getString(R.string.som_list_coachmark_sort_filter_description)))
            }
            if (somListHeaderBinding?.icSomListMenuWaitingPayment?.isVisible == true) {
                somListHeaderBinding?.icSomListMenuWaitingPayment?.let {
                    add(CoachMark2Item(it, getString(R.string.som_list_coachmark_waiting_payment_title), getString(R.string.som_list_coachmark_waiting_payment_description)))
                }
            }
            if (GlobalConfig.isSellerApp()) {
                somListBinding?.tvSomListBulk?.let {
                    add(CoachMark2Item(it, getString(R.string.som_list_coachmark_multi_select_title), getString(R.string.som_list_coachmark_multi_select_description)))
                }
            }
        }
    }

    override fun tryReshowCoachMark() {
        view?.postDelayed({
            reshowStatusFilterCoachMark()
            reshowWaitingPaymentOrderListCoachMark()
            reshowBulkAcceptOrderCoachMark()
        }, DELAY_COACHMARK)
    }

    override fun shouldShowFilterCoachMark() = somListBinding?.scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && coachMarkIndexToShow == filterChipCoachMarkItemPosition &&
            somListBinding?.sortFilterSomList?.isVisible == true

    override fun shouldShowWaitingPaymentCoachMark(waitingPaymentOrderListCountResult: Result<WaitingPaymentCounter>?) =
        somListBinding?.scrollViewErrorState?.isVisible == false && coachMarkIndexToShow == waitingPaymentCoachMarkItemPosition &&
                    shouldShowCoachMark && waitingPaymentOrderListCountResult is Success

    override fun shouldShowBulkAcceptOrderCoachMark() = somListBinding?.scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && coachMarkIndexToShow == bulkProcessCoachMarkItemPosition &&
            somListBinding?.tvSomListBulk?.isVisible == true && tabActive == SomConsts.STATUS_NEW_ORDER

    private fun notifyOpenOrderDetail(order: SomListOrderUiModel) {
        getOpenedOrder().let { openedOrder ->
            if (openedOrder is SomListOrderUiModel && openedOrder.orderId != order.orderId) {
                adapter.notifyItemChanged(adapter.data.indexOf(openedOrder), Bundle().apply {
                    openedOrder.isOpen = false
                    putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, openedOrder.isOpen)
                })
                adapter.notifyItemChanged(adapter.data.indexOf(order), Bundle().apply {
                    order.isOpen = true
                    putBoolean(SomListOrderViewHolder.TOGGLE_OPEN, order.isOpen)
                })
            } else if (openedOrder == null) {
                adapter.notifyItemChanged(adapter.data.indexOf(order), Bundle().apply {
                    order.isOpen = true
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