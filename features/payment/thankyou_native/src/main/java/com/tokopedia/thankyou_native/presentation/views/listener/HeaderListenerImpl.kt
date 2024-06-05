package com.tokopedia.thankyou_native.presentation.views.listener

import android.content.Context
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.SPACE
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.BankTransfer
import com.tokopedia.thankyou_native.data.mapper.CtaTypeMapper
import com.tokopedia.thankyou_native.data.mapper.InstantPaymentPage
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentTypeMapper
import com.tokopedia.thankyou_native.data.mapper.Redirect
import com.tokopedia.thankyou_native.data.mapper.Retail
import com.tokopedia.thankyou_native.data.mapper.VirtualAccount
import com.tokopedia.thankyou_native.data.mapper.WaitingPaymentPage
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.ThanksPageHelper
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.unifycomponents.Toaster

class HeaderListenerImpl(
    private val context: Context?,
    private val rootView: View?,
    private val thanksPageData: ThanksPageData,
    private val thankYouPageAnalytics: ThankYouPageAnalytics,
    private val onDialogRedirectListener: OnDialogRedirectListener
): HeaderListener {

    override fun onCopyAccountId(accountNumberStr: String) {
        context?.let { context ->
            ThanksPageHelper.copyTOClipBoard(context, accountNumberStr)
            showToastCopySuccessFully()
        }
        thankYouPageAnalytics.sendSalinButtonClickEvent(
            thanksPageData.profileCode,
            thanksPageData.gatewayName,
            thanksPageData.paymentID
        )
    }

    override fun onCopyAmount(amountStr: String) {
        context?.let { context ->
            ThanksPageHelper.copyTOClipBoard(context, amountStr)
            showToastAmountSuccessfully()
        }
        thankYouPageAnalytics.sendSalinButtonClickEvent(
            thanksPageData.profileCode,
            thanksPageData.gatewayName,
            thanksPageData.paymentID
        )
    }

    override fun onSeeDetailInvoice() {
        onDialogRedirectListener.openInvoiceDetail()
    }

    override fun onPrimaryButtonClick() {
        if (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType, thanksPageData.paymentStatus) == WaitingPaymentPage) {
            onDialogRedirectListener.refreshThanksPageData()
            return
        }
        thanksPageData.customDataAppLink?.let {
            if (it.home.isNullOrBlank()) {
                onDialogRedirectListener.gotoHomePage()
            } else {
                onDialogRedirectListener.launchApplink(it.home)
            }
        } ?: run {
            onDialogRedirectListener.gotoHomePage()
        }
    }

    override fun onSecondaryButtonClick() {
        if (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType, thanksPageData.paymentStatus) == InstantPaymentPage) {
            onDialogRedirectListener.gotoOrderList(thanksPageData.customDataAppLink?.order.orEmpty())
        } else if (PaymentPageMapper.getPaymentPageType(thanksPageData.pageType) == WaitingPaymentPage) {
            onDialogRedirectListener.openHowToPay()
        } else {
            onDialogRedirectListener.refreshThanksPageData()
        }
    }

    override fun openApplink(applink: String?) {
        applink?.let {
            context?.apply {
                RouteManager.route(this, applink)
            }
        }
    }

    override fun onButtonClick(applink: String, type: String, isPrimary: Boolean, text: String) {
        thankYouPageAnalytics.sendCtaClickAnalytic(
            isPrimary,
            text,
            applink,
            thanksPageData.paymentID,
            thanksPageData.merchantCode,
            thanksPageData.gatewayName
        )
        if (CtaTypeMapper.getType(type) == Redirect) {
            openApplink(applink)
        } else {
            onDialogRedirectListener.refreshThanksPageData()
        }
    }

    private fun showToastCopySuccessFully() {
        rootView?.let {
            val toasterMsg = when (PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)) {
                VirtualAccount -> {
                    if (thanksPageData.gatewayName == DeferredPaymentFragment.GATEWAY_KLIK_BCA)
                        context?.getString(R.string.thank_klikBCA_virtual_account_tag)
                    else if (thanksPageData.gatewayName == DeferredPaymentFragment.JENIUS)
                        context?.getString(R.string.cashtag)
                    else
                        context?.getString(R.string.thank_virtual_account_tag)
                }
                Retail -> context?.getString(R.string.thankyou_retail_account_label)
                BankTransfer -> context?.getString(R.string.thank_account_number)
                else -> context?.getString(R.string.thank_virtual_account_tag)
            }

            val finalToasterMsg = toasterMsg + String.SPACE + context?.getString(R.string.copy_success)

            if (finalToasterMsg?.isNotBlank() == true)
                Toaster.make(it, finalToasterMsg, Toaster.LENGTH_SHORT)
        }
    }

    private fun showToastAmountSuccessfully() {
        rootView?.let {
            val finalToasterMsg = context?.getString(R.string.total_tagihan_successfully_copied)

            Toaster.make(it, finalToasterMsg.orEmpty(), Toaster.LENGTH_SHORT)
        }
    }
}
