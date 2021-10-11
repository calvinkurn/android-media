package com.tokopedia.sellerhome.settings.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.requestStatusBarLight
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.menu.common.analytics.*
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.StatisticMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.sellerhome.settings.analytics.SettingPerformanceTracker
import com.tokopedia.sellerhome.settings.analytics.SettingShopOperationalTracker
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.adapter.OtherMenuAdapter
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.OtherMenuShopShareData
import com.tokopedia.sellerhome.settings.view.bottomsheet.SettingsFreeShippingBottomSheet
import com.tokopedia.sellerhome.settings.view.fragment.old.OtherMenuFragment
import com.tokopedia.sellerhome.settings.view.viewholder.OtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherMenuViewModel
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class OtherMenuFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(),
    SettingTrackingListener, OtherMenuAdapter.Listener, OtherMenuViewHolder.Listener,
    StatusBarCallback, SellerHomeFragmentListener, ShareBottomsheetListener {

    companion object {
        private const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"
        private const val TAB_PM_PARAM = "tab"

        private const val TOKOPEDIA_SUFFIX = "| Tokopedia"
        private const val DELIMITER = " - "

        const val OTHER_MENU_SHARE_BOTTOM_SHEET_PAGE_NAME = "Seller App - Lainnya"
        const val OTHER_MENU_SHARE_BOTTOM_SHEET_FEATURE_NAME = "Share"

        @JvmStatic
        fun createInstance(): com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment =
            com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment()
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

    @Inject
    lateinit var settingPerformanceTracker: SettingPerformanceTracker

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OtherMenuViewModel::class.java)
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

    private val isSharingEnabled by lazy {
        context?.let {
            UniversalShareBottomSheet.isCustomSharingEnabled(it)
        } == true
    }

    @FragmentType
    private var currentFragmentType: Int = FragmentType.OTHER

    private var viewHolder: OtherMenuViewHolder? = null

    private var multipleErrorSnackbar: Snackbar? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private var canShowErrorToaster = true
    private var hasShownMultipleErrorToaster = false

    private var shopShareInfo: OtherMenuShopShareData? = null
    private var shopSnippetImageUrl: String = ""
    private var canShowShareBottomSheet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SellerHomeActivity)?.attachCallback(this)
        viewModel.run {
            setErrorStateMapDefaultValue()
            setSuccessStateMapDefaultValue()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            viewHolder = OtherMenuViewHolder(view, it, this, userSession, this)
        }
        viewHolder?.setInitialLayouts()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBar()
        }
        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllOtherMenuData()
    }

    override fun onItemClicked(t: SettingUiModel?) {}

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory =
        OtherMenuAdapterTypeFactory(this, userSession = userSession)

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun getScreenName(): String = ""

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        if (settingShopInfoImpressionTrackable is StatisticMenuItemUiModel) {
            sendEventImpressionStatisticMenuItem(userSession.userId)
        } else {
            settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> {
        return OtherMenuAdapter(context, this, adapterTypeFactory)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_sah_new_other_menu

    override fun goToPrintingPage() {
        val url = "${TokopediaUrl.getInstance().WEB}${SellerBaseUrl.PRINTING}"
        val applink = String.format(APPLINK_FORMAT_ALLOW_OVERRIDE, ApplinkConst.WEBVIEW, false, url)
        RouteManager.getIntent(context, applink)?.let {
            context?.startActivity(it)
        }
    }

    override fun goToSettings() {
        startActivity(Intent(context, MenuSettingActivity::class.java))
    }

    override fun getRecyclerView(): RecyclerView? = getRecyclerView(view)

    override fun getFragmentAdapter(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> =
        adapter

    override fun onShopInfoClicked() {
        RouteManager.route(context, ApplinkConst.SHOP, userSession.shopId)
    }

    override fun onShopBadgeClicked() {
        NewOtherMenuTracking.sendEventClickShopReputationBadge()
        goToReputationHistory()
    }

    override fun onFollowersCountClicked() {
//        NewOtherMenuTracking.sendEventClickTotalFollowers()
//        goToShopFavouriteList()
    }

    override fun onSaldoClicked() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalGlobal.WEBVIEW,
                ApplinkConst.WebViewUrl.SALDO_DETAIL
            )
            context?.startActivity(intent)
        }
        NewOtherMenuTracking.sendEventClickSaldoBalance()
    }

    override fun onKreditTopadsClicked() {
        val bottomSheet =
            childFragmentManager.findFragmentByTag(OtherMenuFragment.TOPADS_BOTTOMSHEET_TAG)
        if (bottomSheet is BottomSheetUnify) {
            bottomSheet.dismiss()
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTO_TOPUP)
        } else {
            RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_CREDIT)
        }
        NewOtherMenuTracking.sendEventClickTopadsBalance()
    }

    override fun onFreeShippingClicked() {
        freeShippingTracker.trackFreeShippingClick()
        val freeShippingBottomSheet = SettingsFreeShippingBottomSheet.createInstance()
        if (isActivityResumed()) {
            freeShippingBottomSheet.show(childFragmentManager)
        }
    }

    override fun onShopOperationalClicked() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
    }

    override fun onGoToPowerMerchantSubscribe(tab: String?) {
        sellerMenuTracker.sendEventClickShopSettingNew()
        if (tab != null) {
            val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
            val appLinkPMTab =
                Uri.parse(appLink).buildUpon().appendQueryParameter(TAB_PM_PARAM, tab).build()
                    .toString()
            context.let { RouteManager.route(it, appLinkPMTab) }
        }
    }

    override fun onRefreshShopInfo() {
        viewModel.getUserShopInfo()
    }

    override fun onShopBadgeRefresh() {
        viewModel.getShopBadge()
    }

    override fun onShopTotalFollowersRefresh() {
        viewModel.getShopTotalFollowers()
    }

    override fun onUserInfoRefresh() {
        viewModel.getUserShopInfo()
    }

    override fun onOperationalHourRefresh() {
        viewModel.getShopOperational()
    }

    override fun onSaldoBalanceRefresh() {
        viewModel.getBalanceInfo()
    }

    override fun onKreditTopAdsRefresh() {
        viewModel.getKreditTopAds()
    }

    override fun onFreeShippingRefresh() {
        viewModel.getFreeShippingStatus()
    }

    override fun onTopAdsTooltipClicked(isTopAdsActive: Boolean) {
        val bottomSheetChildView = setupBottomSheetLayout(isTopAdsActive)
        bottomSheetChildView?.run {
            with(topAdsBottomSheet) {
                setChild(this@run)
                show(
                    this@OtherMenuFragment.childFragmentManager,
                    OtherMenuFragment.TOPADS_BOTTOMSHEET_TAG
                )
            }
        }
    }

    override fun onTopadsValueSet() {
        viewModel.startToggleTopadsCredit()
    }

    override fun onShareButtonClicked() {
        NewOtherMenuTracking.sendEventClickShareButton(userSession.shopId, userSession.userId)
        saveImageToStorageBeforeShowBottomsheet()
    }

    override fun onScrollToTop() {
        viewHolder?.scrollToTop()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setStatusBar() {
        (activity as? Activity)?.requestStatusBarLight()
    }

    override fun setCurrentFragmentType(fragmentType: Int) {
        currentFragmentType = fragmentType
        if (fragmentType != FragmentType.OTHER) {
            multipleErrorSnackbar?.dismiss()
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.SHOP_TYPE
            uri = shopShareInfo?.coreUrl
            id = userSession.shopId
            //set and share in the Linker Data
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            ogTitle = getShareBottomSheetOgTitle()
            ogDescription = getShareBottomSheetOgDescription()
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        })
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    checkUsingCustomBranchLinkDomain(linkerShareData)
                    val shareString = getString(
                        R.string.sah_new_other_share_text,
                        userSession.shopName,
                        linkerShareData?.shareContents
                    )
                    shareModel.subjectName = userSession.shopName
                    SharingUtil.executeShareIntent(
                        shareModel,
                        linkerShareData,
                        activity,
                        view,
                        shareString
                    )

                    NewOtherMenuTracking.sendEventClickSharingChannel(
                        shopId = userSession.shopId,
                        userId = userSession.userId,
                        channel = shareModel.channel.orEmpty()
                    )

                    universalShareBottomSheet?.dismiss()
                }

                override fun onError(linkerError: LinkerError?) {}
            })
        )
    }

    override fun onCloseOptionClicked() {
        NewOtherMenuTracking.sendEventClickCloseShareBottomSheet(
            userSession.shopId,
            userSession.userId
        )
    }

    override fun onShopStatusImpression(shopType: ShopType) {
        NewOtherMenuTracking.sendEventImpressionShopStatus(shopType)
    }

    override fun onFreeShippingImpression() {
        freeShippingTracker.trackFreeShippingImpression()
    }

    private fun observeLiveData() {
        observeShopBadge()
        observeShopTotalFollowers()
        observeShopStatus()
        observeShopOperationalHour()
        observeSaldoBalance()
        observeKreditTopads()
        observeFreeShipping()
        observeIsTopAdsAutoTopup()
        observeShopPeriod()
        observeShopShareInfo()
        observeShouldSwipeSecondaryInfo()
        observeMultipleErrorToaster()
        observeToasterAlreadyShown()
        observeToggleTopadsCount()
    }

    private fun observeShopBadge() {
        viewModel.shopBadgeLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setReputationBadgeData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onShopBadgeRefresh()
                }
                logHeaderError(it.throwable, OtherMenuFragment.SHOP_BADGE)
            }
        }
    }

    private fun observeShopTotalFollowers() {
        viewModel.shopTotalFollowersLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setShopFollowersData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onShopTotalFollowersRefresh()
                }
                logHeaderError(it.throwable, OtherMenuFragment.SHOP_FOLLOWERS)
            }
        }
    }

    private fun observeShopStatus() {
        viewModel.userShopInfoLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setShopStatusData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onRefreshShopInfo()
                }
                logHeaderError(it.throwable, OtherMenuFragment.SHOP_INFO)
            }
        }
    }

    private fun observeShopOperationalHour() {
        viewModel.shopOperationalLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setShopOperationalData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onOperationalHourRefresh()
                }
                logHeaderError(it.throwable, OtherMenuFragment.OPERATIONAL_HOUR)
            }
        }
    }

    private fun observeSaldoBalance() {
        viewModel.balanceInfoLiveData.observe(viewLifecycleOwner) {
            activity?.runOnUiThread {
                viewHolder?.setBalanceSaldoData(it)
                if (it is SettingResponseState.SettingError) {
                    showErrorToaster(it.throwable) {
                        onSaldoBalanceRefresh()
                    }
                    logHeaderError(it.throwable, OtherMenuFragment.SALDO_BALANCE)
                }
            }
        }
    }

    private fun observeKreditTopads() {
        viewModel.kreditTopAdsLiveData.observe(viewLifecycleOwner) {
            activity?.runOnUiThread {
                viewHolder?.setBalanceTopadsData(it)
                if (it is SettingResponseState.SettingError) {
                    showErrorToaster(it.throwable) {
                        onKreditTopAdsRefresh()
                    }
                    logHeaderError(it.throwable, OtherMenuFragment.TOPADS_BALANCE)
                }
            }
        }
    }

    private fun observeFreeShipping() {
        viewModel.freeShippingLiveData.observe(viewLifecycleOwner) {
            viewHolder?.setFreeShippingData(it)
            if (it is SettingResponseState.SettingError) {
                showErrorToaster(it.throwable) {
                    onFreeShippingRefresh()
                }
                logHeaderError(it.throwable, OtherMenuFragment.FREE_SHIPPING)
            }
        }
    }

    private fun observeIsTopAdsAutoTopup() {
        viewModel.isTopAdsAutoTopupLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    viewHolder?.setIsTopadsAutoTopup(result.data)
                }
                is Fail -> {
                    showErrorToaster(result.throwable)
                    logHeaderError(result.throwable, OtherMenuFragment.TOPADS_AUTO_TOPUP)
                }
            }
        }
    }

    private fun observeShopPeriod() {
        observe(viewModel.shopPeriodType) {
            when (it) {
                is Success -> {
                    setTrackerPerformanceMenu(it.data.isNewSeller)
                }
                is Fail -> { }
            }
        }
        viewModel.getShopPeriodType()
    }

    private fun observeShopShareInfo() {
        viewModel.shopShareInfoLiveData.observe(viewLifecycleOwner) { shareInfo ->
            if (isSharingEnabled) {
                animateShareButtonFromShareData(shareInfo)
            }
        }
    }

    private fun observeShouldSwipeSecondaryInfo() {
        viewModel.shouldSwipeSecondaryInfo.observe(viewLifecycleOwner) { shouldSwipe ->
            if (shouldSwipe) {
                viewHolder?.swipeSecondaryInfoGently()
            }
        }
    }

    private fun observeMultipleErrorToaster() {
        viewModel.shouldShowMultipleErrorToaster.observe(viewLifecycleOwner) { shouldShowError ->
            if (shouldShowError) {
                showMultipleErrorToaster()
            } else {
                multipleErrorSnackbar?.dismiss()
            }
        }
    }

    private fun observeToasterAlreadyShown() {
        viewModel.isToasterAlreadyShown.observe(viewLifecycleOwner) { isToasterAlreadyShown ->
            canShowErrorToaster = !isToasterAlreadyShown
        }
    }

    private fun observeToggleTopadsCount() {
        viewModel.numberOfTopupToggleCounts.observe(viewLifecycleOwner) { count ->
            if (count != null) {
                viewHolder?.toggleTopadsTopup()
            }
        }
    }

    private fun goToReputationHistory() {
        val reputationHistoryIntent =
            RouteManager.getIntent(context, ApplinkConst.REPUTATION).apply {
                putExtra(OtherMenuFragment.GO_TO_REPUTATION_HISTORY, true)
            }
        startActivity(reputationHistoryIntent)
    }

    private fun goToShopFavouriteList() {
        val shopFavouriteListIntent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST)
                .apply {
                    putExtra(OtherMenuFragment.EXTRA_SHOP_ID, userSession.shopId)
                }
        startActivity(shopFavouriteListIntent)
    }

    private fun isActivityResumed(): Boolean {
        val state = (activity as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED
    }

    private fun setupBottomSheetLayout(isTopAdsActive: Boolean): View? {
        val bottomSheetInfix: String
        val bottomSheetDescription: String
        if (isTopAdsActive) {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_active)
            bottomSheetDescription = resources.getString(R.string.setting_topads_description_active)
        } else {
            bottomSheetInfix = resources.getString(R.string.setting_topads_status_inactive)
            bottomSheetDescription =
                resources.getString(R.string.setting_topads_description_inactive)
        }
        val bottomSheetTitle = resources.getString(R.string.setting_topads_status, bottomSheetInfix)
        return topAdsBottomSheetView?.apply {
            findViewById<Typography>(R.id.topAdsBottomSheetTitle)?.text = bottomSheetTitle
            findViewById<TextView>(R.id.topAdsBottomSheetDescription)?.text = bottomSheetDescription
            findViewById<UnifyButton>(R.id.topAdsNextButton)?.setOnClickListener {
                onKreditTopadsClicked()
            }
        }
    }

    private fun showMultipleErrorToaster() {
        multipleErrorSnackbar =
            view?.run {
                Toaster.build(
                    this,
                    context?.getString(R.string.setting_header_multiple_error_message)
                        .orEmpty(),
                    Snackbar.LENGTH_INDEFINITE,
                    Toaster.TYPE_NORMAL,
                    context?.getString(R.string.setting_toaster_error_retry).orEmpty()
                )
                {
                    viewModel.reloadErrorData()
                    viewModel.onShownMultipleError()
                    hasShownMultipleErrorToaster = false
                }.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        viewModel.onShownMultipleError()
                        hasShownMultipleErrorToaster = false
                    }
                })
            }
        multipleErrorSnackbar?.show()
        viewModel.onShownMultipleError(true)
        hasShownMultipleErrorToaster = true
    }

    private fun showErrorToaster(throwable: Throwable, onRetryAction: () -> Unit = {}) {
        viewModel.onCheckDelayErrorResponseTrigger()
        val canShowToaster = currentFragmentType == FragmentType.OTHER && canShowErrorToaster
        if (canShowToaster && !hasShownMultipleErrorToaster) {
            val errorMessage = context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            } ?: resources.getString(R.string.setting_toaster_error_message)
            view?.showToasterError(errorMessage, onRetryAction)
        }
    }

    private fun logHeaderError(throwable: Throwable, errorType: String) {
        SellerHomeErrorHandler.logException(
            throwable,
            context?.getString(R.string.setting_header_error_message,
                errorType
            ).orEmpty()
        )
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

    private fun animateShareButtonFromShareData(shareInfo: OtherMenuShopShareData?) {
        if (shareInfo != null) {
            viewHolder?.runShareButtonAnimation()
            shopShareInfo = shareInfo
        }
    }

    private fun saveImageToStorageBeforeShowBottomsheet() {
        shopShareInfo?.shopSnippetUrl?.let { snippetUrl ->
            if (snippetUrl.isNotEmpty() && canShowShareBottomSheet) {
                canShowShareBottomSheet = false
                shopSnippetImageUrl = snippetUrl
                context?.let {
                    SharingUtil.saveImageFromURLToStorage(it, shopSnippetImageUrl) { storageImage ->
                        canShowShareBottomSheet = true
                        showUniversalShareBottomSheet(storageImage)
                    }
                }
            }
        }
    }

    private fun showUniversalShareBottomSheet(storageImageUrl: String) {
        universalShareBottomSheet = null
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@OtherMenuFragment)
            setUtmCampaignData(
                OTHER_MENU_SHARE_BOTTOM_SHEET_PAGE_NAME,
                userSession.userId,
                userSession.shopId,
                OTHER_MENU_SHARE_BOTTOM_SHEET_FEATURE_NAME
            )
            setMetaData(
                userSession.shopName,
                userSession.shopAvatar,
                ""
            )
            setOgImageUrl(shopSnippetImageUrl)
        }.also { shareBottomSheet ->
            activity?.supportFragmentManager?.let { fm ->
                shareBottomSheet.run {
                    imageSaved(storageImageUrl)
                    show(fm, this@OtherMenuFragment)

                    NewOtherMenuTracking.sendEventImpressionViewOnSharingChannel(
                        userSession.shopId, userSession.userId
                    )
                }
            }
        }
    }

    private fun getShareBottomSheetOgTitle(): String {
        return shopShareInfo?.let {
            "${
                joinStringWithDelimiter(
                    userSession.shopName,
                    it.location,
                    delimiter = DELIMITER
                )
            } $TOKOPEDIA_SUFFIX"
        }.orEmpty()
    }

    private fun getShareBottomSheetOgDescription(): String {
        return shopShareInfo?.let {
            joinStringWithDelimiter(it.description, it.tagline, delimiter = DELIMITER)
        }.orEmpty()
    }

    private fun joinStringWithDelimiter(vararg listString: String, delimiter: String): String {
        val filteredListString = listString.filter {
            it.isNotEmpty()
        }
        return TextUtils.join(delimiter, filteredListString)
    }

    private fun checkUsingCustomBranchLinkDomain(linkerShareData: LinkerShareResult?) {
        val shopBranchLinkDomain = shopShareInfo?.branchLinkDomain.orEmpty()
        if (shopBranchLinkDomain.isNotEmpty())
            changeLinkerShareDataContent(linkerShareData, shopBranchLinkDomain)
    }

    private fun changeLinkerShareDataContent(
        linkerShareData: LinkerShareResult?,
        shopBranchLinkDomain: String
    ) {
        linkerShareData?.apply {
            shareContents = replaceLastUrlSegment(shareContents.orEmpty(), shopBranchLinkDomain)
            url = replaceLastUrlSegment(url.orEmpty(), shopBranchLinkDomain)
            shareUri = replaceLastUrlSegment(shareUri.orEmpty(), shopBranchLinkDomain)
        }
    }

    private fun replaceLastUrlSegment(urlString: String, replacementValue: String): String {
        return urlString.split("/").toMutableList().also { list ->
            list[list.lastIndex] = replacementValue
        }.joinToString("/")
    }

    private fun View.showToasterError(errorMessage: String, onRetryAction: () -> Unit) {
        Toaster.build(
            this,
            errorMessage,
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_ERROR,
            resources.getString(R.string.setting_toaster_error_retry)
        ) {
            onRetryAction()
        }.show()
    }

}