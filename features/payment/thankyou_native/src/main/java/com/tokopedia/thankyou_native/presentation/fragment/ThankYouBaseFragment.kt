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
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.response.DefaultChosenAddressData
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.GyroRecommendationAnalytics
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.*
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ConfigFlag
import com.tokopedia.thankyou_native.domain.model.ThankPageTopTickerData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.activity.ARG_MERCHANT
import com.tokopedia.thankyou_native.presentation.activity.ARG_PAYMENT_ID
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.helper.DialogHelper
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.thankyou_native.recommendation.presentation.view.IRecommendationView
import com.tokopedia.thankyou_native.recommendation.presentation.view.MarketPlaceRecommendation
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendation
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.IDigitalRecommendationView
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


abstract class ThankYouBaseFragment : BaseDaggerFragment(), OnDialogRedirectListener {

    abstract fun getRecommendationContainer(): LinearLayout?
    abstract fun getFeatureListingContainer(): GyroView?
    abstract fun getTopAdsView(): TopAdsView?
    abstract fun bindThanksPageDataToUI(thanksPageData: ThanksPageData)
    abstract fun getLoadingView(): View?
    abstract fun onThankYouPageDataReLoaded(data: ThanksPageData)
    abstract fun getTopTickerView(): Ticker?

    private lateinit var dialogHelper: DialogHelper

    @Inject
    lateinit var thankYouPageAnalytics: dagger.Lazy<ThankYouPageAnalytics>

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var gyroRecommendationAnalytics: dagger.Lazy<GyroRecommendationAnalytics>

    private var iRecommendationView: IRecommendationView? = null

    private val marketRecommendationPlaceLayout = R.layout.thank_layout_market_place_recom

    private var iDigitalRecommendationView: IDigitalRecommendationView? = null

    private val digitalRecommendationLayout = R.layout.thank_layout_digital_recom

