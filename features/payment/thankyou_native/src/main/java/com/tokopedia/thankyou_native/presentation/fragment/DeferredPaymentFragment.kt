package com.tokopedia.thankyou_native.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.DialogOrigin
import com.tokopedia.thankyou_native.presentation.helper.OriginCheckStatusButton
import com.tokopedia.thankyou_native.presentation.helper.OriginOnBackPress
import com.tokopedia.thankyou_native.presentation.helper.OriginTimerFinished
import com.tokopedia.thankyou_native.presentation.viewModel.DetailInvoiceViewModel
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.thankyou_native.presentation.views.PDPThankYouPageView
import com.tokopedia.thankyou_native.presentation.views.ThankYouPageTimerView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_deferred.*
import javax.inject.Inject

class DeferredPaymentFragment : ThankYouBaseFragment(), ThankYouPageTimerView.ThankTimerViewListener {

    private lateinit var thanksPageDataViewModel: ThanksPageDataViewModel
    private lateinit var detailInvoiceViewModel: DetailInvoiceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var paymentType: PaymentType? = null

    var dialogOrigin: DialogOrigin? = null

    private lateinit var thanksPageData: ThanksPageData

    private var dialog: DialogUnify? = null

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
        detailInvoiceViewModel = viewModelProvider.get(DetailInvoiceViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deferredPDPView.fragment = this
        observeViewModel()
        bindDataToUi()
    }

    override fun openInvoiceDetail() {
        detailInvoiceViewModel.createInvoiceData(thanksPageData)
    }

    override fun getRecommendationView(): PDPThankYouPageView? {
        return deferredPDPView
    }

    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onThankYouPageDataLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })

        detailInvoiceViewModel.mutableInvoiceVisitables.observe(this, Observer {
            openDetailedInvoiceBottomsheet(it)
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
                    inflateWaitingUI(getString(R.string.thank_account_number), isCopyVisible = true)
                    showDigitAnnouncementTicker()
                }
                is VirtualAccount -> inflateWaitingUI(getString(R.string.thank_virtual_account_tag), isCopyVisible = true)
                is Retail -> inflateWaitingUI(getString(R.string.thank_payment_code), isCopyVisible = true)
                is SmsPayment -> inflateWaitingUI(getString(R.string.thank_phone_number), isCopyVisible = false)
            }
        }
        initCheckPaymentWidgetData()
    }

    private fun inflateWaitingUI(numberTypeTitle: String, isCopyVisible: Boolean) {
        tvPaymentGatewayName.text = thanksPageData.gatewayName
        ImageLoader.LoadImage(ivPaymentGatewayImage, thanksPageData.gatewayImage)
        tvAccountNumberTypeTag.text = numberTypeTitle
        tvAccountNumber.text = thanksPageData.additionalInfo?.accountDest ?: ""
        tvTotalAmount.text = getString(R.string.thankyou_rp, thanksPageData.amountStr)

        if (isCopyVisible) {
            tvAccountNumberCopy.visible()
            tvAccountNumberCopy.tag = thanksPageData.additionalInfo
            tvAccountNumberCopy.setOnClickListener {
                val accountNumberStr: String? = tvAccountNumberCopy.tag?.toString()
                copyAccountNumberToClipboard(accountNumberStr)
            }
        } else {
            tvAccountNumberCopy.gone()
        }
        if (thanksPageData.additionalInfo.bankName.isNotBlank()) {
            tvBankName.text = thanksPageData.additionalInfo.bankName
            tvBankName.visible()
        }
        tvSeeDetail.setOnClickListener { openInvoiceDetail() }
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
        view_divider_3.gone()
    }

    private fun copyAccountNumberToClipboard(accountNumberStr: String?) {
        accountNumberStr?.let { str ->
            context?.let { context ->
                val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Tokopedia", str)
                clipboard.primaryClip = clip
            }
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
        const val SCREEN_NAME = "Selesaikan Pembayaran"
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
