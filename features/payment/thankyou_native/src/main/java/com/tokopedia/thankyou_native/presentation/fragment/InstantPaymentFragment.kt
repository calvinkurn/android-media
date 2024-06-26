package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.data.mapper.CashOnDelivery
import com.tokopedia.thankyou_native.data.mapper.PaymentTypeMapper
import com.tokopedia.thankyou_native.domain.model.GatewayAdditionalData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.ThanksSummaryInfo
import com.tokopedia.thankyou_native.helper.getMaskedNumberSubStringPayment
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.ScrollHelper
import com.tokopedia.thankyou_native.presentation.viewModel.CheckWhiteListViewModel
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

const val CHARACTER_LOADER_JSON_ZIP_FILE = "thanks_page_instant_anim.zip"

class InstantPaymentFragment : ThankYouBaseFragment() {

    private lateinit var dialogUnify: DialogUnify

    private val scrollHelper: ScrollHelper by lazy {
        ScrollHelper(this)
    }

    private val checkWhiteListViewModel: CheckWhiteListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(CheckWhiteListViewModel::class.java)
    }

    private var countDownTimer: CountDownTimer? = null
    private var countdownMillis: Long = 0

    override fun getLoadingView(): View? = loadingLayout

    override fun getRecommendationContainer(): LinearLayout? = recommendationContainer
    override fun getFeatureListingContainer(): GyroView? = featureListingContainer
    override fun getTopAdsView(): TopAdsView? = topAdsView
    override fun getBottomContentRecyclerView(): RecyclerView? = rvBottomContent
    override fun getBannerTitle(): Typography? = tvBannerTitle
    override fun getBannerCarousel(): CarouselUnify? = carouselBanner

    override fun onThankYouPageDataReLoaded(data: ThanksPageData) {
        // not required
    }

    override fun getTopTickerView(): Ticker? = topTicker

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
        val v = inflater.inflate(R.layout.thank_fragment_success_payment, container, false)
        scrollHelper.detectHorizontalScroll(v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            checkCreditCardRegisteredForRBA(it)
        }
        observeViewModel()
    }

    override fun onPause() {
        super.onPause()
        cancelGratifDialog()
    }

    override fun onStop() {
        super.onStop()
        cancelGratifDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelGratifDialog()
        countDownTimer?.cancel()
    }

    fun cancelGratifDialog() {
        if (activity is ThankYouPageActivity) {
            (activity as ThankYouPageActivity).cancelGratifDialog()
        }
    }

    override fun bindThanksPageDataToUI(thanksPageData: ThanksPageData) {
        setUpIllustration()

        if (thanksPageData.customDataMessage == null || thanksPageData.customDataMessage.title.isNullOrBlank()) {
            tv_payment_success.text = getString(R.string.thank_instant_payment_successful)
        } else {
            tv_payment_success.text = thanksPageData.customDataMessage.title
        }
        if (thanksPageData.customDataMessage == null || thanksPageData.customDataMessage.subtitle.isNullOrBlank()) {
            tv_payment_success_check_order.text = getString(R.string.thank_instant_payment_check_order)
        } else {
            tv_payment_success_check_order.text = thanksPageData.customDataMessage.subtitle
        }

        if (thanksPageData.customDataMessage == null || thanksPageData.customDataMessage.titleOrderButton.isNullOrBlank()) {
            btn_see_transaction_list.text = getString(R.string.thank_see_transaction_list)
        } else {
            btn_see_transaction_list.text = thanksPageData.customDataMessage.titleOrderButton
        }

        if (thanksPageData.gatewayImage.isNotEmpty()) {
            ivPayment.scaleType = ImageView.ScaleType.CENTER_INSIDE
            ivPayment.setImageUrl(thanksPageData.gatewayImage)
        }

        val gatewayAdditionalData = getGatewayAdditionalInfo()

        if (gatewayAdditionalData != null) {
            tvInstallmentInfo.text = gatewayAdditionalData.value ?: ""
            tvInstallmentInfo.visible()
        } else if (!thanksPageData.additionalInfo.installmentInfo.isNullOrBlank()) {
            tvInstallmentInfo.text = thanksPageData.additionalInfo.installmentInfo
            tvInstallmentInfo.visible()
        } else {
            tvInstallmentInfo.gone()
        }

        if (thanksPageData.additionalInfo.maskedNumber.isNotBlank()) {
            tv_payment_method.text = thanksPageData.additionalInfo.maskedNumber.getMaskedNumberSubStringPayment()
        } else {
            tv_payment_method.text = thanksPageData.gatewayName
        }

        if (thanksPageData.paymentMethodCount > 0) {
            tvPaymentMethodCount.text = getString(R.string.thank_payment_method_count, thanksPageData.paymentMethodCount)
        } else {
            tvPaymentMethodCount.gone()
        }

        tvTotalAmount.text = getString(R.string.thankyou_rp_without_space, thanksPageData.amountStr)

        if (thanksPageData.thanksSummaryInfo.isNullOrEmpty().not()) {
            setSummaryData(thanksPageData.thanksSummaryInfo!!)
        }

        clPaymentMethod.setOnClickListener { openInvoiceDetail(thanksPageData) }

        setUpHomeButton(btnShopAgain)
        setUpSecondaryButton(thanksPageData)

        setUpAutoRedirect()
    }

    private fun setUpAutoRedirect() {
        if (thanksPageData.configFlagData?.autoRedirect != true) return
        tv_payment_success_info.show()
        view_divider_2.hide()

        countDownTimer = object : CountDownTimer(
            thanksPageData.customDataOther?.delayDuration?.toLong()?.times(1000L) ?: 0L,
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                if (context == null) return

                val secondsRemaining = millisUntilFinished / 1000
                val spannableString = SpannableString(thanksPageData.customDataMessage?.loaderText + " " + secondsRemaining.toString() + " detik")

                val secondIndex = spannableString.indexOf(secondsRemaining.toString())
                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), unifyprinciplesR.color.Unify_GN500)), secondIndex, secondIndex + secondsRemaining.toString().length, 0)

                tv_payment_success_info.text = spannableString
            }

            override fun onFinish() {
                if (context == null) return

                RouteManager.route(requireContext(), thanksPageData.customDataAppLink?.autoRedirect)
            }
        }

        countDownTimer?.start()
    }

    private fun setUpSecondaryButton(thanksPageData: ThanksPageData) {
        btn_see_transaction_list.shouldShowWithAction(
            thanksPageData.configFlagData?.shouldHideOrderButton == false
        ) {
            btn_see_transaction_list.setOnClickListener {
                if (thanksPageData.customDataAppLink == null ||
                    thanksPageData.customDataAppLink.order.isNullOrBlank()
                ) {
                    gotoOrderList()
                } else {
                    gotoOrderList(thanksPageData.customDataAppLink.order)
                }
            }
        }
    }

    private fun setSummaryData(thanksSummaryInfo: ArrayList<ThanksSummaryInfo>) {
        context?.let {
            dividerSeparator.visible()
            llSummaryContainer.visible()
            val inflater: LayoutInflater = LayoutInflater.from(context)
            llSummaryContainer.removeAllViews()
            thanksSummaryInfo.forEach { info ->
                if (info.isCta == true) {
                    val detailText = Typography(it)
                    detailText.text = info.ctaText
                    detailText.setType(Typography.SMALL)
                    detailText.setWeight(Typography.BOLD)
                    detailText.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500))

                    val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    detailText.setOnClickListener {
                        openPaymentDetails(info)
                    }
                    llSummaryContainer.addView(detailText)
                    lp.topMargin = 10.toPx()
                    detailText.layoutParams = lp
                    detailText.requestLayout()
                } else {
                    val rowView = inflater.inflate(R.layout.thank_payment_mode_item, null, false)
                    val tvTitle = rowView.findViewById<Typography>(R.id.tvInvoicePaymentModeName)
                    val tvValue = rowView.findViewById<Typography>(R.id.tvInvoicePaidWithModeValue)
                    tvTitle.text = info.desctiption
                    tvValue.text = info.message
                    tvValue.setWeight(Typography.BOLD)

                    val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    lp.topMargin = 4.toPx()
                    rowView.layoutParams = lp
                    llSummaryContainer.addView(rowView)
                }
            }
        }
    }

    private fun openPaymentDetails(info: ThanksSummaryInfo) {
        if (info.ctaApplink.isNullOrEmpty()) {
            if (info.ctaLink.isNullOrEmpty().not()) {
                RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, info.ctaLink)
            }
        } else {
            RouteManager.route(context, info.ctaApplink)
        }
    }

    private fun getGatewayAdditionalInfo(): GatewayAdditionalData? {
        thanksPageData.gatewayAdditionalDataList?.forEach {
            if (thanksPageData.gatewayName == it.key) {
                return it
            }
        }
        return null
    }

    private fun observeViewModel() {
        checkWhiteListViewModel.whiteListResultLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onSuccessFullyRegister()
                    is Fail -> onSingleAuthRegisterFail()
                }
            }
        )
    }

    private fun onSingleAuthRegisterFail() {
        loadingLayout.gone()
        showErrorOnUI(getString(R.string.thank_enable_single_authentication_error)) { enableSingleAuthentication() }
    }

    private fun onSuccessFullyRegister() {
        loadingLayout.gone()
        showToaster(getString(R.string.thank_enable_single_authentication_success))
    }

    private fun checkCreditCardRegisteredForRBA(context: Context) {
        val paymentType = PaymentTypeMapper.getPaymentTypeByStr(thanksPageData.paymentType)
        if (paymentType == CashOnDelivery || thanksPageData.whitelistedRBA) {
            return
        }

        if (::dialogUnify.isInitialized) {
            dialogUnify.cancel()
        }
        if (!thanksPageData.whitelistedRBA) {
            dialogUnify = DialogUnify(
                context = context,
                actionType = DialogUnify.HORIZONTAL_ACTION,
                imageType = DialogUnify.NO_IMAGE
            ).apply {
                setTitle(getString(R.string.thank_single_authentication))
                setDescription(getString(R.string.thank_enable_single_authentication_easy))
                setPrimaryCTAText(getString(R.string.thank_activate_it))
                setSecondaryCTAText(getString(R.string.thank_next_time))
                setPrimaryCTAClickListener { enableSingleAuthentication() }
                setSecondaryCTAClickListener { dialogUnify.cancel() }
                show()
            }
        }
    }

    private fun enableSingleAuthentication() {
        if (::dialogUnify.isInitialized) {
            dialogUnify.cancel()
        }
        loadingLayout.visible()
        checkWhiteListViewModel.registerForSingleAuth()
    }

    companion object {
        const val SCREEN_NAME = "Pembayaran Berhasil"
        fun getFragmentInstance(
            bundle: Bundle?,
            thanksPageData: ThanksPageData,
            isWidgetOrderingEnabled: Boolean
        ): InstantPaymentFragment = InstantPaymentFragment().apply {
            bundle?.let {
                arguments = bundle
                bundle.putParcelable(ARG_THANK_PAGE_DATA, thanksPageData)
                bundle.putBoolean(ARG_IS_WIDGET_ORDERING_ENABLED, isWidgetOrderingEnabled)
            }
        }
    }
}
