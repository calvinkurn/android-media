package com.tokopedia.thankyou_native.presentation.fragment

import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.GyroRecommendationAnalytics
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLOSE_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.OPEN_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.data.mapper.DigitalThankPage
import com.tokopedia.thankyou_native.data.mapper.MarketPlaceThankPage
import com.tokopedia.thankyou_native.data.mapper.PaymentExpired
import com.tokopedia.thankyou_native.data.mapper.PaymentPageMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentStatus
import com.tokopedia.thankyou_native.data.mapper.PaymentStatusMapper
import com.tokopedia.thankyou_native.data.mapper.PaymentVerified
import com.tokopedia.thankyou_native.data.mapper.ThankPageTypeMapper
import com.tokopedia.thankyou_native.di.component.ThankYouPageComponent
import com.tokopedia.thankyou_native.domain.model.ThankPageTopTickerData
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.helper.ThanksPageHelper
import com.tokopedia.thankyou_native.helper.addContainer
import com.tokopedia.thankyou_native.helper.attachTopAdsHeadlinesView
import com.tokopedia.thankyou_native.helper.getTopAdsHeadlinesView
import com.tokopedia.thankyou_native.presentation.activity.ARG_MERCHANT
import com.tokopedia.thankyou_native.presentation.activity.ARG_PAYMENT_ID
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendation
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.helper.DialogHelper
import com.tokopedia.thankyou_native.presentation.helper.OnDialogRedirectListener
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import com.tokopedia.thankyou_native.presentation.views.GyroView
import com.tokopedia.thankyou_native.presentation.views.RegisterMemberShipListener
import com.tokopedia.thankyou_native.presentation.views.TopAdsView
import com.tokopedia.thankyou_native.recommendation.presentation.view.IRecommendationView
import com.tokopedia.thankyou_native.recommendation.presentation.view.MarketPlaceRecommendation
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendation
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.IDigitalRecommendationView
import com.tokopedia.tokomember.TokomemberActivity
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.thank_fragment_success_payment.*
import javax.inject.Inject


