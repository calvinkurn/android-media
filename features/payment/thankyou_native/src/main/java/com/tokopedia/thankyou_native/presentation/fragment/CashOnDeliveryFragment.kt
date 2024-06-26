package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.getMaskedNumberSubStringPayment
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*

class CashOnDeliveryFragment : ThankYouBaseFragment() {

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_success_payment, container, false)
    }

    override fun getTopTickerView(): Ticker? = topTicker

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer

    override fun getFeatureListingContainer(): GyroView? = featureListingContainer
    override fun getTopAdsView(): TopAdsView? = topAdsView
    override fun getBottomContentRecyclerView(): RecyclerView? = rvBottomContent
    override fun getBannerTitle(): Typography? = tvBannerTitle
    override fun getBannerCarousel(): CarouselUnify? = carouselBanner

    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        tv_payment_success.text = getString(R.string.thank_cod_payment_successful)
        tv_payment_success_check_order.text = getString(R.string.thank_cod_payment_check_order)

        setUpIllustration()

        if(thanksPageData.gatewayImage.isNotEmpty()){
            ivPayment.scaleType = ImageView.ScaleType.CENTER_INSIDE
            ivPayment.setImageUrl(thanksPageData.gatewayImage)
        }

        if (thanksPageData.additionalInfo.maskedNumber.isNotBlank()) {
            tv_payment_method.text = thanksPageData.additionalInfo
                    .maskedNumber.getMaskedNumberSubStringPayment()
        } else
            tv_payment_method.text = thanksPageData.gatewayName

        if (thanksPageData.paymentMethodCount > 0)
            tvPaymentMethodCount.text = getString(R.string.thank_payment_method_count,
                    thanksPageData.paymentMethodCount)
        else
            tvPaymentMethodCount.gone()

        clPaymentMethod.setOnClickListener { openInvoiceDetail(thanksPageData) }

        tvTotalAmount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)

        btn_see_transaction_list.setOnClickListener { gotoOrderList() }

        setUpHomeButton(btnShopAgain)
    }

    override fun getLoadingView(): View? = null

    override fun onThankYouPageDataReLoaded(data: ThanksPageData) {
        //not required
    }

    companion object {
        const val SCREEN_NAME = "Pembayaran Berhasil"

        fun getFragmentInstance(
            bundle: Bundle?,
            thanksPageData: ThanksPageData,
            isWidgetOrderingEnabled: Boolean,
        )
                : CashOnDeliveryFragment = CashOnDeliveryFragment().apply {
            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
                bundle.putBoolean(ARG_IS_WIDGET_ORDERING_ENABLED, isWidgetOrderingEnabled)
            }
        }
    }

}
