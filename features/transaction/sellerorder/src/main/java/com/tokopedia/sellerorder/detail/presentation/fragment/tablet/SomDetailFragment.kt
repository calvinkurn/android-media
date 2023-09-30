package com.tokopedia.sellerorder.detail.presentation.fragment.tablet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToChangeCourierPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToConfirmShippingPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToRequestPickupPage
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.data.model.GetResolutionTicketStatusResponse
import com.tokopedia.sellerorder.detail.data.model.SetDelivered
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceResponse
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailComponent
import com.tokopedia.unifycomponents.Toaster

class SomDetailFragment : com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment() {

    private var orderDetailListener: SomOrderDetailListener? = null
    private var shouldRefreshOrderList: Boolean = false
    private var shouldPassInvoice: Boolean = false

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailFragment {
            return SomDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(SomConsts.PARAM_ORDER_ID, bundle.getString(SomConsts.PARAM_ORDER_ID))
                    putBoolean(SomConsts.PARAM_PASS_INVOICE, bundle.getBoolean(SomConsts.PARAM_PASS_INVOICE))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shouldPassInvoice = arguments?.getBoolean(SomConsts.PARAM_PASS_INVOICE, false) ?: false
        super.onCreate(savedInstanceState)
    }

    override fun initInjector() {
        activity?.let { activity ->
            DaggerSomDetailComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(activity.application))
                .build()
                .inject(this)
        }
    }

    override fun handleRequestPickUpResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP)) {
            val message = data.getStringExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP).orEmpty()
            showCommonToaster(message)
            shouldRefreshOrderList = true
            loadDetail()
        }
    }

    override fun handleChangeCourierAndConfirmShippingResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(SomConsts.RESULT_CONFIRM_SHIPPING)) {
            val resultConfirmShippingMsg = data.getStringExtra(SomConsts.RESULT_CONFIRM_SHIPPING).orEmpty()
            showCommonToaster(resultConfirmShippingMsg)
            shouldRefreshOrderList = true
            loadDetail()
        }
    }

    override fun handleReschedulePickupResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            shouldRefreshOrderList = true
            loadDetail()
        }
    }

    override fun handleReturnToShipperResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
            shouldRefreshOrderList = true
            loadDetail()
        }
    }

    override fun handleFindNewDriverResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            shouldRefreshOrderList = true
            loadDetail()
        }
    }

    override fun onGoToOrderDetailButtonClicked() {
        shouldRefreshOrderList = true
        super.onGoToOrderDetailButtonClicked()
    }

    override fun loadDetail() {
        showLoading()
        if (connectionMonitor?.isConnected == true) {
            activity?.let {
                SomAnalytics.sendScreenName(SomConsts.DETAIL_ORDER_SCREEN_NAME + orderId)
                it.resources?.let { r ->
                    somDetailViewModel.loadDetailOrder(orderId)
                    if (shouldRefreshOrderList) {
                        shouldRefreshOrderList = false
                        orderDetailListener?.onRefreshOrder(orderId)
                    }
                }
            }
        } else {
            showErrorState(GlobalError.NO_CONNECTION)
        }
    }

    override fun doOnUserNotAllowedToViewSOM() {
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(context, ApplinkConstInternalSellerapp.SELLER_HOME)
        } else {
            activity?.finish()
        }
    }

    override fun renderDetail(
        somDetail: SomDetailOrder.Data.GetSomDetail?,
        somDynamicPriceResponse: SomDynamicPriceResponse.GetSomDynamicPrice?,
        resolutionTicketStatusResponse: GetResolutionTicketStatusResponse
        .ResolutionGetTicketStatus.ResolutionData?
    ) {
        if (shouldPassInvoice) {
            shouldPassInvoice = false
            orderDetailListener?.onShouldPassInvoice(detailResponse?.invoice.orEmpty())
        }
        super.renderDetail(somDetail, somDynamicPriceResponse, resolutionTicketStatusResponse)
    }

    override fun onSuccessAcceptOrder() {
        shouldRefreshOrderList = true
        loadDetail()
    }

    override fun onSuccessRejectCancelOrder() {
        shouldRefreshOrderList = true
        super.onSuccessRejectCancelOrder()
    }

    override fun onSuccessEditAwb() {
        shouldRefreshOrderList = true
        super.onSuccessEditAwb()
    }

    override fun onRefresh(view: View?) {
        shouldRefreshOrderList = true
        super.onRefresh(view)
    }

    override fun onSuccessRejectOrder(rejectOrderData: SomRejectOrderResponse.Data.RejectOrder) {
        if (rejectOrderData.success == 1) {
            showToaster(rejectOrderData.message.firstOrNull() ?: getString(R.string.message_change_order_status_success), view, Toaster.TYPE_NORMAL, "")
        } else {
            showToaster(rejectOrderData.message.firstOrNull() ?: getString(R.string.global_error), view, Toaster.TYPE_ERROR)
        }
        shouldRefreshOrderList = true
        loadDetail()
    }

    override fun onSuccessSetDelivered(deliveredData: SetDelivered) {
        if (deliveredData.success == SomConsts.SOM_SET_DELIVERED_SUCCESS_CODE) {
            showToaster(getString(R.string.message_set_delivered_success), view, Toaster.TYPE_NORMAL, "")
            bottomSheetManager?.dismissBottomSheets()
            shouldRefreshOrderList = true
            loadDetail()
        } else {
            val message = deliveredData.message.joinToString().takeIf { it.isNotBlank() } ?: getString(R.string.global_error)
            showToaster(message, view, Toaster.TYPE_ERROR, "")
            bottomSheetManager?.getSomBottomSheetSetDelivered()?.onFailedSetDelivered()
        }
    }

    override fun createIntentConfirmShipping(isChangeShipping: Boolean) {
        parentFragment?.let {
            if (isChangeShipping) {
                goToChangeCourierPage(it, orderId)
            } else {
                goToConfirmShippingPage(it, orderId)
            }
        }
    }

    override fun requestPickUp() {
        parentFragment?.let {
            goToRequestPickupPage(it, orderId)
        }
    }

    override fun observeOrderExtensionRequestInfo() {
        orderExtensionViewModel.orderExtensionRequestInfo.observe(viewLifecycleOwner) { result ->
            if (result.message.isNotBlank() || result.throwable != null) {
                if (result.success) {
                    showCommonToaster(result.message)
                } else {
                    if (result.throwable == null) {
                        showErrorToaster(result.message)
                    } else {
                        result.throwable?.showErrorToaster()
                    }
                }
            }
            if (result.completed && result.refreshOnDismiss) {
                shouldRefreshOrderList
                loadDetail()
            }
            onRequestExtensionInfoChanged(result)
        }
    }

    override fun showBackButton(): Boolean = false

    override fun doOnResume() {
        // noop
    }

    fun setOrderIdToShow(orderId: String) {
        if (orderId != this.orderId) {
            bottomSheetManager?.dismissBottomSheets()
            this.orderId = orderId
            loadDetail()
        }
    }

    fun refreshOrder(orderId: String) {
        if (orderId == this.orderId) {
            bottomSheetManager?.dismissBottomSheets()
            loadDetail()
        }
    }

    fun setOrderDetailListener(listener: SomOrderDetailListener) {
        this.orderDetailListener = listener
    }

    fun closeOrderDetail() {
        bottomSheetManager?.dismissBottomSheets()
        orderId = ""
    }

    interface SomOrderDetailListener {
        fun onRefreshOrder(orderId: String)
        fun onShouldPassInvoice(invoice: String)
    }
}