    private val thanksPageDataViewModel: ThanksPageDataViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

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
            digitalRecomTrackingQueue = TrackingQueue(this)
        }

    }


    override fun onPause() {
        super.onPause()
        digitalRecomTrackingQueue?.sendAll()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFeatureListingContainer()?.gone()
        if (!::thanksPageData.isInitialized)
            activity?.finish()
        else {
            bindThanksPageDataToUI(thanksPageData)
            observeViewModel()
            getFeatureRecommendationData()
            addRecommendation()
            getTopTickerData()
            thanksPageDataViewModel.resetAddressToDefault()
        }
    }

    private fun getFeatureRecommendationData() {
        val configFlag: ConfigFlag? = thanksPageData.configFlag?.let {
            Gson().fromJson(it, ConfigFlag::class.java)
        }
        configFlag?.apply {
            if (isThanksWidgetEnabled)
                thanksPageDataViewModel.getFeatureEngine(thanksPageData)
        }
    }

    private fun addRecommendation() {
        val pgCategoryIds = mutableListOf<Int>()
        when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
            is MarketPlaceThankPage -> {
                thanksPageData.shopOrder.forEach { shopOrder ->
                    shopOrder.purchaseItemList.forEach { purchaseItem ->
                        val categoryId = purchaseItem.categoryId.toIntOrNull()
                        categoryId?.let {
                            pgCategoryIds.add(it)
                        }
                    }
                }
                addMarketPlaceRecommendation()
                addDigitalRecommendation(pgCategoryIds, MarketPlaceThankPage)
            }
            is DigitalThankPage -> {
                addDigitalRecommendation(pgCategoryIds, DigitalThankPage)
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
            iRecommendationView?.loadRecommendation(thanksPageData, this)
    }

    private fun addDigitalRecommendation(pgCategoryIds: List<Int> = listOf(), pageType: ThankPageType) {
        val recomContainer = getRecommendationContainer()
        iDigitalRecommendationView = recomContainer?.let { container ->
            val view = getRecommendationView(digitalRecommendationLayout)
            container.addView(view)
            view.findViewById<DigitalRecommendation>(R.id.digitalRecommendationView)
        }
        if (::thanksPageData.isInitialized)
            iDigitalRecommendationView?.loadRecommendation(thanksPageData,
                    this, digitalRecomTrackingQueue, pgCategoryIds, pageType)
    }

    private fun getRecommendationView(@LayoutRes layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, null, false)
    }

    fun refreshThanksPageData() {
        getLoadingView()?.visible()
        arguments?.let {
            if (it.containsKey(ARG_PAYMENT_ID) && it.containsKey(ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(it.getLong(ARG_PAYMENT_ID),
                        it.getString(ARG_MERCHANT, ""))
            }
        }
    }

    private fun getTopTickerData() {
        thanksPageDataViewModel.getThanksPageTicker(thanksPageData.configList)
    }


    private fun observeViewModel() {
        thanksPageDataViewModel.thanksPageDataResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onThankYouPageDataReLoaded(it.data)
                is Fail -> onThankYouPageDataLoadingFail(it.throwable)
            }
        })
        thanksPageDataViewModel.gyroRecommendationLiveData.observe(viewLifecycleOwner, Observer {
            addDataToGyroRecommendationView(it)
        })

        thanksPageDataViewModel.topTickerLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setTopTickerData(it.data)
                }
                is Fail -> getTopTickerView()?.gone()
            }
        })

        thanksPageDataViewModel.defaultAddressLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success->{
                    updateLocalizingAddressData(it.data)
                }
                is Fail->{
                    //do nothing
                }
            }
        })

        thanksPageDataViewModel.topAdsDataLiveData.observe(viewLifecycleOwner) {
            addDataToTopAdsView(it)
        }

    }

    private fun updateLocalizingAddressData(data: DefaultChosenAddressData) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(it,
                    data.addressId.toString(), data.cityId.toString(),
                    data.districtId.toString(),
                    data.latitude, data.longitude,
                    "${data.addressName} ${data.receiverName}",
                    data.postalCode, "", "")
        }
    }

    private fun setTopTickerData(tickerData: List<TickerData>) {
        context?.let { context ->
            getTopTickerView()?.apply {
                visible()
                val tickerViewPagerAdapter = TickerPagerAdapter(context, tickerData)
                addPagerView(tickerViewPagerAdapter, tickerData)
                tickerViewPagerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                    override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                        if (itemData is ThankPageTopTickerData) {
                            if (itemData.isAppLink()) {
                                openAppLink(linkUrl.toString())
                            } else
                                openWebLink(linkUrl.toString())
                        }
                    }
                })
            }
        }
    }

    private fun addDataToGyroRecommendationView(gyroRecommendation: GyroRecommendation) {
        if (::thanksPageData.isInitialized) {
            if (!gyroRecommendation.gyroVisitable.isNullOrEmpty()) {
                getFeatureListingContainer()?.visible()
                getFeatureListingContainer()?.addData(gyroRecommendation, thanksPageData,
                        gyroRecommendationAnalytics.get())
            } else {
                getFeatureListingContainer()?.gone()
            }
        }
    }
    private fun addDataToTopAdsView(data: TopAdsRequestParams) {
        if (!data.topAdsUIModelList.isNullOrEmpty()) {
            getTopAdsView()?.visible()
            getTopAdsView()?.addData(data)
        } else {
            getTopAdsView()?.gone()
        }
    }

    private fun onThankYouPageDataLoadingFail(throwable: Throwable) {
        getLoadingView()?.gone()
    }

    fun openHowToPay(thanksPageData: ThanksPageData) {
        thanksPageData.howToPayAPP?.let {
            RouteManager.route(context, thanksPageData.howToPayAPP)
            thankYouPageAnalytics.get().sendOnHowtoPayClickEvent(thanksPageData.profileCode,
                    thanksPageData.paymentID.toString())
        }
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
            thanksPageData.thanksCustomization?.let {
                it.customHomeButtonTitle?.apply {
                    if (isNotBlank())
                        homeButton.text = this
                }
            }

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
        thankYouPageAnalytics.get().sendLihatDetailClickEvent(thanksPageData.profileCode,
                PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
                thanksPageData.paymentID.toString())

        if (activity is ThankYouPageActivity) {
            (activity as ThankYouPageActivity).cancelGratifDialog()
        }
    }

    override fun gotoHomePage() {
        RouteManager.route(context, ApplinkConst.HOME, "")
        thankYouPageAnalytics.get().sendBelanjaLagiClickEvent(thanksPageData.profileCode,
                PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
                thanksPageData.paymentID.toString())
        activity?.finish()
    }

    private fun openWebLink(urlStr : String?) {
        urlStr?.let {
            activity?.apply {
                RouteManager.route(this,
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, urlStr))
            }
        }
    }

    private fun openAppLink(appLink: String?) {
        appLink?.let {
            activity?.apply {
                RouteManager.route(this, appLink)
            }
        }
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
        thankYouPageAnalytics.get().sendBelanjaLagiClickEvent(thanksPageData.profileCode,
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
                    .sendCheckTransactionListEvent(thanksPageData.profileCode,
                            thanksPageData.paymentID.toString())
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
            if (applink.isNullOrBlank()) {
                gotoOrderList()
            } else {
                thankYouPageAnalytics.get()
                        .sendCheckTransactionListEvent(thanksPageData.profileCode,
                                thanksPageData.paymentID.toString())
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
        return RouteManager.getIntent(context, ApplinkConst.PURCHASE_ORDER)
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
        const val TICKER_WARNING = "Warning"
        const val TICKER_INFO = "Info"
        const val TICKER_ERROR = "Error"

        const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"
    }
}