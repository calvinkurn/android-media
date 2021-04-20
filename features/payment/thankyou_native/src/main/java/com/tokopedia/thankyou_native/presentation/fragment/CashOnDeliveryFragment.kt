package com.tokopedia.thankyou_native.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.getMaskedNumberSubStringPayment
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*

class CashOnDeliveryFragment : ThankYouBaseFragment() {

    override fun getScreenName(): String = SCREEN_NAME

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.thank_fragment_success_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCharacterAnimation()
    }

    private fun showCharacterAnimation() {
        context?.let {
            val lottieTask = LottieCompositionFactory.fromAsset(context, CHARACTER_LOADER_JSON_ZIP_FILE)
            lottieTask?.addListener { result: LottieComposition? ->
                result?.let {
                    lottieAnimationView?.setComposition(result)
                    lottieAnimationView?.playAnimation()
                }
            }
        }
    }

    override fun getTopTickerView(): Ticker? = topTicker

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer

    override fun getFeatureListingContainer(): GyroView? = featureListingContainer

    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        tv_payment_success.text = getString(R.string.thank_cod_payment_successful)
        tv_payment_success_check_order.text = getString(R.string.thank_cod_payment_check_order)

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

        fun getFragmentInstance(bundle: Bundle?, thanksPageData: ThanksPageData)
                : CashOnDeliveryFragment = CashOnDeliveryFragment().apply {
            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
            }
        }
    }

}
