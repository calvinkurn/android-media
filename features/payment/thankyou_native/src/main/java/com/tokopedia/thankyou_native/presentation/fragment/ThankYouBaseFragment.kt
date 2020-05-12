package com.tokopedia.thankyou_native.presentation.fragment

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.dialog.CloseableBottomSheetFragment
import com.tokopedia.thankyou_native.presentation.helper.DialogHelper
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import com.tokopedia.thankyou_native.recommendation.presentation.view.WishList
import com.tokopedia.unifycomponents.Toaster

abstract class ThankYouBaseFragment : BaseDaggerFragment(), OnDialogRedirectListener {

    private lateinit var invoiceBottomSheets: CloseableBottomSheetFragment
    private lateinit var dialogHelper: DialogHelper

    abstract fun getThankPageData(): ThanksPageData
    abstract fun getRecommendationView(): PDPThankYouPageView?
    abstract fun getThankPageAnalytics(): ThankYouPageAnalytics


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecommendationView()?.loadRecommendation(this)
    }

    fun openHowTOPay(thanksPageData: ThanksPageData) {
        getThankPageAnalytics().sendOnHowtoPayClickEvent()
        RouteManager.route(context, thanksPageData.howToPay)
    }

    fun showPaymentStatusDialog(isTimerFinished: Boolean,
                                thanksPageData: ThanksPageData) {
        var paymentStatus = PaymentStatusMapper
                .getPaymentStatusByInt(thanksPageData.paymentStatus)

        if (isTimerFinished && !isPaymentVerified(paymentStatus))
            paymentStatus = PaymentExpired

        context?.let {
            if (!::dialogHelper.isInitialized)
                dialogHelper = DialogHelper(it, this)
            dialogHelper.showPaymentStatusDialog(paymentStatus)
        }
    }

    private fun isPaymentVerified(paymentStatus: PaymentStatus?): Boolean {
        return when (paymentStatus) {
            is PaymentVerified -> true
            else -> false
        }
    }

    fun openInvoiceDetail(thanksPageData: ThanksPageData) {
        getThankPageAnalytics().sendLihatDetailClickEvent(PaymentPageMapper
                .getPaymentPageType(thanksPageData.pageType))
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
        getThankPageAnalytics().sendBelanjaLagiClickEvent()
        RouteManager.route(context, ApplinkConst.HOME, "")
        activity?.finish()
    }

    override fun gotoPaymentWaitingPage() {
        val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
        val paymentListIntent = RouteManager.getIntent(context, ApplinkConst.PMS, "")
        paymentListIntent?.let {
            TaskStackBuilder.create(context)
                    .addNextIntent(homeIntent)
                    .addNextIntent(paymentListIntent)
                    .startActivities()
        }
        activity?.finish()
    }

    override fun gotoOrderList() {
        try {
            getThankPageAnalytics().sendCheckTransactionListEvent()
            val list: List<String> = getThankPageData().shopOrder.map {
                it.orderId
            }

            val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
            val orderListListIntent = RouteManager.getIntent(context, ApplinkConst.Transaction.ORDER_MARKETPLACE_DETAIL, list[0])
            orderListListIntent?.let {
                TaskStackBuilder.create(context)
                        .addNextIntent(homeIntent)
                        .addNextIntent(orderListListIntent)
                        .startActivities()
            }
            activity?.finish()
        } catch (e: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            WishList.REQUEST_FROM_PDP -> {
                if (data != null) {
                    val wishListStatusFromPdp = data
                            .getBooleanExtra(WishList.PDP_WIHSLIST_STATUS_IS_WISHLIST, false)
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