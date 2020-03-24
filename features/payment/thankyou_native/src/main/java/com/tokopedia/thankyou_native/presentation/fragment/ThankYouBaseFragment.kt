package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.PaymentStatusMapper
import com.tokopedia.thankyou_native.presentation.dialog.InvoiceDetailBottomSheet
import com.tokopedia.thankyou_native.presentation.dialog.PaymentMethodsBottomSheet
import com.tokopedia.thankyou_native.presentation.helper.DialogHelper
import com.tokopedia.thankyou_native.presentation.helper.DialogOrigin
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.thankyou_native.presentation.views.PDPThankYouPageView
import com.tokopedia.thankyou_native.presentation.views.Wishlist
import com.tokopedia.unifycomponents.Toaster
import java.util.*

abstract class ThankYouBaseFragment : BaseDaggerFragment(), OnDialogRedirectListener {

    private lateinit var paymentMethodsBottomSheet: PaymentMethodsBottomSheet
    private lateinit var dialogHelper: DialogHelper


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.thank_menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_info) {
            openInvoiceDetail()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    abstract fun openInvoiceDetail()
    abstract fun getRecommendationView() : PDPThankYouPageView?


    fun openDetailedInvoiceBottomsheet(visitables: ArrayList<Visitable<*>>?) {
        context?.let { context ->
            visitables?.let { visitables ->
                InvoiceDetailBottomSheet(context).show(visitables)
            }
        }
    }

    fun openHowTOPay(thanksPageData: ThanksPageData) {
        context?.let { context ->
            if (!::paymentMethodsBottomSheet.isInitialized)
                paymentMethodsBottomSheet = PaymentMethodsBottomSheet(context)
            paymentMethodsBottomSheet.show(thanksPageData.howToPay)
        }
    }

    fun showPaymentStatusDialog(dialogOrigin: DialogOrigin?, thanksPageData: ThanksPageData){
        context?.let {
            if (!::dialogHelper.isInitialized)
                dialogHelper = DialogHelper(it, this)
            dialogOrigin?.let { dialogOrigin ->
                dialogHelper.showPaymentStatusDialog(dialogOrigin,
                        PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus))
            }
        }
    }

    override fun gotoHomePage() {
        RouteManager.route(context, ApplinkConst.HOME, "")
        activity?.finish()
    }

    override fun gotoPaymentWaitingPage() {
        RouteManager.route(context, ApplinkConst.PMS, "")
        activity?.finish()
    }

    override fun gotoOrderList() {
        RouteManager.route(context, ApplinkConst.PURCHASE_ORDER_DETAIL, "")//arrayOf(thanksPageData.orderList[0].orderId))
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Wishlist.REQUEST_FROM_PDP -> {
                if (data != null) {
                    val wishListStatusFromPdp = data.getBooleanExtra(Wishlist.PDP_WIHSLIST_STATUS_IS_WISHLIST, false)
                    val position = data.getIntExtra(Wishlist.PDP_EXTRA_UPDATED_POSITION, -1)
                    getRecommendationView()?.onActivityResult(position, wishListStatusFromPdp)
                }
            }
        }
    }

    fun showErrorOnUI(errorMessage: String, retry: (() -> Unit)?) {
        view?.let { view ->
            retry?.let {
                Toaster.make(view, errorMessage,
                        Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                        getString(R.string.thank_coba_lagi), View.OnClickListener { retry.invoke() })
            }
        }
    }

    fun showToaster(message: String) {
        view?.let { Toaster.make(it, message, Toaster.LENGTH_SHORT) }
    }

}