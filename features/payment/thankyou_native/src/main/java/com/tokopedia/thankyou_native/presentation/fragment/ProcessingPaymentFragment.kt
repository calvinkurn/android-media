package com.tokopedia.thankyou_native.presentation.fragment

import com.tokopedia.imageassets.TokopediaImageUrl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.PaymentType
import com.tokopedia.thankyou_native.data.mapper.PaymentTypeMapper
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.thank_fragment_processing.*
import kotlinx.android.synthetic.main.thank_fragment_processing.btnShopAgain
import kotlinx.android.synthetic.main.thank_fragment_processing.carouselBanner
import kotlinx.android.synthetic.main.thank_fragment_processing.featureListingContainer
import kotlinx.android.synthetic.main.thank_fragment_processing.loadingLayout
import kotlinx.android.synthetic.main.thank_fragment_processing.recommendationContainer
import kotlinx.android.synthetic.main.thank_fragment_processing.rvBottomContent
import kotlinx.android.synthetic.main.thank_fragment_processing.topAdsView
import kotlinx.android.synthetic.main.thank_fragment_processing.topTicker
import kotlinx.android.synthetic.main.thank_fragment_processing.tvBannerTitle
import kotlinx.android.synthetic.main.thank_fragment_processing.tvTotalAmount
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*

class ProcessingPaymentFragment : ThankYouBaseFragment() {

    var paymentType: PaymentType? = null

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)!!
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_processing, container, false)
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
        inflateWaitingUI()
        initCheckPaymentWidgetData()
    }

    override fun getLoadingView(): View? = loadingLayout

    private fun inflateWaitingUI() {
        ivPaymentProcessing.loadImage(URL_THANK_PAYMENT_PROCESSING)
        tvPaymentProcessingTimeInfo.text = getString(R.string.thank_payment_in_progress_time, thanksPageData.gatewayName)
        ivPaymentGatewayImage.loadImage(thanksPageData.gatewayImage)
        ivPaymentGatewayImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
        tvPaymentGatewayName.text = thanksPageData.gatewayName
        tvCreditWithTimeLine.text = thanksPageData.additionalInfo.installmentInfo
        tvInterestRate.text = getString(R.string.thank_interest_rate, thanksPageData.additionalInfo.interest)
        tvTotalAmount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)
        tvSeeDetail.setOnClickListener { openInvoiceDetail(thanksPageData) }
        if (thanksPageData.customDataMessage == null || thanksPageData.customDataMessage?.wtvText.isNullOrBlank()) {
            tvCheckPaymentStatusTitle.text = getString(R.string.thank_processing_payment_check_order)
        } else {
            tvCheckPaymentStatusTitle.text = thanksPageData.customDataMessage?.wtvText
        }
    }

    private fun initCheckPaymentWidgetData() {
        btnCheckPaymentStatus.setOnClickListener {
            thankYouPageAnalytics.get().onCheckPaymentStatusClick(thanksPageData.profileCode,
                thanksPageData.paymentID
            )
            refreshThanksPageData()
        }
        setUpHomeButton(btnShopAgain)
    }

    override fun onThankYouPageDataReLoaded(data: ThanksPageData) {
        loadingLayout.gone()
        thanksPageData = data
        showPaymentStatusDialog(false, thanksPageData)
    }

    internal fun onBackPressed(): Boolean {
        refreshThanksPageData()
        return true
    }

    companion object {
        const val SCREEN_NAME = "Pembayaran Diproses"
        fun getFragmentInstance(
            bundle: Bundle,
            thanksPageData: ThanksPageData,
            isWidgetOrderingEnabled: Boolean,
        ): ProcessingPaymentFragment = ProcessingPaymentFragment().apply {
            bundle.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
                bundle.putBoolean(ARG_IS_WIDGET_ORDERING_ENABLED, isWidgetOrderingEnabled)
            }
        }

        private const val URL_THANK_PAYMENT_PROCESSING = TokopediaImageUrl.URL_THANK_PAYMENT_PROCESSING
    }

}
