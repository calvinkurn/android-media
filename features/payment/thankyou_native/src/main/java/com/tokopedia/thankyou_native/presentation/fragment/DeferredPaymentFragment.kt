package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.ThanksPageHelper.copyTOClipBoard
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.htmltags.HtmlUtil
import java.util.Calendar
import java.util.Date
import kotlinx.android.synthetic.main.thank_fragment_deferred.*
import kotlinx.android.synthetic.main.thank_fragment_deferred.btnShopAgain
import kotlinx.android.synthetic.main.thank_fragment_deferred.carouselBanner
import kotlinx.android.synthetic.main.thank_fragment_deferred.featureListingContainer
import kotlinx.android.synthetic.main.thank_fragment_deferred.loadingLayout
import kotlinx.android.synthetic.main.thank_fragment_deferred.recommendationContainer
import kotlinx.android.synthetic.main.thank_fragment_deferred.rvBottomContent
import kotlinx.android.synthetic.main.thank_fragment_deferred.topAdsView
import kotlinx.android.synthetic.main.thank_fragment_deferred.topTicker
import kotlinx.android.synthetic.main.thank_fragment_deferred.tvBannerTitle
import kotlinx.android.synthetic.main.thank_fragment_deferred.tvTotalAmount
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*

class DeferredPaymentFragment : ThankYouBaseFragment() {

