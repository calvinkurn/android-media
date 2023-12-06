package com.tokopedia.thankyou_native.presentation.views.listener

import android.content.Context
import android.view.View
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.BankTransfer
import com.tokopedia.thankyou_native.data.mapper.PaymentType
import com.tokopedia.thankyou_native.data.mapper.Retail
import com.tokopedia.thankyou_native.data.mapper.VirtualAccount
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.ThanksPageHelper
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class WaitingHeaderListenerImpl @Inject constructor(
    private val context: Context?,
    private val rootView: View?,
    private val paymentType: PaymentType,
    private val thanksPageData: ThanksPageData,
    private val thankYouPageAnalytics: ThankYouPageAnalytics,
): WaitingHeaderListener {

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
            showToastCopySuccessFully()
        }
        thankYouPageAnalytics.sendSalinButtonClickEvent(
            thanksPageData.profileCode,
            thanksPageData.gatewayName,
            thanksPageData.paymentID
        )
    }

    override fun onSeeDetailInvoice() {

    }

    override fun onPrimaryButtonClick() {

    }

    override fun onSecondaryButtonClick() {

    }

    private fun showToastCopySuccessFully() {
        rootView?.let {
            val toasterMessage = when (paymentType) {
                is BankTransfer -> context?.getString(R.string.thankyou_bank_account_copied)
                is VirtualAccount -> if (thanksPageData.gatewayName == DeferredPaymentFragment.GATEWAY_KLIK_BCA)
                    context?.getString(R.string.thankyou_klikbca_virtual_account_copied)
                else
                    context?.getString(R.string.thankyou_virtual_account_copied)
                is Retail -> context?.getString(R.string.thankyou_retail_account_copied)
                else -> ""
            }
            if (toasterMessage?.isNotBlank() == true)
                Toaster.make(it, toasterMessage, Toaster.LENGTH_SHORT)
        }
    }
}
