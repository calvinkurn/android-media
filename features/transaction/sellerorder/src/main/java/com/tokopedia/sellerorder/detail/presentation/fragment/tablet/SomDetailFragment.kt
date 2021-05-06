package com.tokopedia.sellerorder.detail.presentation.fragment.tablet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.navigator.SomNavigator
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToChangeCourierPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToConfirmShippingPage
import com.tokopedia.sellerorder.common.navigator.SomNavigator.goToRequestPickupPage
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailComponent
import kotlinx.android.synthetic.main.fragment_som_detail.*

class SomDetailFragment : com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment(), Toolbar.OnMenuItemClickListener {

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
        activity?.application?.let {
            DaggerSomDetailComponent.builder()
                    .somComponent(SomComponentInstance.getSomComponent(it))
                    .build()
                    .inject(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        btn_primary?.isLoading = false
        if (requestCode == SomNavigator.REQUEST_CONFIRM_REQUEST_PICKUP && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(SomConsts.RESULT_PROCESS_REQ_PICKUP)) {
                    shouldRefreshOrderList = true
                    loadDetail()
                }
            }
        } else if (requestCode == SomNavigator.REQUEST_CONFIRM_SHIPPING && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(SomConsts.RESULT_CONFIRM_SHIPPING)) {
                    shouldRefreshOrderList = true
                    loadDetail()
                }
            }
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

    override fun renderDetail() {
        if (shouldPassInvoice) {
            shouldPassInvoice = false
            orderDetailListener?.onShouldPassInvoice(detailResponse?.invoice.orEmpty())
        }
        super.renderDetail()
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

    override fun showBackButton(): Boolean = false

    fun setOrderIdToShow(orderId: String) {
        if (orderId != this.orderId) {
            dismissBottomSheets()
            this.orderId = orderId
            loadDetail()
        }
    }

    fun refreshOrder(orderId: String) {
        if (orderId == this.orderId) {
            dismissBottomSheets()
            loadDetail()
        }
    }

    fun setOrderDetailListener(listener: SomOrderDetailListener) {
        this.orderDetailListener = listener
    }

    fun closeOrderDetail() {
        dismissBottomSheets()
        orderId = ""
    }

    interface SomOrderDetailListener {
        fun onRefreshOrder(orderId: String)
        fun onShouldPassInvoice(invoice: String)
    }
}