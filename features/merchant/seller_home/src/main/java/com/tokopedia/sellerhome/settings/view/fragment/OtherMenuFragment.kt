package com.tokopedia.sellerhome.settings.view.fragment

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendEventImpressionStatisticMenuItem
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.StatisticMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.*
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.StatusbarHelper
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.sellerhome.settings.analytics.SettingShopOperationalTracker
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.bottomsheet.SettingsFreeShippingBottomSheet
import com.tokopedia.sellerhome.settings.view.viewholder.OtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherMenuViewModel
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_other_menu.*
import javax.inject.Inject

class OtherMenuFragment: BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(), OtherMenuViewHolder.Listener, StatusBarCallback, SettingTrackingListener {

    companion object {
        private const val APPLINK_FORMAT = "%s?url=%s%s"

        private const val START_OFFSET = 56 // Pixels when scrolled past toolbar height
        private const val HEIGHT_OFFSET = 24 // Pixels of status bar height, the view that could be affected by scroll change
        private const val MAXIMUM_ALPHA = 255f
        private const val ALPHA_CHANGE_THRESHOLD = 150

        private const val TOPADS_BOTTOMSHEET_TAG = "topads_bottomsheet"

        private const val GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY"
        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"

        private const val ERROR_GET_SETTING_SHOP_INFO = "Error when get shop info in other setting."

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
    lateinit var sellerHomeConfig: SellerHomeRemoteConfig
    @Inject
    lateinit var freeShippingTracker: SettingFreeShippingTracker
    @Inject
    lateinit var shopOperationalTracker: SettingShopOperationalTracker

    private var otherMenuViewHolder: OtherMenuViewHolder? = null

    private var startToTransitionOffset = 0
    private var statusInfoTransitionOffset = 0

    private var isInitialStatusBar = false
    private var isDefaultDarkStatusBar = true

    private var canShowErrorToaster = true

    private var thematicIllustration: AppCompatImageView? = null

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

    override fun onResume() {
        super.onResume()
        getAllShopInfoData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SellerHomeActivity)?.attachCallback(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOffset()
        setupView(view)
        observeLiveData()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory(this)

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
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            context?.startActivity(intent)
        }
    }

