package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.PaymentStatusMapper
import com.tokopedia.thankyou_native.presentation.dialog.CloseableBottomSheetFragment
import com.tokopedia.thankyou_native.presentation.helper.DialogHelper
import com.tokopedia.thankyou_native.presentation.helper.DialogOrigin
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import com.tokopedia.thankyou_native.recommendation.presentation.view.WishList
import com.tokopedia.unifycomponents.Toaster
import retrofit2.http.Url
import java.net.URL

abstract class ThankYouBaseFragment : BaseDaggerFragment(), OnDialogRedirectListener {

    private lateinit var invoiceBottomSheets: CloseableBottomSheetFragment
    private lateinit var howToPayBottomSheets: CloseableBottomSheetFragment
    private lateinit var dialogHelper: DialogHelper

    abstract fun getThankPageData(): ThanksPageData
    abstract fun getRecommendationView(): PDPThankYouPageView?

    fun openHowTOPay(thanksPageData: ThanksPageData) {
        RouteManager.route(context, thanksPageData.howToPay)
    }

    fun showPaymentStatusDialog(dialogOrigin: DialogOrigin?, thanksPageData: ThanksPageData) {
        context?.let {
            if (!::dialogHelper.isInitialized)
                dialogHelper = DialogHelper(it, this)
            dialogOrigin?.let { dialogOrigin ->
                dialogHelper.showPaymentStatusDialog(dialogOrigin,
                        PaymentStatusMapper.getPaymentStatusByInt(thanksPageData.paymentStatus))
            }
        }
    }

    fun openInvoiceDetail(thanksPageData: ThanksPageData) {
        if (!::invoiceBottomSheets.isInitialized)
            invoiceBottomSheets = CloseableBottomSheetFragment
                    .newInstance(InvoiceFragment.getInvoiceFragment(thanksPageData),
                            true,
                            getString(R.string.thank_payment_detail),
                            null,
                            CloseableBottomSheetFragment.STATE_FULL)
        activity?.let {
            invoiceBottomSheets.showNow(it.supportFragmentManager, "")
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
        try {
            val list: List<String> = getThankPageData().shopOrder.map {
                it.orderId
            }
            RouteManager.route(context, ApplinkConst.Transaction.ORDER_MARKETPLACE_DETAIL, list[0])
            activity?.finish()
        }catch (e : Exception){}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            WishList.REQUEST_FROM_PDP -> {
                if (data != null) {
                    val wishListStatusFromPdp = data.getBooleanExtra(WishList.PDP_WIHSLIST_STATUS_IS_WISHLIST, false)
                    val position = data.getIntExtra(WishList.PDP_EXTRA_UPDATED_POSITION, -1)
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