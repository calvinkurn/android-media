package com.tokopedia.thankyou_native.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.DialogOrigin
import com.tokopedia.thankyou_native.presentation.helper.OriginCheckStatusButton
import com.tokopedia.thankyou_native.presentation.helper.OriginOnBackPress
import com.tokopedia.thankyou_native.presentation.helper.OriginTimerFinished
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.thankyou_native.presentation.views.ThankYouPageTimerView
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_deferred.*
import javax.inject.Inject

class DeferredPaymentFragment : ThankYouBaseFragment(), ThankYouPageTimerView.ThankTimerViewListener {

    private lateinit var thanksPageDataViewModel: ThanksPageDataViewModel

    @Inject
    lateinit var thankYouPageAnalytics: ThankYouPageAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var paymentType: PaymentType? = null

    private var dialogOrigin: DialogOrigin? = null

    private lateinit var thanksPageData: ThanksPageData

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)
            }
        }
        initViewModels()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_deferred, container, false)
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        thanksPageDataViewModel = viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        bindDataToUi()
    }

    override fun getThankPageData(): ThanksPageData {
        return thanksPageData
    }

    override fun getRecommendationView(): PDPThankYouPageView? {
        return deferredPDPView
    }

    override fun getThankPageAnalytics(): ThankYouPageAnalytics {
        return thankYouPageAnalytics
    }

    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onThankYouPageDataLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })
    }

    private fun bindDataToUi() {
        activity?.let {
            if (!::thanksPageData.isInitialized)
                it.finish()
        }
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
        initCheckPaymentWidgetData()
    }

    private fun highlightLastThreeDigits(amountStr: String) {
        tvTotalAmount.setTextColor(resources.getColor(com.tokopedia.design.R.color.grey_796))
        val spannable = SpannableString(getString(R.string.thankyou_rp_without_space, amountStr))
        if (amountStr.length > 3) {
            val startIndex = spannable.length - 3
            spannable.setSpan(ForegroundColorSpan(resources.getColor(com.tokopedia.design.R.color.orange_500)),
                    startIndex, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tvTotalAmount.text = spannable
    }

    private fun inflateWaitingUI(numberTypeTitle: String?, isCopyVisible: Boolean, highlightAmountDigits: Boolean) {
        tvPaymentGatewayName.text = thanksPageData.gatewayName
        ImageLoader.LoadImage(ivPaymentGatewayImage, thanksPageData.gatewayImage)
        numberTypeTitle?.let {
            tvAccountNumberTypeTag.text = numberTypeTitle
            tvAccountNumber.text = thanksPageData.additionalInfo?.accountDest ?: ""
        } ?: run {
            tvAccountNumberTypeTag.gone()
            tvAccountNumber.gone()
        }
        if (highlightAmountDigits)
            highlightLastThreeDigits(thanksPageData.amountStr)
        else
            tvTotalAmount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)

        if (isCopyVisible) {
            tvAccountNumberCopy.visible()
            tvAccountNumberCopy.tag = thanksPageData.additionalInfo.accountDest
            tvAccountNumberCopy.setOnClickListener {
                val accountNumberStr: String? = tvAccountNumberCopy.tag?.toString()
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
        tvSeePaymentMethods.setOnClickListener { openHowTOPay(thanksPageData) }
        tvDeadlineTime.text = thanksPageData.expireTimeStr
        tvDeadlineTimer.setExpireTimeUnix(thanksPageData.expireTimeUnix, this)
    }

    private fun initCheckPaymentWidgetData() {
        btnCheckPaymentStatus.setOnClickListener {
            dialogOrigin = OriginCheckStatusButton
            checkPaymentStatus()
        }
        btnShopAgain.setOnClickListener { gotoHomePage() }
    }

    private fun checkPaymentStatus() {
        arguments?.let {
            if (it.containsKey(ThankYouPageActivity.ARG_PAYMENT_ID) && it.containsKey(ThankYouPageActivity.ARG_MERCHANT)) {
                refreshThanksPageData(it.getLong(ThankYouPageActivity.ARG_PAYMENT_ID),
                        it.getString(ThankYouPageActivity.ARG_MERCHANT, ""))
            }
        }
    }

    private fun refreshThanksPageData(paymentId: Long, merchantID: String) {
        loading_layout.visible()
        thanksPageDataViewModel.getThanksPageData(paymentId, merchantID)
    }

    private fun showDigitAnnouncementTicker() {
        tickerAnnouncementExactDigits.visible()
        tickerAnnouncementExactDigits
                .setTextDescription(getString(R.string.thank_exact_transfer_upto_3_digits))
        view_divider_3.gone()
    }

    private fun copyAccountNumberToClipboard(accountNumberStr: String?) {
        thankYouPageAnalytics.sendSalinButtonClickEvent(thanksPageData.gatewayName)
        accountNumberStr?.let { str ->
            context?.let { context ->
                val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE)
                        as ClipboardManager
                val clip = ClipData.newPlainText(COPY_BOARD_LABEL, str.replace("\\s+".toRegex(), ""))
                clipboard.primaryClip = clip
                showToastCopySuccessFully(context)
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
        dialogOrigin = OriginTimerFinished
        checkPaymentStatus()
    }

    override fun onStart() {
        super.onStart()
        tvDeadlineTimer.startTimer()
    }

    override fun onStop() {
        super.onStop()
        tvDeadlineTimer.stopTimer()
    }

    private fun onThankYouPageDataLoadingFail(throwable: Throwable) {
        loading_layout.gone()
    }

    private fun onThankYouPageDataLoaded(data: ThanksPageData) {
        loading_layout.gone()
        thanksPageData = data
        showPaymentStatusDialog(dialogOrigin, thanksPageData)
    }

    private fun isPaymentTimerExpired(): Boolean {
        if (thanksPageData.expireTimeUnix <= System.currentTimeMillis() / 1000L)
            return true
        return false
    }

    internal fun onBackPressed(): Boolean {
        if (!isPaymentTimerExpired()) {
            dialogOrigin = OriginOnBackPress
            checkPaymentStatus()
            return true
        }
        return false
    }

    companion object {
        private val COPY_BOARD_LABEL = "Tokopedia"
        const val SCREEN_NAME = "Selesaikan Pembayaran"

        const val GATEWAY_KLIK_BCA = "KlikBCA"

        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
        fun getFragmentInstance(bundle: Bundle, thanksPageData: ThanksPageData):
                DeferredPaymentFragment = DeferredPaymentFragment().apply {
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }
}
