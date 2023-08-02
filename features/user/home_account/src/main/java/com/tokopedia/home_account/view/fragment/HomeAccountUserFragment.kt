package com.tokopedia.home_account.view.fragment

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.util.ViewHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.Analytics.ABOUT_US
import com.tokopedia.home_account.AccountConstants.Analytics.ACCOUNT_BANK
import com.tokopedia.home_account.AccountConstants.Analytics.ADDRESS_LIST
import com.tokopedia.home_account.AccountConstants.Analytics.APPLICATION_REVIEW
import com.tokopedia.home_account.AccountConstants.Analytics.DEVELOPER_OPTIONS
import com.tokopedia.home_account.AccountConstants.Analytics.LOGOUT
import com.tokopedia.home_account.AccountConstants.Analytics.PAYMENT_METHOD
import com.tokopedia.home_account.AccountConstants.Analytics.PERSONAL_DATA
import com.tokopedia.home_account.AccountConstants.Analytics.TERM_CONDITION
import com.tokopedia.home_account.AccountConstants.TDNBanner.TDN_INDEX
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.ResultBalanceAndPoint
import com.tokopedia.home_account.analytics.AddVerifyPhoneAnalytics
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.analytics.TokopediaPlusAnalytics
import com.tokopedia.home_account.data.model.CentralizedUserAssetConfig
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.LoadMoreRecommendation
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.RecommendationTitleView
import com.tokopedia.home_account.data.model.SeparatorView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.data.pref.AccountPreference
import com.tokopedia.home_account.databinding.BottomSheetOclBinding
import com.tokopedia.home_account.databinding.HomeAccountUserFragmentBinding
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.privacy_account.view.LinkAccountWebViewActivity
import com.tokopedia.home_account.privacy_account.view.LinkAccountWebviewFragment
import com.tokopedia.home_account.view.HomeAccountUserViewModel
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountBalanceAndPointAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.adapter.viewholder.CommonViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_KUPON_SAYA
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_TOKOMEMBER
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder.Companion.TYPE_TOPQUEST
import com.tokopedia.home_account.view.custom.HomeAccountEndlessScrollListener
import com.tokopedia.home_account.view.helper.StaticMenuGenerator
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.home_account.view.mapper.UiModelMapper
import com.tokopedia.home_account.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.internal_review.factory.createReviewHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintResult
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.loginfingerprint.tracker.BiometricTracker.Companion.EVENT_LABEL_SUCCESS
import com.tokopedia.loginfingerprint.view.activity.RegisterFingerprintActivity
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper
import com.tokopedia.loginfingerprint.view.helper.BiometricPromptHelper
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.sessioncommon.tracker.OclTracker
import com.tokopedia.sessioncommon.util.OclUtils
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

