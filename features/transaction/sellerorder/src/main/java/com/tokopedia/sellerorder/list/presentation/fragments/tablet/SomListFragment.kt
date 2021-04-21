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
        } else {
            updateOrderDetail = false
            hideOrderDetail = false
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

    override fun createCoachMarkItems(firstNewOrderView: View): ArrayList<CoachMark2Item> {
        return arrayListOf<CoachMark2Item>().apply {
            add(CoachMark2Item(sortFilterSomList, getString(R.string.som_list_coachmark_sort_filter_title), getString(R.string.som_list_coachmark_sort_filter_description)))
            if (som_list_toolbar?.menu?.findItem(R.id.som_list_action_waiting_payment_order)?.isVisible == true) {
                activity?.findViewById<View>(R.id.som_list_action_waiting_payment_order)?.let {
                    add(CoachMark2Item(it, getString(R.string.som_list_coachmark_waiting_payment_title), getString(R.string.som_list_coachmark_waiting_payment_description)))
                }
            }
            if (GlobalConfig.isSellerApp()) {
                add(CoachMark2Item(tvSomListBulk, getString(R.string.som_list_coachmark_multi_select_title), getString(R.string.som_list_coachmark_multi_select_description)))
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

    override fun shouldShowFilterCoachMark() = scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && coachMarkIndexToShow == filterChipCoachMarkItemPosition &&
            sortFilterSomList?.isVisible == true

    override fun shouldShowWaitingPaymentCoachMark(waitingPaymentOrderListCountResult: Result<WaitingPaymentCounter>?) =
            scrollViewErrorState?.isVisible == false && coachMarkIndexToShow == waitingPaymentCoachMarkItemPosition &&
                    shouldShowCoachMark && waitingPaymentOrderListCountResult is Success

    override fun shouldShowBulkAcceptOrderCoachMark() = scrollViewErrorState?.isVisible == false &&
            shouldShowCoachMark && coachMarkIndexToShow == bulkProcessCoachMarkItemPosition &&
            tvSomListBulk?.isVisible == true

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
        dismissBottomSheets()
        super.onActionCompleted(true, orderId)
    }

    fun applySearchParam(invoice: String) {
        val typingAnimator = ValueAnimator.ofInt(0, invoice.length)
        typingAnimator.duration = SEARCH_ANIMATION_DURATION
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