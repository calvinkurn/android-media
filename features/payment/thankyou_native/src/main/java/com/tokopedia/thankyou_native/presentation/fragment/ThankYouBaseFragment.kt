package com.tokopedia.thankyou_native.presentation.fragment

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.helper.DialogHelper
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.thankyou_native.recommendation.presentation.view.IRecommendationView
import com.tokopedia.thankyou_native.recommendation.presentation.view.MarketPlaceRecommendation
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendation
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.IDigitalRecommendationView
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


abstract class ThankYouBaseFragment : BaseDaggerFragment(), OnDialogRedirectListener {

    abstract fun getRecommendationContainer(): LinearLayout?
    abstract fun bindThanksPageDataToUI(thanksPageData: ThanksPageData)
    abstract fun getLoadingView(): View?
    abstract fun onThankYouPageDataReLoaded(data: ThanksPageData)

    private lateinit var dialogHelper: DialogHelper

    @Inject
    lateinit var thankYouPageAnalytics: dagger.Lazy<ThankYouPageAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private var iRecommendationView: IRecommendationView? = null

    private val marketRecommendationPlaceLayout = R.layout.thank_layout_market_place_recom

    private var iDigitalRecommendationView: IDigitalRecommendationView? = null

    private val digitalRecommendationLayout = R.layout.thank_layout_digital_recom

    private val thanksPageDataViewModel: ThanksPageDataViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    private var topAdsTrackingQueue: TrackingQueue? = null
    private var nonTopAdsTrackingQueue: TrackingQueue? = null
    private var digitalRecomTrackingQueue: TrackingQueue? = null

    lateinit var thanksPageData: ThanksPageData