open class HomeAccountUserFragment :
    BaseDaggerFragment(),
    HomeAccountUserListener,
    BalanceAndPointListener,
    TokopediaPlusListener {

    @Inject
    lateinit var mapper: DataViewMapper

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var accountPref: AccountPreference

    @Inject
    lateinit var homeAccountAnalytic: HomeAccountAnalytics

    @Inject
    lateinit var tokopediaAnalytics: TokopediaPlusAnalytics

    @Inject
    lateinit var addVerifyPhoneAnalytics: AddVerifyPhoneAnalytics

    @Inject
    lateinit var biometricTracker: BiometricTracker

    @Inject
    lateinit var menuGenerator: StaticMenuGenerator

    @Inject
    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var oclUtils: OclUtils

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var biometricOfferingDialog: BottomSheetUnify? = null

    private val binding by viewBinding(HomeAccountUserFragmentBinding::bind)
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }
    private val reviewHelper by lazy { createReviewHelper(context?.applicationContext) }
    private var endlessRecyclerViewScrollListener: HomeAccountEndlessScrollListener? = null
    private var fpmBuyer: PerformanceMonitoring? = null
    private var trackingQueue: TrackingQueue? = null
    private var widgetTitle: String = ""
    private var topAdsHeadlineUiModel: TopadsHeadlineUiModel? = null
    private var isShowDarkModeToggle = false
    private var isShowScreenRecorder = false

    var adapter: HomeAccountUserAdapter? = null
    var balanceAndPointAdapter: HomeAccountBalanceAndPointAdapter? = null
    var memberAdapter: HomeAccountMemberAdapter? = null
    var commonAdapter: HomeAccountUserCommonAdapter? = null
    var isProfileSectionBinded = false
    val coachMarkItem = ArrayList<CoachMark2Item>()
    var appBarCollapseListener: onAppBarCollapseListener? = null
    var isNeedRefreshProfileItems = true
    var coachMark: CoachMark2? = null
    var balanceAndPointLocalLoad: LocalLoad? = null
    var memberLocalLoad: LocalLoad? = null
    var balanceAndPointCardView: CardUnify? = null
    var memberCardView: CardUnify? = null

    override fun getScreenName(): String = "homeAccountUserFragment"

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    private fun isEnablePrivacyAccount(): Boolean {
        return remoteConfig.getBoolean(REMOTE_CONFIG_KEY_PRIVACY_ACCOUNT, false)
    }

    private fun isEnableExplicitProfileMenu(): Boolean {
        return getAbTestPlatform()
            .getString(EXPLICIT_PROFILE_MENU_ROLLOUT)
            .contains(EXPLICIT_PROFILE_MENU_ROLLOUT)
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!userSession.isLoggedIn) {
            goToApplink(ApplinkConst.LOGIN)
            activity?.finish()
        } else {
            viewModel.refreshUserProfile()
        }
        fetchRemoteConfig()
        fpmBuyer = PerformanceMonitoring.start(FPM_BUYER)
        context?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeAccountUserActivity) appBarCollapseListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_account_user_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.homeAccountUserToolbar?.let {
            it.setIcon(
                IconBuilder(
                    IconBuilderFlag(pageSource = NavSource.ACCOUNT)
                ).addIcon(iconId = IconList.ID_NAV_GLOBAL) {}
            )
            viewLifecycleOwner.lifecycle.addObserver(it)
        }

        setupStatusBar()
        setupObserver()

        context?.run {
            coachMark = CoachMark2(this)
        }

        balanceAndPointAdapter = HomeAccountBalanceAndPointAdapter(this)
        memberAdapter = HomeAccountMemberAdapter(this)

        adapter = HomeAccountUserAdapter(
            this,
            this,
            balanceAndPointAdapter,
            memberAdapter,
            userSession,
            shopAdsNewPositionCallback
        )

        setupList()
        setLoadMore()
        showLoading()
        getData()

        binding?.homeAccountUserToolbar?.let {
            NavRecyclerViewScrollListener(
                navToolbar = it,
                startTransitionPixel = START_TRANSITION_PIXEL,
                toolbarTransitionRangePixel = TOOLBAR_TRANSITION_RANNGE_PIXEL,
                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                    override fun onAlphaChanged(offsetAlpha: Float) {
                        setStatusBarAlpha(offsetAlpha)
                    }

                    override fun onSwitchToDarkToolbar() {
                        it.switchToLightToolbar()
                    }

                    override fun onSwitchToLightToolbar() {
                    }

                    override fun onYposChanged(yOffset: Int) {
                    }
                }
            )
        }?.let { binding?.homeAccountUserFragmentRv?.addOnScrollListener(it) }

        binding?.homeAccountUserFragmentRv?.swipeLayout = binding?.homeAccountUserFragmentSwipeRefresh
        binding?.homeAccountUserFragmentSwipeRefresh?.setOnRefreshListener {
            coachMark?.dismissCoachMark()
            onRefresh()
            getData()
            isNeedRefreshProfileItems = true
            binding?.homeAccountUserFragmentSwipeRefresh?.isRefreshing = false
        }
    }

    private val shopAdsNewPositionCallback: (Int, CpmModel) -> Unit = { index, cpmModel ->
        val position = topAdsHeadlineUiModel?.let { topAdsHeadlineUiModel ->
            adapter?.getItems()?.let { it.indexOf(topAdsHeadlineUiModel) + index }
        }
        position?.let {
            addItem(TopadsHeadlineUiModel(cpmModel), addSeparator = false, position = it)
        }
    }

    override fun onLinkingAccountClicked(isLinked: Boolean) {
        homeAccountAnalytic.trackClickLinkAccount()
        if (isLinked) {
            LinkAccountWebViewActivity.gotoSuccessPage(activity, ApplinkConst.HOME)
        } else {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW).apply {
                putExtra(
                    ApplinkConstInternalGlobal.PARAM_LD,
                    LinkAccountWebviewFragment.BACK_BTN_APPLINK
                )
            }
            startActivityForResult(intent, REQUEST_CODE_LINK_ACCOUNT)
        }
    }

    override fun onProfileClicked() {
        homeAccountAnalytic.eventClickProfile()
    }

    override fun onIconWarningClicked(profile: ProfileDataView) {
        showBottomSheetAddName(profile)
    }

    override fun onEditProfileClicked() {
        homeAccountAnalytic.eventClickProfile()
        goToApplink(ApplinkConstInternalUserPlatform.SETTING_PROFILE)
    }

    override fun onMemberItemClicked(applink: String, type: Int) {
        when (type) {
            TYPE_TOKOMEMBER -> {
                homeAccountAnalytic.eventClickRewardMemberStore()
            }
            TYPE_TOPQUEST -> {
                homeAccountAnalytic.eventClickRewardTopQuest()
            }
            TYPE_KUPON_SAYA -> {
                homeAccountAnalytic.eventClickRewardMyCoupon()
            }
        }
        goToApplink(applink)
    }

    override fun onSettingItemClicked(item: CommonDataView) {
        mapSettingId(item)
    }

    override fun onSwitchChanged(item: CommonDataView, isActive: Boolean, switch: SwitchUnify) {
        when (item.id) {
            AccountConstants.SettingCode.SETTING_SHAKE_ID -> {
                homeAccountAnalytic.eventClickAppSettingShake(isActive)
                accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_SHAKE, isActive)
            }
            AccountConstants.SettingCode.SETTING_GEOLOCATION_ID -> {
                homeAccountAnalytic.eventClickAppSettingGeolocation(isActive)
                if (isActive) {
                    switch.isChecked = false
                    createAndShowLocationAlertDialog(isActive)
                } else {
                    goToApplicationDetailActivity()
                }
            }
            AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID -> {
                homeAccountAnalytic.eventClickAppSettingSafeMode(isActive)
                switch.isChecked = !isActive
                createAndShowSafeModeAlertDialog(isActive)
            }
            AccountConstants.SettingCode.SETTING_DARK_MODE -> {
                setupDarkMode(isActive)
            }
            AccountConstants.SettingCode.SETTING_PLAY_WIDGET_AUTOPLAY -> {
                accountPref.saveSettingValue(AccountConstants.KEY.KEY_PREF_PLAY_WIDGET_AUTOPLAY, isActive)
            }
            else -> {
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            AccountConstants.REQUEST.REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    updateLocationSwitch(true)
                } else {
                    goToApplicationDetailActivity()
                }
            }
        }
    }

    override fun onProfileAdapterReady(memberAdapter: HomeAccountMemberAdapter) {
        this.memberAdapter = memberAdapter
        if (isNeedRefreshProfileItems) {
            getProfileData()
        }
        isNeedRefreshProfileItems = false
    }

    override fun onItemViewBinded(position: Int, itemView: View, data: Any) {
        if (position == POSITION_0) {
            initMemberLocalLoad(itemView)
            initBalanceAndPointLocalLoad(itemView)
        }
    }

    override fun onProductRecommendationImpression(item: RecommendationItem, adapterPosition: Int) {
        trackingQueue?.let {
            homeAccountAnalytic.eventAccountProductView(it, item, adapterPosition)
        }
        activity?.let {
            if (item.isTopAds) {
                TopAdsUrlHitter(it)
                    .hitImpressionUrl(
                        it::class.qualifiedName,
                        item.trackerImageUrl,
                        item.productId.toString(),
                        item.name,
                        item.imageUrl,
                        COMPONENT_NAME_TOP_ADS
                    )
            }
        }
    }

    override fun onProductRecommendationClicked(item: RecommendationItem, adapterPosition: Int) {
        homeAccountAnalytic.eventAccountProductClick(item, adapterPosition, widgetTitle)
        activity?.let {
            if (item.isTopAds) {
                TopAdsUrlHitter(it).hitClickUrl(
                    it::class.qualifiedName,
                    item.clickUrl,
                    item.productId.toString(),
                    item.name,
                    item.imageUrl,
                    COMPONENT_NAME_TOP_ADS
                )
            }
        }

        RouteManager.getIntent(
            activity,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.productId.toString()
        ).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, adapterPosition)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    override fun onProductRecommendationThreeDotsClicked(
        item: RecommendationItem,
        adapterPosition: Int
    ) {
        showProductCardOptions(
            this,
            ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = item.isWishlist,
                productId = item.productId.toString(),
                isTopAds = item.isTopAds,
                topAdsWishlistUrl = item.wishlistUrl,
                topAdsClickUrl = item.clickUrl,
                productName = item.name,
                productImageUrl = item.imageUrl,
                productPosition = adapterPosition
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_FROM_PDP -> {
                data?.let {
                    val wishlistStatusFromPdp = data.getBooleanExtra(
                        WIHSLIST_STATUS_IS_WISHLIST,
                        false
                    )
                    val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                    updateWishlist(wishlistStatusFromPdp, position)
                }
            }
            REQUEST_CODE_CHANGE_NAME -> {
                if (resultCode == Activity.RESULT_OK) {
                    gotoSettingProfile()
                }
            }
            REQUEST_CODE_PROFILE_SETTING -> {
                getData()
            }
            REQUEST_CODE_LINK_ACCOUNT -> {
                viewModel.refreshPhoneNo()
            }
            REQUEST_CODE_REGISTER_BIOMETRIC -> {
                if (resultCode == Activity.RESULT_OK) {
                    biometricOfferingDialog?.dismiss()
                    biometricTracker.trackOnAktivasiResult(EVENT_LABEL_SUCCESS)
                    FingerprintDialogHelper.createBiometricOfferingSuccessDialog(requireActivity(), onPrimaryBtnClicked = {
                        doLogout()
                    })
                } else {
                    activity?.supportFragmentManager?.run {
                        if (biometricOfferingDialog?.isAdded == false) {
                            biometricOfferingDialog?.show(this, "")
                        }
                    }

                    val reason = if (data?.hasExtra(RegisterFingerprintActivity.RESULT_INTENT_REGISTER_BIOM) == true) {
                        data.getStringExtra(RegisterFingerprintActivity.RESULT_INTENT_REGISTER_BIOM)
                    } else {
                        "otp failed"
                    }

                    biometricTracker.trackOnBiometricResultFail(reason ?: "")
                    view?.run {
                        Toaster.build(this, getString(R.string.label_failed_register_biometric_offering), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                    }
                }
            }
            REQUEST_CODE_EXPLICIT_PROFILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    view?.let {
                        Toaster.build(
                            view = it,
                            text = getString(R.string.explicit_profile_message_success_save),
                            actionText = getString(R.string.explicit_profile_message_success_save_ok),
                            duration = Toaster.LENGTH_INDEFINITE
                        ).show()
                    }
                }
            }
            REQUEST_CODE_ADD_PHONE -> {
                getData()
                if (resultCode == Activity.RESULT_OK) {
                    view?.let { Toaster.build(it, getString(R.string.add_phone_success_message)).show() }
                }
            }
            REQUEST_CODE_VERIFY_PHONE -> {
                getData()
                if (resultCode == Activity.RESULT_OK) {
                    view?.let { Toaster.build(it, getString(R.string.verify_phone_success_message)).show() }
                }
            }
        }

        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            }
        )
    }

    override fun onCommonAdapterReady(position: Int, commonAdapter: HomeAccountUserCommonAdapter) {
        if (position == POSITION_2) {
            this.commonAdapter = commonAdapter
        }
    }

    override fun onClickBalanceAndPoint(balanceAndPointUiModel: BalanceAndPointUiModel) {
        homeAccountAnalytic.eventClickAccountPage(
            balanceAndPointUiModel.id,
            balanceAndPointUiModel.isActive,
            balanceAndPointUiModel.isFailed
        )
        if (balanceAndPointUiModel.isFailed) {
            balanceAndPointAdapter?.changeItemToShimmer(
                UiModelMapper.getBalanceAndPointShimmerUiModel(
                    balanceAndPointUiModel
                )
            )
            viewModel.getBalanceAndPoint(balanceAndPointUiModel.id, balanceAndPointUiModel.hideTitle, balanceAndPointUiModel.title)
        } else if (!balanceAndPointUiModel.applink.isEmpty()) {
            goToApplink(balanceAndPointUiModel.applink)
        }
    }

    private fun fetchRemoteConfig() {
        context?.let {
            isShowDarkModeToggle = remoteConfig.getBoolean(RemoteConfigKey.SETTING_SHOW_DARK_MODE_TOGGLE, false)
            isShowScreenRecorder = remoteConfig.getBoolean(RemoteConfigKey.SETTING_SHOW_SCREEN_RECORDER, false)
        }
    }

    private fun isEnableBiometricOffering(): Boolean {
        return getAbTestPlatform().getString(AccountConstants.RollenceKey.BIOMETRIC_ENTRY_POINT).isNotEmpty()
    }

    private fun setupObserver() {
        viewModel.buyerAccountDataData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetBuyerAccount(it.data)
                }
                is Fail -> {
                    onFailedGetBuyerAccount()
                }
            }
        }

        viewModel.firstRecommendationData.observe(
            viewLifecycleOwner,
            Observer {
                removeLoadMoreLoading()
                when (it) {
                    is Success -> onSuccessGetFirstRecommendationData(it.data.recommendationWidget, it.data.tdnBanner)
                    is Fail -> {
                        onFailGetData()
                        endlessRecyclerViewScrollListener?.changeLoadingStatus(false)
                    }
                }
            }
        )

        viewModel.getRecommendationData.observe(
            viewLifecycleOwner,
            Observer {
                removeLoadMoreLoading()
                when (it) {
                    is Success -> addRecommendationItem(it.data, null)
                    is Fail -> {
                        onFailGetData()
                        endlessRecyclerViewScrollListener?.changeLoadingStatus(false)
                    }
                }
            }
        )

        viewModel.shortcutData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetShortcutGroup(it.data)
                    }
                    is Fail -> {
                        onFailedGetShortcutGroup(it.throwable)
                    }
                }
            }
        )

        viewModel.centralizedUserAssetConfig.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetCentralizedAssetConfig(it.data)
                    }
                    is Fail -> {
                        onFailedGetCentralizedAssetConfig()
                    }
                }
            }
        )

        viewModel.balanceAndPoint.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is ResultBalanceAndPoint.Success -> {
                        onSuccessGetBalanceAndPoint(it.data)
                    }
                    is ResultBalanceAndPoint.Fail -> {
                        onFailedGetBalanceAndPoint(it.walletId)
                    }
                }
            }
        )

        viewModel.phoneNo.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    getData()
                }
            }
        )

        viewModel.safeModeStatus.observe(
            viewLifecycleOwner,
            Observer {
                updateSafeModeSwitch(it)
            }
        )

        viewModel.checkFingerprintStatus.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onSuccessGetFingerprintStatus(it.data)
                    is Fail -> onFailedGetFingerprintStatus(it.throwable)
                }
                hideLoading()
            }
        )

        viewModel.tokopediaPlusData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessLoadTokpediaPlusWidget(it.data)
                }
                is Fail -> {
                    adapter?.onFailedLoadTokopediaWidget(it.throwable)
                }
            }
        }
    }

    private fun onSuccessLoadTokpediaPlusWidget(data: TokopediaPlusDataModel) {
        adapter?.setTokopediaPlusContent(data)
    }

    fun onSuccessGetFingerprintStatus(checkFingerprintResponse: CheckFingerprintResult) {
        if (checkFingerprintResponse.isRegistered) {
            homeAccountAnalytic.trackOnShowLogoutDialog()
            showDialogLogout()
        } else {
            if (activity != null) {
                if (BiometricPromptHelper.isBiometricAvailable(requireActivity())) {
                    homeAccountAnalytic.trackOnShowBiometricOffering()
                    if (biometricOfferingDialog != null && biometricOfferingDialog?.isAdded == false) {
                        activity?.supportFragmentManager?.run {
                            biometricOfferingDialog?.show(this, "")
                        }
                    } else {
                        biometricOfferingDialog =
                            FingerprintDialogHelper.createBiometricOfferingDialog(
                                requireActivity(),
                                onPrimaryBtnClicked = {
                                    biometricTracker.trackClickOnAktivasi()
                                    val intent = RouteManager.getIntent(
                                        requireContext(),
                                        ApplinkConstInternalUserPlatform.REGISTER_BIOMETRIC
                                    )
                                    startActivityForResult(
                                        intent,
                                        REQUEST_CODE_REGISTER_BIOMETRIC
                                    )
                                    biometricOfferingDialog?.dismiss()
                                },
                                onSecondaryBtnClicked = {
                                    biometricTracker.trackClickOnTetapKeluar()
                                    showDialogLogout()
                                    biometricOfferingDialog?.dismiss()
                                },
                                onCloseBtnClicked = {
                                    biometricOfferingDialog?.dismiss()
                                    biometricTracker.trackClickOnCloseBtnOffering()
                                }
                            )
                    }
                } else {
                    showDialogLogout()
                }
            }
        }
    }

    fun onFailedGetFingerprintStatus(throwable: Throwable) {
        homeAccountAnalytic.trackOnShowBiometricOfferingFailed(throwable.message ?: "")
        showDialogLogout()
    }

    private fun onSuccessGetCentralizedAssetConfig(centralizedUserAssetConfig: CentralizedUserAssetConfig) {
        if (centralizedUserAssetConfig.assetConfig.isNotEmpty()) {
            centralizedUserAssetConfig.assetConfig.forEach {
                balanceAndPointAdapter?.addItemWallet(
                    UiModelMapper.getBalanceAndPointShimmerUiModel(
                        it
                    )
                )
            }
        }
        getBalanceAndPoints(centralizedUserAssetConfig)
    }

    private fun getBalanceAndPoints(centralizedUserAssetConfig: CentralizedUserAssetConfig) {
        centralizedUserAssetConfig.assetConfig.forEach {
            viewModel.getBalanceAndPoint(it.id, it.hideTitle, it.title)

            if (it.id == AccountConstants.WALLET.GOPAY) {
                balanceAndPointAdapter?.removeById(AccountConstants.WALLET.TOKOPOINT)
            }
        }
    }

    private fun onFailedGetCentralizedAssetConfig() {
        displayBalanceAndPointLocalLoad(true)
    }

    private fun onSuccessGetBalanceAndPoint(balanceAndPoint: WalletappGetAccountBalance) {
        balanceAndPointAdapter?.changeItemToSuccessBySameId(
            UiModelMapper.getBalanceAndPointUiModel(
                balanceAndPoint
            )
        )
        adapter?.notifyItemChanged(POSITION_0)
    }

    private fun onFailedGetBalanceAndPoint(walletId: String) {
        balanceAndPointAdapter?.changeItemToFailedById(walletId)
        adapter?.notifyItemChanged(POSITION_0)
    }

    private fun onSuccessGetShortcutGroup(shortcutResponse: ShortcutResponse) {
        displayMemberLocalLoad(false)
        adapter?.run {
            if (isFirstItemIsProfile()) {
                (getItem(POSITION_0) as ProfileDataView).memberStatus =
                    shortcutResponse.tokopointsStatusFiltered.statusFilteredData.tier
                notifyItemChanged(POSITION_0)
            }
        }
        val mappedMember = mapper.mapMemberItemDataView(shortcutResponse)
        memberAdapter?.addItems(mappedMember)
    }

    private fun onFailedGetShortcutGroup(throwable: Throwable) {
        setDefaultMemberTitle()
        displayMemberLocalLoad(true)
    }

    private fun onMemberErrorClicked() {
        viewModel.getShortcutData()
    }

    private fun setDefaultMemberTitle() {
        adapter?.setDefaultMemberTitle(getString(R.string.default_member_title))
        adapter?.notifyItemChanged(POSITION_0)
    }

    private fun onFailedGetBuyerAccount() {
        setDefaultMemberTitle()
        displayMemberLocalLoad(true)
        displayBalanceAndPointLocalLoad(true)
        onFailGetData()
    }

    private fun onFailGetData() {
        adapter?.run {
            if (isFirstItemIsProfile()) {
                removeItemAt(POSITION_0)
            }
            addItem(
                POSITION_0,
                ProfileDataView(
                    name = userSession.name,
                    phone = userSession.phoneNumber,
                    email = userSession.email,
                    avatar = userSession.profilePicture
                )
            )
            notifyItemChanged(POSITION_0)
        }
        hideLoading()
        fpmBuyer?.run { stopTrace() }
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = binding?.statusBarBg?.background
        drawable?.alpha = alpha.toInt()
        binding?.statusBarBg?.background = drawable
    }

    private fun isFirstItemIsProfile(): Boolean =
        adapter?.getItem(POSITION_0) is ProfileDataView

    private fun onSuccessGetBuyerAccount(buyerAccount: ProfileDataView) {
        displayMemberLocalLoad(false)
        displayBalanceAndPointLocalLoad(false)
        adapter?.run {
            if (isFirstItemIsProfile()) {
                removeItemAt(POSITION_0)
            }
            addItem(POSITION_0, buyerAccount)
            notifyItemChanged(POSITION_0)
        }
        hideLoading()
        fpmBuyer?.run { stopTrace() }

        if (accountPref.isShowCoachmark()) {
            setCoachMark()
        }
    }

    private fun setCoachMark() {
        lifecycleScope.launchWhenResumed {
            try {
                // Add coachmark delay to prevent racing condition
                delay(COACHMARK_DELAY_MS)
                val profileView = binding?.homeAccountUserFragmentRv?.layoutManager?.findViewByPosition(PROFILE_POS)
                profileView?.let {
                    coachMarkItem.add(
                        CoachMark2Item(
                            it.findViewById(R.id.home_account_profile_section),
                            getString(R.string.coachmark_title_account_info),
                            getString(R.string.coachmark_desc_account_info),
                            CoachMark2.POSITION_BOTTOM
                        )
                    )
                }
                val accountSettingView = binding?.homeAccountUserFragmentRv?.layoutManager?.findViewByPosition(ACC_SETTING_POS)
                accountSettingView?.let {
                    coachMarkItem.add(
                        CoachMark2Item(
                            accountSettingView.findViewById(R.id.home_account_expandable_layout_content_title),
                            getString(R.string.coachmark_title_setting),
                            getString(R.string.coachmark_desc_setting),
                            CoachMark2.POSITION_TOP
                        )
                    )
                }
                context?.run {
                    coachMark?.onFinishListener = {
                        accountPref.saveSettingValue(AccountConstants.KEY.KEY_SHOW_COACHMARK, false)
                    }
                    coachMark?.showCoachMark(coachMarkItem)
                }
            } catch (ignored: Exception) {}
        }
    }

    private fun onSuccessGetFirstRecommendationData(
        recommendation: RecommendationWidget,
        tdnBanner: TopAdsImageViewModel?
    ) {
        widgetTitle = recommendation.title
        addItem(RecommendationTitleView(widgetTitle), addSeparator = false)
        addTopAdsHeadLine()
        addRecommendationItem(recommendation.recommendationItemList, tdnBanner)
    }

    private fun addTopAdsHeadLine() {
        topAdsHeadlineUiModel = TopadsHeadlineUiModel()
        topAdsHeadlineUiModel?.let { addItem(it, addSeparator = false) }
    }

    private fun addRecommendationItem(
        list: List<RecommendationItem>,
        tdnBanner: TopAdsImageViewModel?
    ) {
        list.forEachIndexed { index, recommendationItem ->
            if (index == TDN_INDEX) tdnBanner?.let { adapter?.addItem(it) }
            adapter?.addItem(recommendationItem)
            adapter?.notifyItemChanged(index)
        }
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun getFirstRecommendation() {
        showLoadMoreLoading()
        viewModel.getFirstRecommendation()
    }

    private fun showLoading() {
        binding?.homeAccountShimmerLayout?.root?.show()
    }

    private fun hideLoading() {
        binding?.homeAccountShimmerLayout?.root?.hide()
    }

    private fun displayBalanceAndPointLocalLoad(isShow: Boolean) {
        if (isShow) {
            balanceAndPointLocalLoad?.show()
            balanceAndPointCardView?.hide()
        } else {
            balanceAndPointLocalLoad?.hide()
            balanceAndPointCardView?.show()
        }
    }

    private fun displayMemberLocalLoad(isShow: Boolean) {
        if (isShow) {
            memberLocalLoad?.show()
            memberCardView?.hide()
        } else {
            memberLocalLoad?.hide()
            memberCardView?.show()
        }
    }

    private fun setupStatusBar() {
        activity?.let {
            binding?.statusBarBg?.background = ColorDrawable(
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            )
            binding?.statusBarBg?.layoutParams?.height = ViewHelper.getStatusBarHeight(it)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding?.statusBarBg?.visibility = View.INVISIBLE
        } else {
            binding?.statusBarBg?.visibility = View.VISIBLE
        }
        setStatusBarAlpha(0f)
    }

    private fun getProfileData() {
        getWallet()
        loadTokopediaPlus()
        viewModel.getShortcutData()
    }

    private fun loadTokopediaPlus() {
        viewModel.getTokopediaWidgetContent()
    }

    private fun getWallet() {
        displayBalanceAndPointLocalLoad(false)
        viewModel.getCentralizedUserAssetConfig(USER_CENTRALIZED_ASSET_CONFIG_USER_PAGE)
    }

    private fun getData() {
        binding?.homeAccountUserFragmentRv?.scrollToPosition(POSITION_0)
        endlessRecyclerViewScrollListener?.resetState()
        viewModel.getBuyerData(BiometricPromptHelper.isBiometricAvailableActivity(activity))
        setupSettingList()
        getFirstRecommendation()
        viewModel.getSafeModeValue()
        if (oclUtils.isOclEnabled()) {
            viewModel.getOclStatus()
        }
    }

    private fun onRefresh() {
        showLoading()
        balanceAndPointAdapter?.clearAllItemsAndAnimateChanges()
        adapter?.clearAllItemsAndAnimateChanges()
    }

    private fun showDialogClearCache() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.new_home_account_clear_cache_warning))
            dialog.setDescription(getString(R.string.new_home_account_clear_cache_message))
            dialog.setPrimaryCTAText(getString(R.string.new_home_account_label_clear_cache_ok))
            dialog.setPrimaryCTAClickListener {
                clearCache()
            }
            dialog.setSecondaryCTAText(getString(R.string.new_home_account_label_clear_cache_cancel))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun clearCache() {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE)
        if (activityManager is ActivityManager) {
            activityManager.clearApplicationUserData()
        }
    }

    private fun setupList() {
        binding?.homeAccountUserFragmentRv?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding?.homeAccountUserFragmentRv?.adapter = adapter
        binding?.homeAccountUserFragmentRv?.isNestedScrollingEnabled = false
    }

    private fun setupSettingList() {
        val userSettingsMenu = menuGenerator.generateUserSettingMenu()

        val settingsMenuIterator = userSettingsMenu.items.listIterator()

        while (settingsMenuIterator.hasNext()) {
            val value = settingsMenuIterator.next()

            settingsMenuIterator.shouldRemove(
                when (value.id) {
                    AccountConstants.SettingCode.SETTING_LINK_ACCOUNT -> {
                        isEnablePrivacyAccount()
                    }
                    AccountConstants.SettingCode.SETTING_PRIVACY_ACCOUNT -> {
                        !isEnablePrivacyAccount()
                    }
                    AccountConstants.SettingCode.SETTING_EXPLICIT_PROFILE -> {
                        !isEnableExplicitProfileMenu()
                    }
                    else -> false
                }
            )
        }
        addItem(userSettingsMenu, addSeparator = true)
        addItem(
            menuGenerator.generateApplicationSettingMenu(
                accountPref,
                permissionChecker,
                isShowDarkModeToggle,
                isShowScreenRecorder
            ),
            addSeparator = true
        )
        addItem(menuGenerator.generateAboutTokopediaSettingMenu(), addSeparator = true)
        if (GlobalConfig.isAllowDebuggingTools()) {
            addItem(menuGenerator.generateDeveloperOptionsSettingMenu(), addSeparator = true)
        }
        addItem(
            SettingDataView(
                "",
                arrayListOf(
                    CommonDataView(id = AccountConstants.SettingCode.SETTING_OUT_ID, title = getString(R.string.menu_account_title_sign_out), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.SIGN_OUT, endText = "Versi ${GlobalConfig.VERSION_NAME}")
                ),
                isExpanded = true
            ),
            addSeparator = true
        )
    }

    private fun MutableListIterator<CommonDataView>.shouldRemove(shouldRemove: Boolean) {
        if (shouldRemove) this.remove()
    }

    private fun addItem(item: Any, addSeparator: Boolean, position: Int = -1) {
        try {
            if (position != -1) {
                adapter?.removeItemAt(position)
                adapter?.notifyItemRemoved(position)
                adapter?.addItem(position, item)
                if (addSeparator) {
                    adapter?.addItem(position, SeparatorView())
                }
            } else {
                adapter?.addItem(item)
                if (addSeparator) {
                    adapter?.addItem(SeparatorView())
                }
            }

            adapter?.notifyItemInserted(adapter?.itemCount.orZero())
        } catch (e: Exception) {
            e.message
        }
    }

    private fun goToApplink(applink: String) {
        if (applink.isNotEmpty()) {
            val intent = RouteManager.getIntent(context, applink)
            startActivity(intent)
        }
    }

    private fun goToWebview(link: String) {
        if (link.isNotEmpty()) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, link)
            startActivity(intent)
        }
    }

    private fun setupDarkMode(isDarkMode: Boolean) {
        setAppCompatMode(isDarkMode)
        saveDarkModeToSharefPreference(isDarkMode)
        homeAccountAnalytic.eventClickThemeSetting(isDarkMode)
        recreateView()
    }

    private fun setAppCompatMode(isDarkMode: Boolean) {
        val screenMode =
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(screenMode)
    }

    private fun saveDarkModeToSharefPreference(isDarkMode: Boolean) {
        accountPref.saveSettingValue(TkpdCache.Key.KEY_DARK_MODE, isDarkMode)
    }

    private fun recreateView() {
        activity?.run {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            startActivity(Intent(this, this.javaClass))
        }
    }

    private fun createAndShowSafeModeAlertDialog(currentValue: Boolean) {
        var dialogTitleMsg = getString(R.string.new_home_account_safe_mode_selected_dialog_title)
        var dialogBodyMsg = getString(R.string.new_home_account_safe_mode_selected_dialog_msg)
        var dialogPositiveButton =
            getString(R.string.new_home_account_safe_mode_selected_dialog_positive_button)
        val dialogNegativeButton = getString(R.string.new_home_account_label_cancel)

        if (!currentValue) {
            dialogTitleMsg = getString(R.string.new_home_account_safe_mode_unselected_dialog_title)
            dialogBodyMsg = getString(R.string.new_home_account_safe_mode_unselected_dialog_msg)
            dialogPositiveButton =
                getString(R.string.new_home_account_safe_mode_unselected_dialog_positive_button)
        }

        context?.run {
            val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.apply {
                setTitle(dialogTitleMsg)
                setDescription(dialogBodyMsg)
                setPrimaryCTAText(dialogPositiveButton)
                setPrimaryCTAClickListener {
                    viewModel.setSafeMode(currentValue)
                    dismiss()
                }
                setSecondaryCTAText(dialogNegativeButton)
                setSecondaryCTAClickListener { dismiss() }
            }
            dialog.show()
        }
    }

    private fun mapSettingId(item: CommonDataView) {
        when (item.id) {
            AccountConstants.SettingCode.SETTING_VIEW_ALL_BALANCE -> {
                homeAccountAnalytic.eventClickViewMoreWalletAccountPage()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_MORE_MEMBER -> {
                homeAccountAnalytic.eventClickOnMoreMemberOption()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_ACCOUNT_PERSONAL_DATA_ID -> {
                homeAccountAnalytic.eventClickAccountSetting(PERSONAL_DATA)
            }
            AccountConstants.SettingCode.SETTING_ACCOUNT_ADDRESS_ID -> {
                homeAccountAnalytic.eventClickAccountSetting(ADDRESS_LIST)
                homeAccountAnalytic.eventClickAccountSettingListAddress()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_BANK_ACCOUNT_ID -> {
                homeAccountAnalytic.eventClickPaymentSetting(ACCOUNT_BANK)
                homeAccountAnalytic.eventClickAccountSettingBankAccount()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_INSTANT_PAYMENT -> {
                homeAccountAnalytic.eventClickAccountSettingInstantPayment()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_INSTANT_BUY -> {
                homeAccountAnalytic.eventClickAccountSettingInstantBuy()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_NOTIFICATION -> {
                homeAccountAnalytic.eventClickAccountSettingNotification()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_APP_SETTING -> {
                homeAccountAnalytic.eventClickOnMoreAppSettingOption()
            }
            AccountConstants.SettingCode.SETTING_ABOUT_TOKOPEDIA -> {
                homeAccountAnalytic.eventClickOnMoreAboutTokopediaOption()
            }
            AccountConstants.SettingCode.SETTING_TKPD_PAY_ID -> {
                homeAccountAnalytic.eventClickSetting(PAYMENT_METHOD)
                goToApplink(item.applink)
            }

            AccountConstants.SettingCode.SETTING_TNC_ID -> {
                homeAccountAnalytic.eventClickSetting(TERM_CONDITION)
                homeAccountAnalytic.eventClickTermsAndConditionsAboutTokopedia()
                RouteManager.route(
                    activity,
                    AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_TERM_CONDITION
                )
            }

            AccountConstants.SettingCode.SETTING_ABOUT_US -> {
                homeAccountAnalytic.eventClickSetting(ABOUT_US)
                homeAccountAnalytic.eventClickGetToKnowAboutTokopedia()
                RouteManager.getIntent(
                    activity,
                    AccountConstants.Url.BASE_WEBVIEW_APPLINK +
                        AccountConstants.Url.BASE_MOBILE +
                        AccountConstants.Url.PATH_ABOUT_US
                ).run {
                    startActivity(this)
                }
            }

            AccountConstants.SettingCode.SETTING_IP -> {
                homeAccountAnalytic.eventClickIpAboutTokopedia()
                RouteManager.route(
                    activity,
                    AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_IP
                )
            }

            AccountConstants.SettingCode.SETTING_PRIVACY_ID -> {
                homeAccountAnalytic.eventClickSetting(PRIVACY_POLICY)
                homeAccountAnalytic.eventClickPrivacyPolicyAboutTokopedia()
                RouteManager.route(
                    activity,
                    AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_PRIVACY_POLICY
                )
            }

            AccountConstants.SettingCode.SETTING_APP_REVIEW_ID -> {
                homeAccountAnalytic.eventClickSetting(APPLICATION_REVIEW)
                homeAccountAnalytic.eventClickReviewAboutTokopedia()
                goToReviewApp()
            }

            AccountConstants.SettingCode.SETTING_DEV_OPTIONS -> if (GlobalConfig.isAllowDebuggingTools()) {
                homeAccountAnalytic.eventClickSetting(DEVELOPER_OPTIONS)
                RouteManager.route(activity, ApplinkConst.DEVELOPER_OPTIONS)
            }
            AccountConstants.SettingCode.SETTING_FEEDBACK_FORM -> if (GlobalConfig.isAllowDebuggingTools()) {
                RouteManager.route(activity, ApplinkConst.FEEDBACK_FORM)
            }
            AccountConstants.SettingCode.SETTING_OUT_ID -> {
                homeAccountAnalytic.eventClickSetting(LOGOUT)
                homeAccountAnalytic.eventClickLogout()
                checkLogoutOffering()
            }
            AccountConstants.SettingCode.SETTING_QUALITY_SETTING -> {
                RouteManager.route(context, ApplinkConstInternalUserPlatform.MEDIA_QUALITY_SETTING)
            }
            AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE -> {
                homeAccountAnalytic.eventClickAppSettingCleanCache()
                showDialogClearCache()
            }
            AccountConstants.SettingCode.SETTING_APP_ADVANCED_SCREEN_RECORD -> {
                context?.let {
                    homeAccountAnalytic.eventClickAppSettingScreenRecord()
                    RouteManager.route(it, ApplinkConstInternalGlobal.SCREEN_RECORDER)
                }
            }
            AccountConstants.SettingCode.SETTING_SECURITY -> {
                homeAccountAnalytic.eventClickAccountSettingAccountSecurity()
                val intent = RouteManager.getIntent(context, item.applink).apply {
                    putExtras(
                        Bundle().apply {
                            putExtra(ApplinkConstInternalGlobal.PARAM_NEW_HOME_ACCOUNT, true)
                        }
                    )
                }
                startActivity(intent)
            }
            AccountConstants.SettingCode.SETTING_LINK_ACCOUNT -> {
                homeAccountAnalytic.trackClickSettingLinkAcc()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_PRIVACY_ACCOUNT -> {
                homeAccountAnalytic.eventClickAccountSettingPrivacyAccount()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_EXPLICIT_PROFILE -> {
                val intent = RouteManager.getIntent(context, item.applink)
                startActivityForResult(intent, REQUEST_CODE_EXPLICIT_PROFILE)
            }
            else -> {
                goToApplink(item.applink)
            }
        }
    }

    private fun checkLogoutOffering() {
        if (viewModel.getOclStatus.value?.isShowing == true) {
            showOclBtmSheet()
        } else if (isEnableBiometricOffering()) {
            homeAccountAnalytic.trackOnClickLogoutDialog()
            viewModel.getFingerprintStatus()
        } else {
            showDialogLogout()
        }
    }

    fun getTncOclSpan(): SpannableString {
        val sourceString = SpannableString(requireContext().resources.getString(R.string.ocl_btm_sheet_subtitle))

        val startIndexTermAndCondition = sourceString.indexOf(getString(R.string.ocl_btm_sheet_tnc_index))
        val endIndexTermAndCondition = startIndexTermAndCondition.plus(getString(R.string.ocl_btm_sheet_tnc_index).length)

        sourceString.setSpan(
            clickableSpan(ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION),
            startIndexTermAndCondition,
            endIndexTermAndCondition,
            0
        )
        return sourceString
    }

    private fun clickableSpan(page: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                context?.let {
                    startActivity(RouteManager.getIntent(it, ApplinkConstInternalUserPlatform.TERM_PRIVACY, page))
                }
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            }
        }
    }

    private fun showOclBtmSheet() {
        val child = BottomSheetOclBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        child.btmSheetOclPositiveBtn.setOnClickListener {
            OclTracker.sendClickOnButtonSimpanLoginAccountEvent()
            doLogoutAndSaveSession()
        }

        child.btmSheetOclNegativeBtn.setOnClickListener {
            OclTracker.sendClickOnButtonNantiLoginAccountEvent()
            doLogout()
        }
        child.oclSubtitleText.movementMethod = LinkMovementMethod.getInstance()
        child.oclSubtitleText.setText(getTncOclSpan(), TextView.BufferType.SPANNABLE)
        child.imageUnify.loadImage(getString(R.string.ocl_bottomsheet_main_image))
        BottomSheetUnify().apply {
            setChild(child.root)
            setCloseClickListener {
                OclTracker.sendClickOnCloseButtonEvent()
                doLogout()
                dismiss()
            }
        }.show(parentFragmentManager, AccountConstants.LABEL_OCL_BTM_SHEET)
    }

    private fun doLogoutAndSaveSession() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.LOGOUT)
        intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_SAVE_SESSION, true)
        startActivity(intent)
    }

    private fun doLogout() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConstInternalUserPlatform.LOGOUT))
        }
    }

    private fun showDialogLogout() {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.new_home_account_label_logout))
            dialog.setDescription(getString(R.string.new_home_account_label_logout_confirmation))
            dialog.setPrimaryCTAText(getString(R.string.new_home_account_button_logout))
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
                doLogout()
            }
            dialog.setSecondaryCTAText(getString(R.string.new_home_account_label_cancel))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun createAndShowLocationAlertDialog(currentValue: Boolean) {
        if (!currentValue) {
            homeAccountAnalytic.eventClickToggleOnGeolocation()
        } else {
            homeAccountAnalytic.eventClickToggleOffGeolocation()
        }

        context?.run {
            val dialog =
                DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(getString(R.string.new_home_account_title_geolocation_alertdialog))
                    setDescription(getString(R.string.new_home_account_body_geolocation_alertdialog))
                    setPrimaryCTAText(getString(R.string.new_home_account_ok_geolocation_alertdialog))
                    setPrimaryCTAClickListener {
                        askPermissionLocation()
                        dismiss()
                    }
                    setSecondaryCTAText(getString(R.string.new_home_account_batal_geolocation_alertdialog))
                    setSecondaryCTAClickListener {
                        dismiss()
                    }
                }
            dialog.show()
        }
    }

    private fun askPermissionLocation() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            AccountConstants.REQUEST.REQUEST_LOCATION_PERMISSION
        )
    }

    private fun updateLocationSwitch(isEnable: Boolean) {
        commonAdapter?.list?.find { it.id == AccountConstants.SettingCode.SETTING_GEOLOCATION_ID }?.isChecked =
            isEnable
        commonAdapter?.notifyItemChanged(POSITION_1)
        adapter?.notifyItemChanged(POSITION_3)
    }

    private fun updateSafeModeSwitch(isEnable: Boolean) {
        commonAdapter?.list?.find { it.id == AccountConstants.SettingCode.SETTING_SAFE_SEARCH_ID }?.isChecked =
            isEnable
        commonAdapter?.notifyItemChanged(POSITION_2)
        adapter?.notifyItemChanged(POSITION_3)
    }

    private fun goToApplicationDetailActivity() {
        activity?.let {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", it.packageName, null)
            intent.data = uri
            it.startActivity(intent)
        }
    }

    private fun goToReviewApp() {
        if (reviewHelper != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                reviewHelper?.checkForCustomerReview(context, childFragmentManager, ::goToPlaystore)
            }
        } else {
            goToPlaystore()
        }
    }

    private fun goToPlaystore() {
        activity?.let {
            val uri = Uri.parse("market://details?id=" + it.application.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                it.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                it.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(AccountConstants.Url.PLAYSTORE_URL + it.application.packageName)
                    )
                )
            }
        }
    }

    private fun initMemberLocalLoad(itemView: View) {
        itemView.findViewById<LocalLoad>(R.id.home_account_member_local_load)?.let {
            memberLocalLoad = it
            memberLocalLoad?.refreshBtn?.setOnClickListener {
                memberLocalLoad?.progressState = !(memberLocalLoad?.progressState ?: false)
                onMemberErrorClicked()
            }
        }

        itemView.findViewById<CardUnify>(R.id.home_account_member_card)?.let {
            memberCardView = it
        }
    }

    private fun initBalanceAndPointLocalLoad(itemView: View) {
        itemView.findViewById<LocalLoad>(R.id.home_account_balance_and_point_local_load)?.let {
            balanceAndPointLocalLoad = it
            balanceAndPointLocalLoad?.refreshBtn?.setOnClickListener {
                homeAccountAnalytic.eventClickLocalLoadWalletAccountPage()
                balanceAndPointLocalLoad?.progressState =
                    !(balanceAndPointLocalLoad?.progressState ?: false)
                getWallet()
            }
        }

        itemView.findViewById<CardUnify>(R.id.home_account_balance_and_point_card)?.let {
            balanceAndPointCardView = it
        }
    }

    private fun setLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            val layoutManager = binding?.homeAccountUserFragmentRv?.layoutManager
            layoutManager?.let {
                endlessRecyclerViewScrollListener = object : HomeAccountEndlessScrollListener(it) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        if (isLoadingMore()) return
                        showLoadMoreLoading()
                        viewModel.getRecommendation(page)
                    }
                }
            }
        }
        endlessRecyclerViewScrollListener?.let {
            binding?.homeAccountUserFragmentRv?.addOnScrollListener(it)
        }
    }

    private fun showLoadMoreLoading() {
        adapter?.run {
            addItem(getItems().size, LoadMoreRecommendation())
            notifyItemInserted(itemCount)
        }
    }

    private fun removeLoadMoreLoading() {
        adapter?.run {
            if (getItems().isNotEmpty() && getItems()[getItems().lastIndex]::class == LoadMoreRecommendation::class) {
                removeItemAt(getItems().lastIndex)
                notifyItemRemoved(getItems().size)
            }
        }
    }

    private fun isLoadingMore(): Boolean {
        var isLoading = false
        adapter?.run {
            if (lastIndex > -1) {
                val lastItem = getItem(lastIndex)
                isLoading = lastItem is LoadMoreRecommendation
            } else {
                false
            }
        }
        return isLoading
    }

    private fun updateWishlist(wishlistStatusFromPdp: Boolean, position: Int) {
        adapter?.let {
            if (it.getItems().isNotEmpty() && (position < it.getItems().size)) {
                if (it.getItems()[position] is RecommendationItem) {
                    (it.getItems()[position] as RecommendationItem).isWishlist =
                        wishlistStatusFromPdp
                    it.notifyItemChanged(position)
                }
            }
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        homeAccountAnalytic.eventClickWishlistButton(!productCardOptionsModel.isWishlisted)
        handleWishlistV2Action(productCardOptionsModel)
    }

    private fun handleWishlistV2Action(productCardOptionsModel: ProductCardOptionsModel) {
        val recommendationItem = adapter?.getItems()
            ?.getOrNull(productCardOptionsModel.productPosition) as? RecommendationItem
            ?: return
        recommendationItem.isWishlist = productCardOptionsModel.wishlistResult.isAddWishlist

        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            showSuccessAddWishlistV2(productCardOptionsModel.wishlistResult)
            if (productCardOptionsModel.isTopAds) hitWishlistClickUrl(productCardOptionsModel)
        } else {
            showSuccessRemoveWishlistV2(productCardOptionsModel.wishlistResult)
        }
    }

    private fun hitWishlistClickUrl(item: ProductCardOptionsModel) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                this::class.java.simpleName,
                item.topAdsClickUrl + CLICK_TYPE_WISHLIST,
                item.productId,
                item.productName,
                item.productImageUrl
            )
        }
    }

    private fun showSuccessAddWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(wishlistResult, context, v)
            }
        }
    }

    private fun showBottomSheetAddName(profile: ProfileDataView) {
        activity?.let {
            val addNameLayout = View.inflate(context, R.layout.layout_bottom_sheet_add_name, null)
            val btnAddName: UnifyButton =
                addNameLayout.findViewById(R.id.layout_bottom_sheet_add_name_button)
            val iconAddName: ImageUnify =
                addNameLayout.findViewById(R.id.layout_bottom_sheet_add_name_icon)
            val bottomSheet = BottomSheetUnify()

            ImageUtils.loadImage(iconAddName, getString(R.string.add_name_url_icon))
            iconAddName.setOnClickListener {
                gotoChangeName(profile)
                bottomSheet.dismiss()
            }
            btnAddName.setOnClickListener {
                gotoChangeName(profile)
                bottomSheet.dismiss()
            }

            bottomSheet.setChild(addNameLayout)
            bottomSheet.clearAction()
            bottomSheet.setCloseClickListener {
                bottomSheet.dismiss()
            }
            childFragmentManager.run {
                bottomSheet.show(this, "bottom sheet add name")
            }
        }
    }

    private fun showBottomSheetLinkAccount() {
        val child = View.inflate(context, R.layout.layout_bottom_sheet_link_account, null)
        val btnPrimary: UnifyButton = child.findViewById(R.id.bottom_sheet_link_account_btn)
        val imagePrimary: ImageUnify = child.findViewById(R.id.bottom_sheet_link_account_image)

        BottomSheetUnify().apply {
//            ImageUtils.loadImage(imagePrimary)
            btnPrimary.setOnClickListener { }
            setChild(child)
            setCloseClickListener { dismiss() }
        }.show(parentFragmentManager, "linkAccount")
    }

    private fun gotoChangeName(profile: ProfileDataView) {
        val intent = RouteManager.getIntent(
            requireContext(),
            ApplinkConstInternalUserPlatform.CHANGE_NAME,
            profile.name,
            ""
        )
        startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME)
    }

    private fun gotoSettingProfile() {
        val intent =
            RouteManager.getIntent(requireContext(), ApplinkConstInternalUserPlatform.SETTING_PROFILE)
        startActivityForResult(intent, REQUEST_CODE_PROFILE_SETTING)
    }

    private fun showSuccessRemoveWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(wishlistResult, context, v)
            }
        }
    }

    override fun isShown(isShown: Boolean, pageSource: String, tokopediaPlusDataModel: TokopediaPlusDataModel) { }

    override fun onClick(pageSource: String, tokopediaPlusDataModel: TokopediaPlusDataModel) {
        tokopediaAnalytics.sendClickOnTokopediaPlusButtonEvent(tokopediaPlusDataModel.isSubscriber)
        val intent = RouteManager.getIntent(context, tokopediaPlusDataModel.applink)
        startActivity(intent)
    }

    override fun onAddPhoneClicked() {
        addVerifyPhoneAnalytics.sendClickAddPhoneNumberEvent()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PHONE)
        startActivityForResult(intent, REQUEST_CODE_ADD_PHONE)
    }

    override fun onVerifyPhoneCLicked(phoneNumber: String) {
        addVerifyPhoneAnalytics.sendClickVerifiedPhoneNumberEvent()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PHONE_WITH, phoneNumber)
        startActivityForResult(intent, REQUEST_CODE_VERIFY_PHONE)
    }

    override fun onRetry() {
        viewModel.getTokopediaWidgetContent()
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    companion object {
        private const val REQUEST_CODE_CHANGE_NAME = 300
        private const val REQUEST_CODE_PROFILE_SETTING = 301
        private const val REQUEST_FROM_PDP = 394
        private const val REQUEST_CODE_LINK_ACCOUNT = 302
        private const val REQUEST_CODE_REGISTER_BIOMETRIC = 303
        private const val REQUEST_CODE_EXPLICIT_PROFILE = 304
        private const val REQUEST_CODE_ADD_PHONE = 305
        private const val REQUEST_CODE_VERIFY_PHONE = 306

        private const val START_TRANSITION_PIXEL = 200
        private const val TOOLBAR_TRANSITION_RANNGE_PIXEL = 50

        private const val COMPONENT_NAME_TOP_ADS = "Account Home Recommendation Top Ads"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val FPM_BUYER = "mp_account_buyer"
        private const val USER_CENTRALIZED_ASSET_CONFIG_USER_PAGE = "user_page"

        const val REMOTE_CONFIG_KEY_PRIVACY_ACCOUNT = "android_user_privacy_account_enabled"
        private const val EXPLICIT_PROFILE_MENU_ROLLOUT = "explicit_android"
        private const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"

        private const val COACHMARK_SIZE = 3

        private const val POSITION_0 = 0
        private const val POSITION_1 = 1
        private const val POSITION_2 = 2
        private const val POSITION_3 = 3

        private const val PROFILE_POS = 0
        private const val ACC_SETTING_POS = 1

        private const val COACHMARK_DELAY_MS = 1000L
        private const val PRIVACY_POLICY = "Kebijakan Privasi"

        fun newInstance(bundle: Bundle?): Fragment {
            return HomeAccountUserFragment().apply {
                arguments = bundle
            }
        }
    }
}