    var paymentType: PaymentType? = null

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.thank_fragment_deferred, container, false)
    }

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer
    override fun getFeatureListingContainer(): GyroView? = featureListingContainer
    override fun getTopAdsView(): TopAdsView? = topAdsView
    override fun getBottomContentRecyclerView(): RecyclerView? = rvBottomContent
    override fun getBannerTitle(): Typography? = tvBannerTitle
    override fun getBannerCarousel(): CarouselUnify? = carouselBanner

    override fun getTopTickerView(): Ticker? = topTicker

    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        paymentType = PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)
        paymentType?.let { paymentType ->
            when (paymentType) {
                is BankTransfer -> {
                    inflateWaitingUI(
                        getString(R.string.thank_account_number), isCopyVisible = true,
                        highlightAmountDigits = true,
                        paymentType = paymentType
                    )
                    showAnnouncementTicker(
                        getString(R.string.thank_pending),
                        getString(R.string.thank_exact_transfer_upto_3_digits)
                    )
                }
                is VirtualAccount -> {
                    inflateWaitingUI(
                        if (thanksPageData.gatewayName == GATEWAY_KLIK_BCA)
                            getString(R.string.thank_klikBCA_virtual_account_tag)
                        else
                            getString(R.string.thank_virtual_account_tag),
                        isCopyVisible = true, highlightAmountDigits = false,
                        paymentType = paymentType
                    )
                    showAnnouncementTicker(
                        String.EMPTY,
                        getString(R.string.thanks_va_ticker_description)
                    )
                }
                is Retail -> inflateWaitingUI(
                    getString(R.string.thank_payment_code), isCopyVisible = true,
                    highlightAmountDigits = false,
                    paymentType = paymentType
                )
                is SmsPayment -> inflateWaitingUI(
                    getString(R.string.thank_phone_number), isCopyVisible = false,
                    highlightAmountDigits = false,
                    paymentType = paymentType
                )
                else -> {
                    //no-op
                }
            }
        }
        if (thanksPageData.customDataMessage == null || thanksPageData.customDataMessage.wtvText.isNullOrBlank()) {
            tvCheckPaymentStatusTitle.text =
                getString(R.string.thank_processing_payment_check_order)
        } else {
            tvCheckPaymentStatusTitle.text = thanksPageData.customDataMessage.wtvText
        }


        initCheckPaymentWidgetData()
    }

    override fun getLoadingView(): View? = loadingLayout

    private fun highlightLastThreeDigits(amountStr: String) {
        context?.let {
            tvTotalAmount.setTextColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                )
            )
            val spannable =
                SpannableString(getString(R.string.thankyou_rp_without_space, amountStr))
            if (amountStr.length > HIGHLIGHT_DIGIT_COUNT) {
                val startIndex = spannable.length - HIGHLIGHT_DIGIT_COUNT
                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    ),
                    startIndex, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tvTotalAmount.text = spannable
        }
    }

    private fun inflateWaitingUI(
        numberTypeTitle: String?, isCopyVisible: Boolean,
        highlightAmountDigits: Boolean, paymentType: PaymentType
    ) {
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
            tvBankName.text =
                "${thanksPageData.additionalInfo.bankBranch}"
            tvBankName.visible()
        }
        tvSeeDetail.setOnClickListener { openInvoiceDetail(thanksPageData) }
        tvSeePaymentMethods.setOnClickListener { openHowToPay(thanksPageData) }
        tvDeadlineTime.text = thanksPageData.expireTimeStr
        setDeadlineTimer(thanksPageData.expireTimeUnix)
        if (paymentType == VirtualAccount
            && (thanksPageData.combinedAmount > thanksPageData.amount)
        ) {
            setCombinedAmount(thanksPageData)
        } else {
            tvTotalAmountLabel.text = getString(R.string.thank_total_amount_label)
            if (highlightAmountDigits)
                highlightLastThreeDigits(thanksPageData.amountStr)
            else
                tvTotalAmount.text = getString(
                    R.string.thankyou_rp_without_space,
                    thanksPageData.amountStr
                )
        }
        setClickToCopyAmount(paymentType, thanksPageData)
    }

    private fun setClickToCopyAmount(paymentType: PaymentType, thanksPageData: ThanksPageData){
        icCopyAmount.setOnClickListener {
            val amountStr = if (paymentType == VirtualAccount
                && (thanksPageData.combinedAmount > thanksPageData.amount)) {
                thanksPageData.combinedAmount.toString()
            } else {
                thanksPageData.amount.toString()
            }
            copyTotalAmountToClipboard(amountStr)
        }
    }


    private fun setCombinedAmount(thanksPageData: ThanksPageData) {
        tvTotalAmountLabel.text = getString(R.string.thanks_total_combined_amount)
        val amountStr = CurrencyFormatUtil
            .convertPriceValueToIdrFormat(thanksPageData.combinedAmount, false)
        tvTotalAmount.text = amountStr
    }

    private fun initCheckPaymentWidgetData() {
        btnCheckPaymentStatus.setOnClickListener {
            refreshThanksPageData()
            thankYouPageAnalytics.get().onCheckPaymentStatusClick(
                thanksPageData.profileCode,
                thanksPageData.paymentID
            )
        }
        setUpHomeButton(btnShopAgain)
    }

    private fun showAnnouncementTicker(
        title: String,
        description: String,
    ) {
        tickerAnnouncement.visible()
        tickerAnnouncement.tickerTitle = title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tickerAnnouncement.setTextDescription(
                HtmlUtil
                    .fromHtml(description).trim()
            )
        } else {
            tickerAnnouncement
                .setHtmlDescription(description)
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
            .sendSalinButtonClickEvent(
                thanksPageData.profileCode, thanksPageData.gatewayName,
                thanksPageData.paymentID
            )
    }

    private fun copyTotalAmountToClipboard(amountStr: String?) {
        amountStr?.let { str ->
            context?.let { context ->
                copyTOClipBoard(context, str)
                view?.let {
                    Toaster.build(
                        it, getString(R.string.thank_you_amount_copy_success),
                        Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL
                    ).show()
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

    private fun setDeadlineTimer(expireOnTimeUnix: Long) {
        tvDeadlineTimer?.let { timerView ->
            val calendar = Calendar.getInstance()
            calendar.time = Date(expireOnTimeUnix * ONE_SECOND_TO_MILLIS)
            timerView.targetDate = calendar
            timerView.onFinish = {
                refreshThanksPageData()
            }
        }
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

    companion object {
        const val HIGHLIGHT_DIGIT_COUNT = 3
        const val ONE_SECOND_TO_MILLIS = 1000L
        const val SCREEN_NAME = "Selesaikan Pembayaran"

        const val GATEWAY_KLIK_BCA = "KlikBCA"
        const val JENIUS = "Jenius Pay"

        fun getFragmentInstance(
            bundle: Bundle,
            thanksPageData: ThanksPageData,
            isWidgetOrderingEnabled: Boolean,
        ): DeferredPaymentFragment = DeferredPaymentFragment().apply {
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
                bundle.putBoolean(ARG_IS_WIDGET_ORDERING_ENABLED, isWidgetOrderingEnabled)
            }
        }
    }
}