    override fun initInjector() {
        getComponent(ThankYouPageComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_THANK_PAGE_DATA)) {
                thanksPageData = it.getParcelable(ARG_THANK_PAGE_DATA)!!
            }
        }
        activity?.apply {
            nonTopAdsTrackingQueue = TrackingQueue(this)
            topAdsTrackingQueue = TrackingQueue(this)
            digitalRecomTrackingQueue = TrackingQueue(this)
        }

    }

    override fun onPause() {
        super.onPause()
        nonTopAdsTrackingQueue?.sendAll()
        topAdsTrackingQueue?.sendAll()
        digitalRecomTrackingQueue?.sendAll()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::thanksPageData.isInitialized)
            activity?.finish()
        else {
            bindThanksPageDataToUI(thanksPageData)
            observeViewModel()
            addRecommendation()
        }
    }

    private fun addRecommendation() {
        when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
            is MarketPlaceThankPage -> {
                addMarketPlaceRecommendation()
                addDigitalRecommendation()
            }
            is DigitalThankPage -> {
                addDigitalRecommendation()
                addMarketPlaceRecommendation()
            }
        }
    }

    private fun addMarketPlaceRecommendation() {
        val recomContainer = getRecommendationContainer()
        iRecommendationView = recomContainer?.let { container ->
            val view = getRecommendationView(marketRecommendationPlaceLayout)
            container.addView(view)
            view.findViewById<MarketPlaceRecommendation>(R.id.marketPlaceRecommendationView)
        }
        if (::thanksPageData.isInitialized)
            iRecommendationView?.loadRecommendation(thanksPageData.paymentID.toString(),
                    this, topAdsTrackingQueue, nonTopAdsTrackingQueue)
    }

    private fun addDigitalRecommendation() {
        val recomContainer = getRecommendationContainer()
        iDigitalRecommendationView = recomContainer?.let { container ->
            val view = getRecommendationView(digitalRecommendationLayout)
            container.addView(view)
            view.findViewById<DigitalRecommendation>(R.id.digitalRecommendationView)
        }
        if (::thanksPageData.isInitialized)
            iDigitalRecommendationView?.loadRecommendation(thanksPageData.paymentID.toString(),
                    this, digitalRecomTrackingQueue)
    }

    private fun getRecommendationView(@LayoutRes layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, null, false)
    }

    fun refreshThanksPageData() {
        getLoadingView()?.visible()
        arguments?.let {
            if (it.containsKey(ThankYouPageActivity.ARG_PAYMENT_ID) && it.containsKey(ThankYouPageActivity.ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(it.getLong(ThankYouPageActivity.ARG_PAYMENT_ID),
                        it.getString(ThankYouPageActivity.ARG_MERCHANT, ""))
            }
        }
    }

    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onThankYouPageDataReLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })
    }

    private fun onThankYouPageDataLoadingFail(throwable: Throwable) {
        getLoadingView()?.gone()
    }

    fun openHowTOPay(thanksPageData: ThanksPageData) {
        RouteManager.route(context, thanksPageData.howToPay)
        thankYouPageAnalytics.get().sendOnHowtoPayClickEvent(thanksPageData.paymentID.toString())
    }

    fun showPaymentStatusDialog(isTimerFinished: Boolean,
                                thanksPageData: ThanksPageData) {
        var paymentStatus = PaymentStatusMapper
                .getPaymentStatusByInt(thanksPageData.paymentStatus)

        if (isTimerFinished && !isPaymentVerified(paymentStatus))
            paymentStatus = PaymentExpired

        context?.let {
            if (!::dialogHelper.isInitialized)
                dialogHelper = DialogHelper(it, this)
            dialogHelper.showPaymentStatusDialog(paymentStatus)
        }
    }

    private fun isPaymentVerified(paymentStatus: PaymentStatus?): Boolean {
        return when (paymentStatus) {
            is PaymentVerified -> true
            else -> false
        }
    }

    fun setUpHomeButton(homeButton: TextView?) {
        homeButton?.let {
            homeButton.setOnClickListener {
                thanksPageData.thanksCustomization?.let {
                    if (it.customHomeUrlApp.isNullOrBlank())
                        gotoHomePage()
                    else
                        launchApplink(it.customHomeUrlApp)
                } ?: run {
                    gotoHomePage()
                }
            }
        }
    }

    fun openInvoiceDetail(thanksPageData: ThanksPageData) {
        InvoiceFragment.openInvoiceBottomSheet(activity, thanksPageData)
        thankYouPageAnalytics.get().sendLihatDetailClickEvent(PaymentPageMapper
                .getPaymentPageType(thanksPageData.pageType), thanksPageData.paymentID.toString())
    }

    override fun gotoHomePage() {
        RouteManager.route(context, ApplinkConst.HOME, "")
        thankYouPageAnalytics.get().sendBelanjaLagiClickEvent(
                PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
                thanksPageData.paymentID.toString())
        activity?.finish()
    }

    override fun launchApplink(applink: String) {
        val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
        val intent = RouteManager.getIntent(context, applink, "")
        intent?.let {
            TaskStackBuilder.create(context)
                    .addNextIntent(homeIntent)
                    .addNextIntent(intent)
                    .startActivities()
        }
        thankYouPageAnalytics.get().sendBelanjaLagiClickEvent(
                PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
                thanksPageData.paymentID.toString())
        activity?.finish()
    }


    override fun gotoPaymentWaitingPage() {
        val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
        val paymentListIntent = RouteManager.getIntent(context, ApplinkConst.PMS, "")
        paymentListIntent?.let {
            TaskStackBuilder.create(context)
                    .addNextIntent(homeIntent)
                    .addNextIntent(paymentListIntent)
                    .startActivities()
        }
        activity?.finish()
    }

    override fun gotoOrderList() {
        try {
            thankYouPageAnalytics.get()
                    .sendCheckTransactionListEvent(thanksPageData.paymentID.toString())
            val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
            val orderListListIntent = getOrderListPageIntent()
            orderListListIntent?.let {
                TaskStackBuilder.create(context)
                        .addNextIntent(homeIntent)
                        .addNextIntent(orderListListIntent)
                        .startActivities()
            }
            activity?.finish()
        } catch (e: Exception) {
        }
    }

    override fun gotoOrderList(applink: String) {
        try {
            if(applink.isNullOrBlank()){
                gotoOrderList()
            } else {
                thankYouPageAnalytics.get()
                        .sendCheckTransactionListEvent(thanksPageData.paymentID.toString())
                val homeIntent = RouteManager.getIntent(context, ApplinkConst.HOME, "")
                val orderListListIntent = RouteManager.getIntent(context, applink)
                orderListListIntent?.let {
                    TaskStackBuilder.create(context)
                            .addNextIntent(homeIntent)
                            .addNextIntent(orderListListIntent)
                            .startActivities()
                }
                activity?.finish()
            }
        } catch (e: Exception) {
        }
    }


    private fun getOrderListPageIntent(): Intent? {
        return RouteManager.getIntent(context, ApplinkConst.MARKETPLACE_ORDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (iRecommendationView != null) {
            iRecommendationView?.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun showErrorOnUI(errorMessage: String, retry: (() -> Unit)?) {
        view?.let { view ->
            retry?.let {
                Toaster.make(view, errorMessage,
                        Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                        getString(R.string.thank_coba_lagi), View.OnClickListener { retry.invoke() })
            }
        }
    }

    fun showToaster(message: String) {
        view?.let { Toaster.make(it, message, Toaster.LENGTH_SHORT) }
    }

    companion object {
        const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
    }
}