package com.tokopedia.sellerhome.settings.view.fragment.old

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller.menu.common.analytics.*
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.base.*
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.StatusbarHelper
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.sellerhome.settings.analytics.SettingPerformanceTracker
import com.tokopedia.sellerhome.settings.analytics.SettingShopOperationalTracker
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.adapter.OtherMenuAdapter
import com.tokopedia.sellerhome.settings.view.bottomsheet.SettingsFreeShippingBottomSheet
import com.tokopedia.sellerhome.settings.view.viewholder.old.OtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.old.OtherMenuViewModel
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class OtherMenuFragment: BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(),
    OtherMenuViewHolder.Listener, OtherMenuAdapter.Listener, StatusBarCallback,
    SettingTrackingListener, SellerHomeFragmentListener {

    companion object {
        private const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"

        private const val START_OFFSET = 56 // Pixels when scrolled past toolbar height
        private const val HEIGHT_OFFSET = 24 // Pixels of status bar height, the view that could be affected by scroll change
        private const val MAXIMUM_ALPHA = 255f
        private const val ALPHA_CHANGE_THRESHOLD = 150

        const val SCROLLVIEW_INITIAL_POSITION = 0

        const val TOPADS_BOTTOMSHEET_TAG = "topads_bottomsheet"

        const val GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY"
        const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"

        const val SHOP_BADGE = "shop badge"
        const val SHOP_FOLLOWERS = "shop followers"
        const val SHOP_INFO = "shop info"
        const val OPERATIONAL_HOUR = "operational hour"
        const val SALDO_BALANCE = "saldo balance"
        const val TOPADS_BALANCE = "topads balance"
        const val TOPADS_AUTO_TOPUP = "topads auto topup"
        const val FREE_SHIPPING = "free shipping"

        const val SHOW_FULL_SCREEN_BOTTOM_SHEET = "FullScreenBottomSheet"

        @JvmStatic
        fun createInstance(): OtherMenuFragment = OtherMenuFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl
    @Inject
    lateinit var freeShippingTracker: SettingFreeShippingTracker
    @Inject
    lateinit var shopOperationalTracker: SettingShopOperationalTracker

    @Inject lateinit var settingPerformanceTracker: SettingPerformanceTracker
    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    private var otherMenuViewHolder: OtherMenuViewHolder? = null

    private var multipleErrorSnackbar: Snackbar? = null

    private var startToTransitionOffset = 0
    private var statusInfoTransitionOffset = 0

    private var isInitialStatusBar = false
    private var isDefaultDarkStatusBar = true

    private var canShowErrorToaster = true

    @FragmentType
    private var currentFragmentType: Int = FragmentType.OTHER

    private val otherMenuViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OtherMenuViewModel::class.java)
    }

    private val statusBarHeight by lazy {
        context?.let { StatusbarHelper.getStatusBarHeight(it) }
    }

    private val topAdsBottomSheet by lazy {
        BottomSheetUnify().apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    private val topAdsBottomSheetView by lazy {
        context?.let {
            View.inflate(it, R.layout.setting_topads_bottomsheet_layout, null)
        }
    }

    private val otherMenuAdapter by lazy {
        adapter as? OtherMenuAdapter
    }

    private val kreditTopadsClickedBundle by lazy {
        Bundle().also {
            it.putBoolean(SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
        }
    }

    private var statusHeaderImage: AppCompatImageView? = null
    private var statusIconImage: AppCompatImageView? = null
    private var whiteBackgroundView: View? = null
    private var recyclerView: RecyclerView? = null
    private var statusBarBackgroundView: View? = null
    private var scrollView: NestedScrollView? = null

    override fun onResume() {
        super.onResume()
        otherMenuViewModel.getAllOtherMenuData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SellerHomeActivity)?.attachCallback(this)
        otherMenuViewModel.setErrorStateMapDefaultValue()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOffset()
        initView(view)
        setupView(view)
        observeLiveData()
        observeShopPeriod()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory(this, userSession = userSession)

    override fun createAdapterInstance(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> {
        return OtherMenuAdapter(context, this, adapterTypeFactory)
    }

    override fun onItemClicked(settingUiModel: SettingUiModel) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun onShopInfoClicked() {
        RouteManager.route(context, ApplinkConst.SHOP, userSession.shopId)
    }

    override fun onShopBadgeClicked() {
        goToReputationHistory()
    }

    override fun onFollowersCountClicked() {
        goToShopFavouriteList()
    }

    override fun onSaldoClicked() {
        context?.let {
            if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
                RouteManager.route(it, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
            else {
                val intent =
                    RouteManager.getIntent(
                        it,
                        ApplinkConstInternalGlobal.WEBVIEW,
                        ApplinkConst.WebViewUrl.SALDO_DETAIL)
                it.startActivity(intent)
            }
        }
    }

    override fun onKreditTopadsClicked() {
        context?.let {
            val bottomSheet = childFragmentManager.findFragmentByTag(TOPADS_BOTTOMSHEET_TAG)
            if (bottomSheet is BottomSheetUnify) {
                bottomSheet.dismiss()
                RouteManager.route(it, ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP)
            } else {
                RouteManager.route(
                    it,
                    kreditTopadsClickedBundle,
                    ApplinkConst.SellerApp.TOPADS_CREDIT
                )
            }
        }
    }

    override fun onRefreshShopInfo() {
        otherMenuViewModel.getAllOtherMenuData()
        otherMenuViewModel.getShopPeriodType()
    }

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        if (settingShopInfoImpressionTrackable is StatisticMenuItemUiModel) {
            sendEventImpressionStatisticMenuItem(userSession.userId)
        } else {
            settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setStatusBar() {
        (activity as? Activity)?.run {
            if (isInitialStatusBar && !isDefaultDarkStatusBar) {
                requestStatusBarLight()
            } else {
                requestStatusBarDark()
            }
        }
    }

    override fun onStatusBarNeedDarkColor(isDefaultDark: Boolean) {
        isDefaultDarkStatusBar = isDefaultDark
        setStatusBarStateInitialIsLight(!isDefaultDark)
        if (otherMenuViewModel.isStatusBarInitialState.value == false && !isDefaultDark) {
            setStatusBarStateInitialIsLight(isDefaultDark)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (currentFragmentType == FragmentType.OTHER) {
                setStatusBar()
            }
        }
    }

    override fun setCurrentFragmentType(fragmentType: Int) {
        currentFragmentType = fragmentType
        if (fragmentType != FragmentType.OTHER) {
            multipleErrorSnackbar?.dismiss()
        }
    }

    override fun onTopAdsTooltipClicked(isTopAdsActive: Boolean) {
        val bottomSheetChildView = setupBottomSheetLayout(isTopAdsActive)
        bottomSheetChildView?.run {
            with(topAdsBottomSheet) {
                setChild(this@run)
                show(this@OtherMenuFragment.childFragmentManager, TOPADS_BOTTOMSHEET_TAG)
            }
        }
    }

    override fun onFreeShippingClicked() {
        val freeShippingBottomSheet = SettingsFreeShippingBottomSheet.createInstance()
        if (isActivityResumed()) {
            freeShippingBottomSheet.show(childFragmentManager)
        }
    }

    override fun onRefreshData() {
        otherMenuViewModel.onReloadErrorData()
    }

    override fun onShopBadgeRefresh() {
        otherMenuViewModel.getShopBadge()
    }

    override fun onShopTotalFollowersRefresh() {
        otherMenuViewModel.getShopTotalFollowers()
    }

    override fun onShopBadgeFollowersRefresh() {
        otherMenuViewModel.getShopBadgeAndFollowers()
    }

    override fun onUserInfoRefresh() {
        otherMenuViewModel.getUserShopInfo()
    }

    override fun onOperationalHourRefresh() {
        otherMenuViewModel.getShopOperational()
    }

    override fun onSaldoBalanceRefresh() {
        otherMenuViewModel.getBalanceInfo()
    }

    override fun onKreditTopAdsRefresh() {
        otherMenuViewModel.getKreditTopAds()
    }

    override fun onScrollToTop() {
        scrollView?.post {
            scrollView?.smoothScrollTo(SCROLLVIEW_INITIAL_POSITION, SCROLLVIEW_INITIAL_POSITION)
        }
    }

    private fun setupBottomSheetLayout(isTopAdsActive: Boolean) : View? {
        val bottomSheetInfix: String
        val bottomSheetDescription: String
        if (isTopAdsActive) {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_active)
            bottomSheetDescription = resources.getString(R.string.setting_topads_description_active)
        } else {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_inactive)
            bottomSheetDescription = resources.getString(R.string.setting_topads_description_inactive)
        }
        val bottomSheetTitle = resources.getString(R.string.setting_topads_status, bottomSheetInfix)
        return topAdsBottomSheetView?.apply {
            findViewById<Typography>(R.id.topAdsBottomSheetTitle).text = bottomSheetTitle
            findViewById<TextView>(R.id.topAdsBottomSheetDescription).text = bottomSheetDescription
            findViewById<UnifyButton>(R.id.topAdsNextButton).setOnClickListener{
                onKreditTopadsClicked()
            }
        }
    }

    private fun setStatusBarStateInitialIsLight(isLight: Boolean) {
        isInitialStatusBar = isLight
    }

    private fun observeLiveData() {
        observeIsAllError()
        observeMultipleErrorToaster()
        observeShopBadge()
        observeShopTotalFollowers()
        observeShopBadgeFollowersLoading()
        observeShopBadgeFollowersError()
        observeShopStatus()
        observeShopOperationalHour()
        observeSaldoBalance()
        observeKreditTopAds()
        observeFreeShippingStatus()
        observeIsTopAdsAutoTopup()
        observeToasterAlreadyShown()
    }

    private fun observeFreeShippingStatus() {
        observe(otherMenuViewModel.isFreeShippingActive) { freeShippingActive ->
            if(freeShippingActive) {
                otherMenuViewHolder?.setupFreeShippingLayout()
            } else {
                otherMenuViewHolder?.hideFreeShippingLayout()
            }
        }
    }

    private fun observeShopPeriod() {
        observe(otherMenuViewModel.shopPeriodType) {
            when (it) {
                is Success -> {
                    setTrackerPerformanceMenu(it.data.isNewSeller)
                }
                is Fail -> {}
            }
        }
        otherMenuViewModel.getShopPeriodType()
    }

    private fun setTrackerPerformanceMenu(isNewSeller: Boolean) {
        val shopPerformanceData = adapter.list.filterIsInstance<MenuItemUiModel>().find {
            it.onClickApplink == ApplinkConstInternalMarketplace.SHOP_PERFORMANCE
        }
        if (shopPerformanceData != null) {
            settingPerformanceTracker.impressItemEntryPointPerformance(isNewSeller)
        }
        shopPerformanceData?.clickSendTracker = {
            settingPerformanceTracker.clickItemEntryPointPerformance(isNewSeller)
        }
    }

    private fun observeIsAllError() {
        otherMenuViewModel.shouldShowAllError.observe(viewLifecycleOwner) {
            otherMenuViewHolder?.setAllErrorLocalLoad(it)
        }
    }

    private fun observeMultipleErrorToaster() {
        otherMenuViewModel.shouldShowMultipleErrorToaster.observe(viewLifecycleOwner) { shouldShowError ->
            if (shouldShowError) {
                showMultipleErrorToaster()
            } else {
                multipleErrorSnackbar?.dismiss()
            }
        }
    }

    private fun observeShopBadgeFollowersLoading() {
        otherMenuViewModel.shopBadgeFollowersShimmerLiveData.observe(viewLifecycleOwner) {
            otherMenuViewHolder?.setBadgeFollowersLoading(it)
        }
    }

    private fun observeShopBadgeFollowersError() {
        otherMenuViewModel.shopBadgeFollowersErrorLiveData.observe(viewLifecycleOwner) {
            otherMenuViewHolder?.setBadgeFollowersError(it)
        }
    }

    private fun observeShopBadge() {
        otherMenuViewModel.shopBadgeLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is SettingResponseState.SettingSuccess -> {
                    otherMenuViewHolder?.setShopBadge(it.data)
                }
                is SettingResponseState.SettingLoading -> {
                    otherMenuViewHolder?.setShopBadgeLoading()
                }
                is SettingResponseState.SettingError -> {
                    showErrorToaster(it.throwable) {
                        onShopBadgeRefresh()
                    }
                    otherMenuViewHolder?.setShopBadgeError()
                    SellerHomeErrorHandler.logException(
                            it.throwable,
                            context?.getString(R.string.setting_header_error_message, SHOP_BADGE).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeShopTotalFollowers() {
        otherMenuViewModel.shopTotalFollowersLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is SettingResponseState.SettingSuccess -> {
                    otherMenuViewHolder?.setShopTotalFollowers(it.data)
                }
                is SettingResponseState.SettingLoading -> {
                    otherMenuViewHolder?.setShopTotalFollowersLoading()
                }
                is SettingResponseState.SettingError -> {
                    showErrorToaster(it.throwable) {
                        onShopTotalFollowersRefresh()
                    }
                    otherMenuViewHolder?.setShopTotalFollowersError()
                    SellerHomeErrorHandler.logException(
                            it.throwable,
                            context?.getString(R.string.setting_header_error_message, SHOP_FOLLOWERS).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeShopStatus() {
        otherMenuViewModel.userShopInfoLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is SettingResponseState.SettingSuccess -> {
                    otherMenuViewHolder?.setShopStatusType(it.data)
                }
                is SettingResponseState.SettingLoading -> {
                    otherMenuViewHolder?.setShopStatusLoading()
                }
                is SettingResponseState.SettingError -> {
                    showErrorToaster(it.throwable) {
                        onRefreshShopInfo()
                    }
                    otherMenuViewHolder?.setShopStatusError()
                    SellerHomeErrorHandler.logException(
                            it.throwable,
                            context?.getString(R.string.setting_header_error_message, SHOP_INFO).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeShopOperationalHour() {
        otherMenuViewModel.shopOperationalLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is SettingResponseState.SettingSuccess -> {
                    otherMenuViewHolder?.showOperationalHourLayout(it.data)
                }
                is SettingResponseState.SettingLoading -> {
                    otherMenuViewHolder?.showOperationalHourLayoutLoading()
                }
                is SettingResponseState.SettingError -> {
                    showErrorToaster(it.throwable) {
                        onOperationalHourRefresh()
                    }
                    otherMenuViewHolder?.showOperationalHourLayoutError()
                    SellerHomeErrorHandler.logException(
                            it.throwable,
                            context?.getString(R.string.setting_header_error_message, OPERATIONAL_HOUR).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeSaldoBalance() {
        otherMenuViewModel.balanceInfoLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is SettingResponseState.SettingSuccess -> {
                    otherMenuViewHolder?.setSaldoBalance(it.data)
                }
                is SettingResponseState.SettingLoading -> {
                    otherMenuViewHolder?.setSaldoBalanceLoading()
                }
                is SettingResponseState.SettingError -> {
                    showErrorToaster(it.throwable) {
                        onSaldoBalanceRefresh()
                    }
                    otherMenuViewHolder?.setSaldoBalanceError()
                    SellerHomeErrorHandler.logException(
                            it.throwable,
                            context?.getString(R.string.setting_header_error_message, SALDO_BALANCE).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeKreditTopAds() {
        otherMenuViewModel.kreditTopAdsLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is SettingResponseState.SettingSuccess -> {
                    otherMenuViewHolder?.setKreditTopadsBalance(it.data)
                }
                is SettingResponseState.SettingLoading -> {
                    otherMenuViewHolder?.setKreditTopadsBalanceLoading()
                }
                is SettingResponseState.SettingError -> {
                    showErrorToaster(it.throwable) {
                        onKreditTopAdsRefresh()
                    }
                    otherMenuViewHolder?.setKreditTopadsBalanceError()
                    SellerHomeErrorHandler.logException(
                            it.throwable,
                            context?.getString(R.string.setting_header_error_message, TOPADS_BALANCE).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeIsTopAdsAutoTopup() {
        otherMenuViewModel.isTopAdsAutoTopupLiveData.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    otherMenuViewHolder?.setupKreditTopadsBalanceTooltip(result.data)
                }
                is Fail -> {
                    showErrorToaster(result.throwable)
                    otherMenuViewHolder?.setupKreditTopadsBalanceTooltip(null)
                    SellerHomeErrorHandler.logException(
                            result.throwable,
                            context?.getString(R.string.setting_header_error_message, TOPADS_AUTO_TOPUP).orEmpty()
                    )
                }
            }
        }
    }

    private fun observeToasterAlreadyShown() {
        otherMenuViewModel.isToasterAlreadyShown.observe(viewLifecycleOwner) { isToasterAlreadyShown ->
            canShowErrorToaster = !isToasterAlreadyShown
        }
    }

    private fun populateAdapterData() {
        otherMenuAdapter?.populateAdapterData()
    }

    private fun showErrorToaster(throwable: Throwable, onRetryAction: () -> Unit = {}) {
        otherMenuViewModel.onCheckDelayErrorResponseTrigger()
        val canShowToaster = currentFragmentType == FragmentType.OTHER && canShowErrorToaster
        if (canShowToaster) {
            val errorMessage = context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            } ?: resources.getString(R.string.setting_toaster_error_message)
            view?.showToasterError(errorMessage, onRetryAction)
        }
    }

    private fun showMultipleErrorToaster() {
        multipleErrorSnackbar =
                view?.run {
                    Toaster.build(
                            this,
                            context?.getString(R.string.setting_header_multiple_error_message).orEmpty(),
                            Snackbar.LENGTH_INDEFINITE,
                            Toaster.TYPE_NORMAL,
                            context?.getString(R.string.setting_toaster_error_retry).orEmpty())
                    {
                        otherMenuViewModel.reloadErrorData()
                        otherMenuViewModel.onReloadErrorData()
                    }
                }
        multipleErrorSnackbar?.show()
    }

    private fun View.showToasterError(errorMessage: String, onRetryAction: () -> Unit) {
        Toaster.build(this,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                resources.getString(R.string.setting_toaster_error_retry)
        ) {
            onRetryAction()
        }.show()
    }

    private fun setupView(view: View) {
        statusBarBackgroundView?.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, statusBarHeight ?: HEIGHT_OFFSET)
        populateAdapterData()
        recyclerView?.layoutManager = LinearLayoutManager(context)
        context?.let {
            otherMenuViewHolder = OtherMenuViewHolder(
                itemView = view,
                context = it,
                listener = this,
                trackingListener = this,
                freeShippingTracker = freeShippingTracker,
                shopOperationalTracker = shopOperationalTracker,
                sellerMenuTracker = sellerMenuTracker,
                userSession = userSession
            )
        }
        otherMenuViewHolder?.setupInitialLayout()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDefaultDarkStatusBar) {
                activity?.requestStatusBarDark()
            } else {
                activity?.requestStatusBarLight()
            }
        }
        observeRecyclerViewScrollListener()
    }

    private fun initView(view: View) {
        with(view) {
            statusHeaderImage = findViewById(R.id.iv_sah_other_status_header)
            statusIconImage = findViewById(R.id.iv_sah_other_status_icon)
            whiteBackgroundView = findViewById(R.id.bg_white_other_menu)
            statusBarBackgroundView = findViewById(R.id.view_sah_other_status_bar_background)
            recyclerView = findViewById(R.id.recycler_view)
            scrollView = findViewById(R.id.sv_sah_other_menu)
        }
    }

    private fun setupOffset() {
        activity?.theme?.let {
            val tv = TypedValue()
            if (it.resolveAttribute(R.attr.actionBarSize, tv, true)) {
                startToTransitionOffset = START_OFFSET
            }
        }
        statusInfoTransitionOffset = statusBarHeight ?: HEIGHT_OFFSET
    }

    private fun observeRecyclerViewScrollListener() {
        scrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { scrollView, _, _, _, _ ->
            calculateStatusBarView(scrollView.scrollY)
        })
    }

    private fun calculateStatusBarView(offset: Int) {
        val endToTransitionOffset = startToTransitionOffset + statusInfoTransitionOffset
        val maxTransitionOffset = endToTransitionOffset - startToTransitionOffset

        //Offset Alpha is not actually needed for changing the status bar color (only needed the offset),
        //but we will preserve the variable in case the stakeholders need to change the status bar alpha according to the scroll position
        val offsetAlpha = (MAXIMUM_ALPHA /maxTransitionOffset).times(offset - startToTransitionOffset)
        if (offsetAlpha >= ALPHA_CHANGE_THRESHOLD) {
            if (isInitialStatusBar) {
                setDarkStatusBar()
                otherMenuViewModel.setIsStatusBarInitialState(false)
            }
            statusHeaderImage?.gone()
            statusIconImage?.gone()
            whiteBackgroundView?.gone()
        } else {
            if (!isInitialStatusBar) {
                setLightStatusBar()
                otherMenuViewModel.setIsStatusBarInitialState(true)
            }
            statusHeaderImage?.visible()
            statusIconImage?.visible()
            whiteBackgroundView?.visible()
        }
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDefaultDarkStatusBar){
                activity?.requestStatusBarLight()
            }
            setStatusBarStateInitialIsLight(true)
            statusBarBackgroundView?.hide()
        }
    }

    private fun setDarkStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarStateInitialIsLight(false)
            activity?.requestStatusBarDark()
            statusBarBackgroundView?.show()
        }
    }

    private fun goToReputationHistory() {
        val reputationHistoryIntent = RouteManager.getIntent(context, ApplinkConst.REPUTATION).apply {
            putExtra(GO_TO_REPUTATION_HISTORY, true)
        }
        startActivity(reputationHistoryIntent)
    }

    private fun goToShopFavouriteList() {
        val shopFavouriteListIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST).apply {
            putExtra(EXTRA_SHOP_ID, userSession.shopId)
        }
        startActivity(shopFavouriteListIntent)
    }

    override fun goToPrintingPage() {
        val url = "${TokopediaUrl.getInstance().WEB}${SellerBaseUrl.PRINTING}"
        val applink = String.format(APPLINK_FORMAT_ALLOW_OVERRIDE, ApplinkConst.WEBVIEW, false, url)
        RouteManager.getIntent(context, applink)?.let {
            context?.startActivity(it)
        }
    }

    override fun goToSettings() {
        context?.let {
            startActivity(Intent(it, MenuSettingActivity::class.java))
        }
    }

    private fun isActivityResumed(): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }

}