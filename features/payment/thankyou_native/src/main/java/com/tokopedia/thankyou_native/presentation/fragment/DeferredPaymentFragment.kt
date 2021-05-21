package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.ThanksPageHelper.copyTOClipBoard
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.ThankYouPageTimerView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.utils.htmltags.HtmlUtil
import kotlinx.android.synthetic.main.thank_fragment_deferred.*

class DeferredPaymentFragment : ThankYouBaseFragment(), ThankYouPageTimerView.ThankTimerViewListener {

    var paymentType: PaymentType? = null

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_deferred, container, false)
    }

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer
    override fun getFeatureListingContainer(): GyroView? = featureListingContainer

    override fun getTopTickerView(): Ticker? = topTicker

    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        paymentType = PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)
        paymentType?.let {
            when (it) {
                is BankTransfer -> {
                    inflateWaitingUI(getString(R.string.thank_account_number), isCopyVisible = true, highlightAmountDigits = true)
                    showDigitAnnouncementTicker()
                }
                is VirtualAccount -> inflateWaitingUI(
                        if (thanksPageData.gatewayName == GATEWAY_KLIK_BCA)
                            getString(R.string.thank_klikBCA_virtual_account_tag)
                        else
                            getString(R.string.thank_virtual_account_tag),
                        isCopyVisible = true, highlightAmountDigits = false)
                is Retail -> inflateWaitingUI(getString(R.string.thank_payment_code), isCopyVisible = true, highlightAmountDigits = false)
                is SmsPayment -> inflateWaitingUI(getString(R.string.thank_phone_number), isCopyVisible = false, highlightAmountDigits = false)
            }
        }
        if (thanksPageData.thanksCustomization == null || thanksPageData.thanksCustomization.customWtvText.isNullOrBlank()) {
            tvCheckPaymentStatusTitle.text = getString(R.string.thank_processing_payment_check_order)
        } else {
            tvCheckPaymentStatusTitle.text = thanksPageData.thanksCustomization.customWtvText
        }


        initCheckPaymentWidgetData()
    }

    override fun getLoadingView(): View? = loadingLayout

    private fun highlightLastThreeDigits(amountStr: String) {
        context?.let {
            tvTotalAmount.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifycomponents.R.color.Unify_N700_96))
            val spannable = SpannableString(getString(R.string.thankyou_rp_without_space, amountStr))
            if (amountStr.length > HIGHLIGHT_DIGIT_COUNT) {
                val startIndex = spannable.length - HIGHLIGHT_DIGIT_COUNT
                spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifycomponents.R.color.Unify_G500)),
                        startIndex, spannable.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvTotalAmount.text = spannable
        }
    }

    private fun inflateWaitingUI(numberTypeTitle: String?, isCopyVisible: Boolean, highlightAmountDigits: Boolean) {
        tvPaymentGatewayName.text = thanksPageData.gatewayName
        ivPaymentGatewayImage.loadImage(thanksPageData.gatewayImage)
        ivPaymentGatewayImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        numberTypeTitle?.let {
            tvAccountNumberTypeTag.text = numberTypeTitle
            tvAccountNumber.text = thanksPageData.additionalInfo.accountDest
        } ?: run {
            tvAccountNumberTypeTag.gone()
            tvAccountNumber.gone()
        }
        if (highlightAmountDigits)
            highlightLastThreeDigits(thanksPageData.amountStr)
        else
            tvTotalAmount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)

        icCopyAmount.tag = thanksPageData.amount.toString()
        icCopyAmount.setOnClickListener {
            val amountStr: String? = icCopyAmount.tag?.toString()
            copyTotalAmountToClipboard(amountStr)
        }
        if (isCopyVisible) {
            tvAccountNumberCopy.visible()
            tvAccountNumberCopy.tag = thanksPageData.additionalInfo.accountDest
            icCopyAccountNumber.tag = thanksPageData.additionalInfo.accountDest
            tvAccountNumberCopy.setOnClickListener {
                val accountNumberStr: String? = tvAccountNumberCopy.tag?.toString()
                copyAccountNumberToClipboard(accountNumberStr)
            }
            icCopyAccountNumber.setOnClickListener {
                val accountNumberStr: String? = icCopyAccountNumber.tag?.toString()
                copyAccountNumberToClipboard(accountNumberStr)
            }
        } else {
            tvAccountNumberCopy.gone()
        }
        if (thanksPageData.additionalInfo.bankName.isNotBlank()) {
            tvBankName.text = "${thanksPageData.additionalInfo.bankName} ${thanksPageData.additionalInfo.bankBranch}"
            tvBankName.visible()
        }
        tvSeeDetail.setOnClickListener { openInvoiceDetail(thanksPageData) }
        tvSeePaymentMethods.setOnClickListener { openHowToPay(thanksPageData) }
        tvDeadlineTime.text = thanksPageData.expireTimeStr
        tvDeadlineTimer.setExpireTimeUnix(thanksPageData.expireTimeUnix, this)
    }

    private fun initCheckPaymentWidgetData() {
        btnCheckPaymentStatus.setOnClickListener {
            refreshThanksPageData()
            thankYouPageAnalytics.get().onCheckPaymentStatusClick(thanksPageData.profileCode,
                    thanksPageData.paymentID.toString())
        }
        setUpHomeButton(btnShopAgain)
    }

    private fun showDigitAnnouncementTicker() {
        tickerAnnouncementExactDigits.visible()
        tickerAnnouncementExactDigits.tickerTitle = getString(R.string.thank_pending)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tickerAnnouncementExactDigits.setTextDescription(HtmlUtil
                    .fromHtml(getString(R.string.thank_exact_transfer_upto_3_digits)).trim())
        } else {
            tickerAnnouncementExactDigits
                    .setHtmlDescription(getString(R.string.thank_exact_transfer_upto_3_digits))
        }
        view_divider_3.gone()
    }

    private fun copyAccountNumberToClipboard(accountNumberStr: String?) {
        accountNumberStr?.let { str ->
            context?.let { context ->
                copyTOClipBoard(context, str)
                showToastCopySuccessFully(context)
            }
        }
        thankYouPageAnalytics.get()
                .sendSalinButtonClickEvent(thanksPageData.profileCode, thanksPageData.gatewayName,
                        thanksPageData.paymentID.toString())
    }

    private fun copyTotalAmountToClipboard(amountStr: String?) {
        amountStr?.let { str ->
            context?.let { context ->
                copyTOClipBoard(context, str)
                view?.let {
                    Toaster.build(it, getString(R.string.thank_you_amount_copy_success),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
                }
            }
        }
    }

    private fun showToastCopySuccessFully(context: Context) {
        view?.let {
            val toasterMessage = when (paymentType) {
                is BankTransfer -> getString(R.string.thankyou_bank_account_copied)
                is VirtualAccount -> if (thanksPageData.gatewayName == GATEWAY_KLIK_BCA)
                    getString(R.string.thankyou_klikbca_virtual_account_copied)
                else
                    getString(R.string.thankyou_virtual_account_copied)
                is Retail -> getString(R.string.thankyou_retail_account_copied)
                else -> ""
            }
            if (toasterMessage.isNotBlank())
                Toaster.make(it, toasterMessage, Toaster.LENGTH_SHORT)
        }
    }

    override fun onTimerFinished() {
        refreshThanksPageData()
    }

    override fun onStart() {
        super.onStart()
        tvDeadlineTimer.startTimer()
    }

    override fun onStop() {
        super.onStop()
        tvDeadlineTimer.stopTimer()
    }

    override fun onThankYouPageDataReLoaded(data: ThanksPageData) {
        loadingLayout.gone()
        thanksPageData = data
        showPaymentStatusDialog(isTimerExpired(data), thanksPageData)
    }

    private fun isTimerExpired(thanksPageData: ThanksPageData): Boolean {
        if (thanksPageData.expireTimeUnix * 1000L <= System.currentTimeMillis())
            return true
        return false
    }

    private fun isPaymentTimerExpired(): Boolean {
        if (thanksPageData.expireTimeUnix <= System.currentTimeMillis() / ONE_SECOND_TO_MILLIS)
            return true
        return false
    }

    internal fun onBackPressed(): Boolean {
        if (!isPaymentTimerExpired()) {
            refreshThanksPageData()
            return true
        }
        return false
    }

    companion object {
        const val HIGHLIGHT_DIGIT_COUNT = 3
        const val ONE_SECOND_TO_MILLIS = 1000L
        const val SCREEN_NAME = "Selesaikan Pembayaran"

        const val GATEWAY_KLIK_BCA = "KlikBCA"

        fun getFragmentInstance(bundle: Bundle, thanksPageData: ThanksPageData):
                DeferredPaymentFragment = DeferredPaymentFragment().apply {
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }
}