    override fun onKreditTopadsClicked() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TOPADS_BOTTOMSHEET_TAG)
        if (bottomSheet is BottomSheetUnify) {
            bottomSheet.dismiss()
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP)
        } else {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_CREDIT)
        }
    }

    override fun onRefreshShopInfo() {
        showAllLoadingShimmering()
        otherMenuViewModel.getAllSettingShopInfo()
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
        with(otherMenuViewModel) {
            settingShopInfoLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> {
                        showSettingShopInfoState(result.data)
                        otherMenuViewModel.getFreeShippingStatus()
                        otherMenuViewModel.getShopOperational()
                    }
                    is Fail -> {
                        SellerHomeErrorHandler.logExceptionToCrashlytics(result.throwable, ERROR_GET_SETTING_SHOP_INFO)
                        showSettingShopInfoState(SettingResponseState.SettingError)
                    }
                }
            })
            isToasterAlreadyShown.observe(viewLifecycleOwner, Observer { isToasterAlreadyShown ->
                canShowErrorToaster = !isToasterAlreadyShown
            })
        }
        observeFreeShippingStatus()
        observeShopOperationalHour()
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

    private fun observeShopOperationalHour() {
        otherMenuViewModel.shopOperational.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> otherMenuViewHolder?.showOperationalHourLayout(it.data)
                is Fail -> otherMenuViewHolder?.onErrorGetSettingShopInfoData()
            }
        })
    }

    private fun populateAdapterData() {
        val settingList = mutableListOf(
                SettingTitleUiModel(resources.getString(R.string.setting_menu_improve_sales)),
                StatisticMenuItemUiModel(
                        title = resources.getString(R.string.setting_menu_statistic),
                        clickApplink = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD,
                        iconUnify = IconUnify.GRAPH),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_ads_and_shop_promotion),
                        null,
                        ApplinkConstInternalSellerapp.CENTRALIZED_PROMO,
                        eventActionSuffix = SettingTrackingConstant.SHOP_ADS_AND_PROMOTION,
                        iconUnify = IconUnify.PROMO_ADS),
                SettingTitleUiModel(resources.getString(R.string.setting_menu_buyer_info)),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_discussion),
                        null,
                        ApplinkConst.TALK,
                        eventActionSuffix = SettingTrackingConstant.DISCUSSION,
                        iconUnify = IconUnify.DISCUSSION),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_review),
                        null,
                        ApplinkConst.REPUTATION,
                        eventActionSuffix = SettingTrackingConstant.REVIEW,
                        iconUnify = IconUnify.STAR),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_complaint),
                        null,
                        null,
                        eventActionSuffix = SettingTrackingConstant.COMPLAINT,
                        iconUnify = IconUnify.PRODUCT_INFO
                ) {
                    val applink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, SellerBaseUrl.HOSTNAME, SellerBaseUrl.RESO_INBOX_SELLER)
                    val intent = RouteManager.getIntent(context, applink)
                    context?.startActivity(intent)
                },
                DividerUiModel(),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_finance_service),
                        null,
                        eventActionSuffix = SettingTrackingConstant.FINANCIAL_SERVICE,
                        iconUnify = IconUnify.FINANCE
                ) {
                    RouteManager.route(context, ApplinkConst.LAYANAN_FINANSIAL)
                },
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_seller_education_center),
                        null,
                        eventActionSuffix = SettingTrackingConstant.SELLER_CENTER,
                        iconUnify = IconUnify.SHOP_INFO
                ) {
                    val applink = String.format(APPLINK_FORMAT, ApplinkConst.WEBVIEW, SellerBaseUrl.SELLER_HOSTNAME, SellerBaseUrl.SELLER_EDU)
                    val intent = RouteManager.getIntent(context, applink)
                    context?.startActivity(intent)
                },
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_tokopedia_care),
                        null,
                        ApplinkConst.CONTACT_US_NATIVE,
                        eventActionSuffix = SettingTrackingConstant.TOKOPEDIA_CARE,
                        iconUnify = IconUnify.CALL_CENTER),
                DividerUiModel(DividerType.THIN_PARTIAL),
                MenuItemUiModel(
                        resources.getString(R.string.setting_menu_setting),
                        null,
                        null,
                        eventActionSuffix = SettingTrackingConstant.SETTINGS,
                        iconUnify = IconUnify.SETTING
                ) {
                    startActivity(Intent(context, MenuSettingActivity::class.java))
                }
        )
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)
    }

    private fun getAllShopInfoData() {
        showAllLoadingShimmering()
        otherMenuViewModel.getAllSettingShopInfo()
    }

    private fun showAllLoadingShimmering() {
        showSettingShopInfoState(SettingResponseState.SettingLoading)
    }

    private fun showSettingShopInfoState(settingResponseState: SettingResponseState) {
        when(settingResponseState) {
            is SettingSuccess -> {
                if (settingResponseState is SettingShopInfoUiModel) {
                    otherMenuViewHolder?.onSuccessGetSettingShopInfoData(settingResponseState)
                }
            }
            is SettingResponseState.SettingLoading -> otherMenuViewHolder?.onLoadingGetSettingShopInfoData()
            is SettingResponseState.SettingError -> {
                val canShowToaster = currentFragmentType == FragmentType.OTHER && canShowErrorToaster
                if (canShowToaster) {
                    view?.showToasterError(resources.getString(R.string.setting_toaster_error_message))
                }
                otherMenuViewHolder?.onErrorGetSettingShopInfoData()
            }
        }
    }

    private fun retryFetchAfterError() {
        showAllLoadingShimmering()
        otherMenuViewModel.getAllSettingShopInfo(isToasterRetry = true)
    }

    private fun View.showToasterError(errorMessage: String) {
        Toaster.build(this,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                resources.getString(R.string.setting_toaster_error_retry)
        ) {
            retryFetchAfterError()
        }.show()
    }

    private fun setupView(view: View) {
        view.run {
            statusBarBackground?.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, statusBarHeight ?: HEIGHT_OFFSET)
        }
        thematicIllustration = view.findViewById(R.id.iv_other_menu_thematic)
        populateAdapterData()
        recycler_view.layoutManager = LinearLayoutManager(context)
        context?.let {
            otherMenuViewHolder = OtherMenuViewHolder(
                itemView = view,
                context = it,
                listener = this,
                trackingListener = this,
                freeShippingTracker = freeShippingTracker,
                shopOperationalTracker = shopOperationalTracker,
                userSession = userSession
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDefaultDarkStatusBar) {
                activity?.requestStatusBarDark()
            } else {
                activity?.requestStatusBarLight()
            }
        }
        observeRecyclerViewScrollListener()
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
        this.otherMenuScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { scrollView, _, _, _, _ ->
            calculateSearchBarView(scrollView.scrollY)
        })
    }

    private fun calculateSearchBarView(offset: Int) {
        val endToTransitionOffset = startToTransitionOffset + statusInfoTransitionOffset
        val maxTransitionOffset = endToTransitionOffset - startToTransitionOffset

        //Offset Alpha is not actually needed for changing the status bar color (only needed the offset),
        //but we will preserve the variable in case the stakeholders need to change the status bar alpha according to the scroll position
        val offsetAlpha = (MAXIMUM_ALPHA/maxTransitionOffset).times(offset - startToTransitionOffset)
        if (offsetAlpha >= ALPHA_CHANGE_THRESHOLD) {
            if (isInitialStatusBar) {
                setDarkStatusBar()
                otherMenuViewModel.setIsStatusBarInitialState(false)
            }
            shopStatusHeader?.gone()
            thematicIllustration?.gone()
            bg_white_other_menu?.gone()
        } else {
            if (!isInitialStatusBar) {
                setLightStatusBar()
                otherMenuViewModel.setIsStatusBarInitialState(true)
            }
            shopStatusHeader?.visible()
            thematicIllustration?.visible()
            bg_white_other_menu?.visible()
        }
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDefaultDarkStatusBar){
                activity?.requestStatusBarLight()
            }
            setStatusBarStateInitialIsLight(true)
            statusBarBackground?.hide()
        }
    }

    private fun setDarkStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarStateInitialIsLight(false)
            activity?.requestStatusBarDark()
            statusBarBackground?.show()
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

    private fun isActivityResumed(): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }

}