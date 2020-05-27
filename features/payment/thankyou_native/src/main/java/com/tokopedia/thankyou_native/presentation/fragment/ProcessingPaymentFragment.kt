package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.PaymentType
import com.tokopedia.thankyou_native.data.mapper.PaymentTypeMapper
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendation.presentation.view.MarketPlaceRecommendation
import kotlinx.android.synthetic.main.thank_fragment_processing.*

class ProcessingPaymentFragment : ThankYouBaseFragment() {

    var paymentType: PaymentType? = null

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_processing, container, false)
    }

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer

    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        paymentType = PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)
        inflateWaitingUI()
        initCheckPaymentWidgetData()
    }

    override fun getLoadingView(): View? = loading_layout

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
            refreshThanksPageData()
        }
        btnShopAgain.setOnClickListener {
            gotoHomePage()
        }
    }

    override fun onThankYouPageDataReLoaded(data: ThanksPageData) {
        loading_layout.gone()
        thanksPageData = data
        showPaymentStatusDialog(false, thanksPageData)
    }

    internal fun onBackPressed(): Boolean {
        refreshThanksPageData()
        return true
    }

    companion object {
        const val SCREEN_NAME = "Pembayaran Diproses"
        fun getFragmentInstance(bundle: Bundle, thanksPageData: ThanksPageData):
                ProcessingPaymentFragment = ProcessingPaymentFragment().apply {
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}