abstract class ThankYouBaseFragment : BaseDaggerFragment(), OnDialogRedirectListener,
    RegisterMemberShipListener {

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

    private val topadsHeadlineView by lazy { requireContext().getTopAdsHeadlinesView() }

    private var iDigitalRecommendationView: IDigitalRecommendationView? = null

    private val digitalRecommendationLayout = R.layout.thank_layout_digital_recom

    private val thanksPageDataViewModel: ThanksPageDataViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ThanksPageDataViewModel::class.java)
    }

    private var digitalRecomTrackingQueue: TrackingQueue? = null

    lateinit var thanksPageData: ThanksPageData

    @Inject
    lateinit var userSession: UserSessionInterface

    private var membershipBottomSheetData: BottomSheetContentItem? = null
    private var mTokomemberItemPosition = -1
    private var gyroTokomemberItemSuccess: GyroTokomemberItem? = null
    private var memberShipCardId: String = ""

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
            topadsHeadlineView.getHeadlineAds(
                ThanksPageHelper.getHeadlineAdsParam(0, userSession.userId, TOP_ADS_SRC),
                this::showTopAdsHeadlineView,
                this::hideTopAdsHeadlineView
            )
        }
    }

    private fun getFeatureRecommendationData() {
        thanksPageData.configFlagData?.apply {
            if (isThanksWidgetEnabled && shouldHideFeatureRecom == false)
                thanksPageDataViewModel.checkForGoPayActivation(thanksPageData)
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
                addDigitalRecommendation(pgCategoryIds, DigitalRecommendationPage.PG_THANK_YOU_PAGE)
            }
            is DigitalThankPage -> {
                addDigitalRecommendation(pgCategoryIds, DigitalRecommendationPage.DG_THANK_YOU_PAGE)
                addMarketPlaceRecommendation()
            }
        }
    }

    private fun addMarketPlaceRecommendation() {
        if (::thanksPageData.isInitialized) {

            if (thanksPageData.configFlagData?.shouldHideProductRecom == true) return

            val recomContainer = getRecommendationContainer()
            iRecommendationView = recomContainer?.let { container ->
                val view = getRecommendationView(marketRecommendationPlaceLayout)

                //container 1 to display top ads recommendation view
                container.addContainer(TOP_ADS_HEADLINE_ABOVE_RECOM)

                container.addView(view)

                //container 2 to display top ads recommendation view
                container.addContainer(TOP_ADS_HEADLINE_BELOW_RECOM)

                view.findViewById<MarketPlaceRecommendation>(R.id.marketPlaceRecommendationView)
            }
            iRecommendationView?.loadRecommendation(thanksPageData, this)
        }
    }

    private fun addDigitalRecommendation(
        pgCategoryIds: List<Int> = listOf(),
        pageType: DigitalRecommendationPage
    ) {
        if (::thanksPageData.isInitialized) {

            if (thanksPageData.configFlagData?.shouldHideDigitalRecom == true) return

            val recomContainer = getRecommendationContainer()
            iDigitalRecommendationView = recomContainer?.let { container ->
                val view = getRecommendationView(digitalRecommendationLayout)
                container.addView(view)
                view.findViewById<DigitalRecommendation>(R.id.digitalRecommendationView)
            }

            iDigitalRecommendationView?.loadRecommendation(
                this, pgCategoryIds, pageType
            )
        }
    }

    private fun getRecommendationView(@LayoutRes layout: Int): View {
        return LayoutInflater.from(context).inflate(layout, null, false)
    }

    fun refreshThanksPageData() {
        getLoadingView()?.visible()
        arguments?.let {
            if (it.containsKey(ARG_PAYMENT_ID) && it.containsKey(ARG_MERCHANT)) {
                thanksPageDataViewModel.getThanksPageData(
                    it.getString(ARG_PAYMENT_ID, ""),
                    it.getString(ARG_MERCHANT, "")
                )
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
            gyroTokomemberItemSuccess = it?.gyroMembershipSuccessWidget
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
            when (it) {
                is Success -> {
                    updateLocalizingAddressData(it.data)
                }
                is Fail -> {
                    //do nothing
                }
            }
        })

        thanksPageDataViewModel.topAdsDataLiveData.observe(viewLifecycleOwner) {
            addDataToTopAdsView(it)
        }

        thanksPageDataViewModel.membershipRegisterData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.resultStatus?.code == "200") {
                        openTokomemberBottomsheet()
                    } else {
                        showErrorToasterRegister()
                    }
                }
                is Fail -> {
                    showErrorToasterRegister()
                }
            }
        }
    }

    private fun updateLocalizingAddressData(data: GetDefaultChosenAddressResponse) {
        val errorCode = data.error.code
        if (errorCode == ChooseAddressConstant.ERROR_CODE_EMPTY_LAT_LONG_PARAM || errorCode == ChooseAddressConstant.ERROR_CODE_INVALID_LAT_LONG_PARAM ||
            errorCode == ChooseAddressConstant.ERROR_CODE_FAILED_GET_DISTRICT_DATA || errorCode == ChooseAddressConstant.ERROR_CODE_EMPTY_DISTRICT_DATA
        ) {
            val defaultAddress = ChooseAddressConstant.defaultAddress
            context?.let {
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    it,
                    defaultAddress.address_id,
                    defaultAddress.city_id,
                    defaultAddress.district_id,
                    defaultAddress.lat,
                    defaultAddress.long,
                    defaultAddress.label,
                    defaultAddress.postal_code,
                    data.tokonow.shopId.toString(),
                    data.tokonow.warehouseId.toString(),
                    TokonowWarehouseMapper.mapWarehousesResponseToLocal(data.tokonow.warehouses),
                    data.tokonow.serviceType
                )
            }
        } else {
            val addressData = data.data
            context?.let {
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    it,
                    addressData.addressId.toString(),
                    addressData.cityId.toString(),
                    addressData.districtId.toString(),
                    addressData.latitude,
                    addressData.longitude,
                    "${addressData.addressName} ${addressData.receiverName}",
                    addressData.postalCode,
                    data.tokonow.shopId.toString(),
                    data.tokonow.warehouseId.toString(),
                    TokonowWarehouseMapper.mapWarehousesResponseToLocal(data.tokonow.warehouses),
                    data.tokonow.serviceType
                )
            }
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
                getFeatureListingContainer()?.listener = this
                getFeatureListingContainer()?.addData(
                    gyroRecommendation, thanksPageData,
                    gyroRecommendationAnalytics.get()
                )
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
            thankYouPageAnalytics.get().sendOnHowtoPayClickEvent(
                thanksPageData.profileCode,
                thanksPageData.paymentID
            )
        }
    }

    fun showPaymentStatusDialog(
        isTimerFinished: Boolean,
        thanksPageData: ThanksPageData
    ) {
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
        if (thanksPageData.configFlagData?.shouldHideHomeButton == false) {
            homeButton?.let {
                thanksPageData.customDataMessage?.let {
                    it.titleHomeButton?.apply {
                        if (isNotBlank())
                            homeButton.text = this
                    }
                }

                homeButton.setOnClickListener {
                    thanksPageData.customDataAppLink?.let {
                        if (it.home.isNullOrBlank())
                            gotoHomePage()
                        else
                            launchApplink(it.home)
                    } ?: run {
                        gotoHomePage()
                    }
                }
            }
        } else {
            homeButton?.gone()
        }
    }

    fun openInvoiceDetail(thanksPageData: ThanksPageData) {
        InvoiceFragment.openInvoiceBottomSheet(activity, thanksPageData)
        thankYouPageAnalytics.get().sendLihatDetailClickEvent(
            thanksPageData.profileCode,
            PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
            thanksPageData.paymentID
        )

        if (activity is ThankYouPageActivity) {
            (activity as ThankYouPageActivity).cancelGratifDialog()
        }
    }

    override fun gotoHomePage() {
        RouteManager.route(context, ApplinkConst.HOME, "")
        thankYouPageAnalytics.get().sendBelanjaLagiClickEvent(
            thanksPageData.profileCode,
            PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
            thanksPageData.paymentID
        )
        activity?.finish()
    }

    private fun openWebLink(urlStr: String?) {
        urlStr?.let {
            val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlStr
            activity?.apply {
                RouteManager.route(this, webViewAppLink)
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
        thankYouPageAnalytics.get().sendBelanjaLagiClickEvent(
            thanksPageData.profileCode,
            PaymentPageMapper.getPaymentPageType(thanksPageData.pageType),
            thanksPageData.paymentID
        )
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
                .sendCheckTransactionListEvent(
                    thanksPageData.profileCode,
                    thanksPageData.paymentID
                )
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
                    .sendCheckTransactionListEvent(
                        thanksPageData.profileCode,
                        thanksPageData.paymentID
                    )
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
        when (requestCode) {
            REQUEST_CODE_TOKOMEMBER -> context?.let {
                if (membershipBottomSheetData?.membershipType == OPEN_MEMBERSHIP) {
                    gyroTokomemberItemSuccess?.successRegister = true
                }
                getFeatureListingContainer()?.updateTokoMemberWidget(
                    mTokomemberItemPosition,
                    gyroTokomemberItemSuccess
                )
            }
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

    private fun hideTopAdsHeadlineView() {
        topadsHeadlineView.hideShimmerView()
        topadsHeadlineView.hide()
    }

    private fun showTopAdsHeadlineView(cpmModel: CpmModel) {
        topadsHeadlineView.show()
        topadsHeadlineView.hideShimmerView()
        topadsHeadlineView.displayAds(cpmModel)

        getRecommendationContainer()?.attachTopAdsHeadlinesView(
            cpmModel.data?.get(0)?.cpm?.position == 1,
            topadsHeadlineView
        )
    }

    override fun registerMembership(
        bottomSheetContentItem: BottomSheetContentItem,
        memberShipCardId: String,
        position: Int
    ) {
        mTokomemberItemPosition = position
        this.memberShipCardId = memberShipCardId
        this.membershipBottomSheetData = bottomSheetContentItem
        if (bottomSheetContentItem.membershipType == OPEN_MEMBERSHIP) {
            thanksPageDataViewModel.registerTokomember(memberShipCardId)
        } else if (bottomSheetContentItem.membershipType == CLOSE_MEMBERSHIP) {
            openTokomemberBottomsheet()
        }
    }

    private fun openTokomemberBottomsheet() {
        view?.context?.apply {
            startActivityForResult(
                TokomemberActivity.getIntent(this, membershipBottomSheetData),
                REQUEST_CODE_TOKOMEMBER
            )
        }
    }

    private fun showErrorToasterRegister() {
        Toaster.build(
            requireView(),
            getString(R.string.thank_tokomember_register_fail),
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            getString(R.string.thank_coba_lagi)
        ) {
            thanksPageDataViewModel.registerTokomember(memberShipCardId)
        }.show()
    }

    fun setUpIllustration() {
        thanksPageData.customDataOther?.let {
            it.customIllustration?.let { img ->
                if (img.isNotEmpty()) {
                    loadGlideImage(img)
                } else {
                    showCharacterAnimation()
                }
            } ?: run {
                showCharacterAnimation()
            }
        }
    }

    private fun loadGlideImage(imageUrl: String) {
        setIllustrationVisibility(true)
        context?.let {
            try {
                if (ivIllustrationView?.context?.isValidGlideContext() == true) {
                    GlideApp.with(it)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                showCharacterAnimation()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        }).into(ivIllustrationView)
                }
            } catch (e: Throwable) {
            }
        }
    }

    private fun showCharacterAnimation() {
        setIllustrationVisibility(false)
        context?.let {
            val lottieTask =
                LottieCompositionFactory.fromAsset(context, CHARACTER_LOADER_JSON_ZIP_FILE)
            lottieTask?.addListener { result: LottieComposition? ->
                result?.let {
                    lottieAnimationView?.setComposition(result)
                    lottieAnimationView?.playAnimation()
                }
            }
        }
    }

    private fun setIllustrationVisibility(showImage: Boolean = false) {
        if (showImage) {
            lottieAnimationView.gone()
            ivIllustrationView.visible()
        } else {
            lottieAnimationView.visible()
            ivIllustrationView.gone()
        }
    }

    companion object {
        const val TICKER_WARNING = "Warning"
        const val TICKER_INFO = "Info"
        const val TICKER_ERROR = "Error"

        const val ARG_THANK_PAGE_DATA = "arg_thank_page_data"

        /* Constant for toads headlines widget*/
        const val TOP_ADS_SRC = "thank_you_page"
        const val TOP_ADS_HEADLINE_ABOVE_RECOM = "variant1"
        const val TOP_ADS_HEADLINE_BELOW_RECOM = "variant2"
        const val REQUEST_CODE_TOKOMEMBER = 7

        const val CARD_NUMBER_MASKING_UNICODE = "\u25CF\u25CF\u25CF\u25CF "
        const val LAST_NUMBERS = 4
    }
}