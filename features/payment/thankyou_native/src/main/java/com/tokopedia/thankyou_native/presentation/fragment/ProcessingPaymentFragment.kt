package com.tokopedia.thankyou_native.presentation.fragment

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
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.PaymentType
import com.tokopedia.thankyou_native.helper.PaymentTypeMapper
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.DialogOrigin
import com.tokopedia.thankyou_native.presentation.helper.OriginCheckStatusButton
import com.tokopedia.thankyou_native.presentation.helper.OriginOnBackPress
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.thankyou_native.recommendation.presentation.view.PDPThankYouPageView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_processing.*
import javax.inject.Inject

class ProcessingPaymentFragment : ThankYouBaseFragment() {

    private lateinit var thanksPageDataViewModel: ThanksPageDataViewModel


    @Inject
    lateinit var thankYouPageAnalytics: ThankYouPageAnalytics


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var paymentType: PaymentType? = null

    private var dialogOrigin: DialogOrigin? = null

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
        return inflater.inflate(R.layout.thank_fragment_processing, container, false)
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
        inflateWaitingUI()
        initCheckPaymentWidgetData()
    }

    private fun inflateWaitingUI() {
        tvPaymentProcessingTimeInfo.text = getString(R.string.thank_payment_in_progress_time, thanksPageData.gatewayName)
        ImageLoader.LoadImage(ivPaymentGatewayImage, thanksPageData.gatewayImage)
        tvPaymentGatewayName.text = thanksPageData.gatewayName
        tvCreditWithTimeLine.text = thanksPageData.additionalInfo.installmentInfo
        tvInterestRate.text = getString(R.string.thank_interest_rate, thanksPageData.additionalInfo.interest)
        tvTotalAmount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)
        tvSeeDetail.setOnClickListener { openInvoiceDetail(thanksPageData) }
    }

    private fun initCheckPaymentWidgetData() {
        btnCheckPaymentStatus.setOnClickListener {
            dialogOrigin = OriginCheckStatusButton
            checkPaymentStatus()
        }
        btnShopAgain.setOnClickListener {
            gotoHomePage()
        }
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

    private fun onThankYouPageDataLoadingFail(throwable: Throwable) {
        loading_layout.gone()
    }

    private fun onThankYouPageDataLoaded(data: ThanksPageData) {
        loading_layout.gone()
        thanksPageData = data
        showPaymentStatusDialog(dialogOrigin, thanksPageData)
    }

    internal fun onBackPressed(): Boolean {
        dialogOrigin = OriginOnBackPress
        checkPaymentStatus()
        return true
    }

    companion object {
        const val SCREEN_NAME = "Pembayaran Diproses"
        private const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
        fun getFragmentInstance(bundle: Bundle, thanksPageData: ThanksPageData):
                ProcessingPaymentFragment = ProcessingPaymentFragment().apply {
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}