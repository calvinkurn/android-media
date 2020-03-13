package com.tokopedia.thankyou_native.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.ThanksPageData
import com.tokopedia.thankyou_native.helper.*
import com.tokopedia.thankyou_native.presentation.views.ThankYouPageTimerView
import kotlinx.android.synthetic.main.thank_fragment_deferred.*
import javax.inject.Inject

class DeferredPaymentFragment : BaseDaggerFragment(), ThankYouPageTimerView.ThankTimerViewListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var paymentType: PaymentType? = null

    private lateinit var thanksPageData: ThanksPageData

    override fun getScreenName(): String = ""

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
//          thanksPageDataViewModel = viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDataToUi()
        observeViewModel()
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
                    inflateWaitingUI(getString(R.string.thank_account_number), isCopyVisible = true, isWaiting = true)
                    showDigitAnnouncementTicker()
                }
                is VirtualAccount -> inflateWaitingUI(getString(R.string.thank_virtual_account_tag), isCopyVisible = true, isWaiting = true)
                is Retail -> inflateWaitingUI(getString(R.string.thank_payment_code), isCopyVisible = true, isWaiting = true)
                is SmsPayment -> inflateWaitingUI(getString(R.string.thank_phone_number), isCopyVisible = false, isWaiting = true)
            }
        }
    }

    private fun showDigitAnnouncementTicker() {
        tickerAnnouncementExactDigits.visible()
        view_divider_3.gone()
    }

    private fun inflateWaitingUI(numberTypeTitle: String, isCopyVisible: Boolean, isWaiting: Boolean) {
        tvPaymentGatewayName.text = thanksPageData.gatewayName
        ImageLoader.LoadImage(ivPaymentGatewayImage, thanksPageData.gatewayImage)
        tvAccountNumberTypeTag.text = numberTypeTitle
        tvAccountNumber.text = thanksPageData.additionalInfo.accountDest
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
        //todo set bank name
        tvSeeDetail.setOnClickListener { openPaymentDetail() }
        tvSeePaymentMethods.setOnClickListener { openPaymentMethodInfo() }
        if (isWaiting) {
            tvDeadlineTime.text = thanksPageData.expireTimeStr
            tvDeadlineTimer.setStartDuration(getPaymentDuration(), this)
        }

    }

    private fun getPaymentDuration(): Long {
        return if (::thanksPageData.isInitialized) {
            thanksPageData.expireTimeUnix - System.currentTimeMillis()
        } else {
            0L
        }
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

    private fun openPaymentDetail() {
        //todo open payment detail screen
    }

    private fun openPaymentMethodInfo() {
        //todo open payment methods screen
    }

    private fun observeViewModel() {

    }

    companion object {
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
        fun getFragmentInstance(thanksPageData: ThanksPageData): DeferredPaymentFragment = DeferredPaymentFragment().apply {
            val bundle = Bundle()
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

    override fun onTimerFinished() {

    }

}
