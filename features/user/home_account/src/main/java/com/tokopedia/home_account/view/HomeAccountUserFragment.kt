package com.tokopedia.home_account.view

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
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
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
import com.tokopedia.home_account.AccountConstants.Analytics.PRIVACY_POLICY
import com.tokopedia.home_account.AccountConstants.Analytics.TERM_CONDITION
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.data.model.*
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountFinancialAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.custom.HomeAccountEndlessScrollListener
import com.tokopedia.home_account.view.helper.StaticMenuGenerator
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.home_account.view.viewholder.ErrorFinancialItemViewHolder
import com.tokopedia.home_account.view.viewholder.ErrorFinancialViewHolder
import com.tokopedia.home_account.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_KUPON_SAYA
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_TOKOMEMBER
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder.Companion.TYPE_TOPQUEST
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.internal_review.factory.createReviewHelper
import kotlinx.android.synthetic.main.home_account_user_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserFragment : BaseDaggerFragment(), HomeAccountUserListener {

    @Inject
    lateinit var mapper: DataViewMapper

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var accountPref: AccountPreference

    @Inject
    lateinit var homeAccountAnalytic: HomeAccountAnalytics

    @Inject
    lateinit var menuGenerator: StaticMenuGenerator

    @Inject
    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }
    private val reviewHelper by lazy { createReviewHelper(context?.applicationContext) }
    private var endlessRecyclerViewScrollListener: HomeAccountEndlessScrollListener? = null
    private var fpmBuyer: PerformanceMonitoring? = null
    private var trackingQueue: TrackingQueue? = null
    private var widgetTitle: String = ""
    private var isShowHomeAccountTokopoints = false

    var adapter: HomeAccountUserAdapter? = null
    var financialAdapter: HomeAccountFinancialAdapter? = null
    var memberAdapter: HomeAccountMemberAdapter? = null
    var commonAdapter: HomeAccountUserCommonAdapter? = null
    var isProfileSectionBinded = false
    val coachMarkItem = ArrayList<CoachMark2Item>()
    var appBarCollapseListener: onAppBarCollapseListener? = null
    var isNeedRefreshProfileItems = true
    var coachMark: CoachMark2? = null
    var memberLocalLoad: LocalLoad? = null
    var memberCardView: CardUnify? = null
    var memberTitle: Typography? = null
    var memberIcon: ImageUnify? = null

    override fun getScreenName(): String = "homeAccountUserFragment"

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!userSession.isLoggedIn) {
            goToApplink(ApplinkConst.LOGIN)
            activity?.finish()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_account_user_fragment, container, false)
    }

    private fun fetchRemoteConfig() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            isShowHomeAccountTokopoints = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_HOME_ACCOUNT_TOKOPOINTS, false)
        }
    }

    private fun setupObserver() {
        viewModel.buyerAccountDataData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetBuyerAccount(it.data)
                is Fail -> onFailGetData()
            }
        })

        viewModel.firstRecommendationData.observe(viewLifecycleOwner, Observer {
            removeLoadMoreLoading()
            when (it) {
                is Success -> onSuccessGetFirstRecommendationData(it.data)
                is Fail -> {
                    onFailGetData()
                    endlessRecyclerViewScrollListener?.changeLoadingStatus(false)
                }
            }
        })

        viewModel.getRecommendationData.observe(viewLifecycleOwner, Observer {
            removeLoadMoreLoading()
            when (it) {
                is Success -> addRecommendationItem(it.data)
                is Fail -> {
                    onFailGetData()
                    endlessRecyclerViewScrollListener?.changeLoadingStatus(false)
                }
            }
        })

        viewModel.ovoBalance.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetOvoBalance(it.data)
                }
                is Fail -> {
                    onFailedGetOvoBalance()
                }
            }
        })

        viewModel.shortcutData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    memberLocalLoad?.hide()
                    memberCardView?.show()

                    val leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, resources.displayMetrics)
                    memberTitle?.setMargin(leftMargin.toInt(), 0, 0,  0)
                    memberTitle?.text = it.data.tokopointsStatusFiltered.statusFilteredData.tier.nameDesc
                    memberIcon?.show()
                    memberIcon?.setImageUrl(it.data.tokopointsStatusFiltered.statusFilteredData.tier.imageURL)

                    val mappedMember = mapper.mapMemberItemDataView(it.data)
                    memberAdapter?.addItems(mappedMember)
                    memberAdapter?.notifyDataSetChanged()
                    adapter?.notifyDataSetChanged()
                }

                is Fail -> {
                    memberCardView?.hide()
                    memberLocalLoad?.show()
                }
            }
        })

        viewModel.userPageAssetConfig.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetUserPageAssetConfig(it.data)
                }
                is Fail -> {
                    onFailedGetUserPageAssetConfig()
                }
            }
        })

        viewModel.tokopointsDrawerList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetTokopointsDrawerList(it.data)
                }
                is Fail -> {
                    onFailedGetTokopointsDrawerList()
                }
            }
        })

        viewModel.saldoBalance.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetSaldoBalance(it.data)
                }
                is Fail -> {
                    onFailedGetSaldoBalance()
                }
            }
        })
    }

    private fun onSuccessGetUserPageAssetConfig(userPageAssetConfig: UserPageAssetConfig) {
        financialAdapter?.removeByType(ErrorFinancialViewHolder.ERROR_TYPE)
        var ovoUserPageAssetConfigData = UserPageAssetConfigData()
        var tokopointUserPageAssetConfigData = UserPageAssetConfigData()
        userPageAssetConfig.userPageAssetConfig.forEach { userPageAssetConfigData ->
            when (userPageAssetConfigData.assetType) {
                TOKOPOINT_ASSET_TYPE -> {
                    tokopointUserPageAssetConfigData = userPageAssetConfigData
                }
                OVO_ASSET_TYPE -> {
                    ovoUserPageAssetConfigData = userPageAssetConfigData
                }
            }
        }
        when {
            tokopointUserPageAssetConfigData.enable -> {
                if (tokopointUserPageAssetConfigData.order > ovoUserPageAssetConfigData.order) {
                    viewModel.getTokopoints()
                } else {
                    viewModel.getOvoBalance()
                }
            }
            else -> {
                viewModel.getOvoBalance()
            }
        }
        viewModel.getSaldoBalance()
    }

    private fun onFailedGetUserPageAssetConfig() {
        ErrorFinancialViewHolder.ERROR_TYPE
        financialAdapter?.showError()
        adapter?.notifyDataSetChanged()
    }

    private fun onSuccessGetTokopointsDrawerList(tokopointsDrawerList: TokopointsDrawerList) {
        financialAdapter?.removeByType(ErrorFinancialItemViewHolder.TYPE_ERROR_TOKOPOINTS)
        val mappedData = mapper.mapTokopoints(tokopointsDrawerList)
        financialAdapter?.addSingleItem(mappedData)
        adapter?.notifyDataSetChanged()
    }

    private fun onFailedGetTokopointsDrawerList() {
        context?.let {
            financialAdapter?.removeByType(ErrorFinancialItemViewHolder.TYPE_ERROR_TOKOPOINTS)
            val mappedData = mapper.mapError(it, ErrorFinancialItemViewHolder.TYPE_ERROR_TOKOPOINTS, R.drawable.ic_account_tokopoint)
            financialAdapter?.addSingleItem(mappedData)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun onSuccessGetSaldoBalance(balance: Balance) {
        context?.let {
            financialAdapter?.removeByType(ErrorFinancialItemViewHolder.TYPE_ERROR_SALDO)
            val mappedData = mapper.mapSaldo(it, balance)
            financialAdapter?.addSingleItem(mappedData)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun onFailedGetSaldoBalance() {
        context?.let {
            financialAdapter?.removeByType(ErrorFinancialItemViewHolder.TYPE_ERROR_SALDO)
            val mappedData = mapper.mapError(it, ErrorFinancialItemViewHolder.TYPE_ERROR_SALDO, R.drawable.ic_account_balance)
            financialAdapter?.addSingleItem(mappedData)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun onSuccessGetOvoBalance(walletModel: WalletModel) {
        context?.let {
            financialAdapter?.removeByType(ErrorFinancialItemViewHolder.TYPE_ERROR_OVO)
            val mappedMember = mapper.mapToFinancialData(it, walletModel)
            financialAdapter?.addSingleItem(mappedMember)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun onFailedGetOvoBalance() {
        context?.let {
            financialAdapter?.removeByType(ErrorFinancialItemViewHolder.TYPE_ERROR_OVO)
            val mappedData = mapper.mapError(it, ErrorFinancialItemViewHolder.TYPE_ERROR_OVO, R.drawable.ic_account_ovo)
            financialAdapter?.addSingleItem(mappedData)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onMemberErrorClicked() {
        viewModel.getShortcutData()
    }

    override fun onFinancialErrorClicked(type: Int) {
        when (type) {
            ErrorFinancialItemViewHolder.TYPE_ERROR_TOKOPOINTS -> {
                viewModel.getTokopoints()
            }
            ErrorFinancialItemViewHolder.TYPE_ERROR_OVO -> {
                viewModel.getOvoBalance()
            }
            ErrorFinancialItemViewHolder.TYPE_ERROR_SALDO -> {
                viewModel.getSaldoBalance()
            }
            ErrorFinancialViewHolder.ERROR_TYPE -> {
                viewModel.getUserPageAssetConfig()
            }
        }
    }

    private fun onFailGetData() {
        memberCardView?.hide()
        memberLocalLoad?.show()
        adapter?.run {
            if (getItem(0) is ProfileDataView) {
                removeItemAt(0)
            }
            addItem(0, ProfileDataView(
                    name = userSession.name,
                    phone = userSession.phoneNumber,
                    email = userSession.email,
                    avatar = userSession.profilePicture
            ))
            notifyDataSetChanged()
        }
        hideLoading()
        fpmBuyer?.run { stopTrace() }
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = status_bar_bg.background
        drawable.alpha = alpha.toInt()
        status_bar_bg.background = drawable
    }

    private fun onSuccessGetBuyerAccount(buyerAccount: UserAccountDataModel) {
        memberLocalLoad?.hide()
        memberCardView?.show()
        adapter?.run {
            if (getItem(0) is ProfileDataView) {
                removeItemAt(0)
            }
            addItem(0, mapper.mapToProfileDataView(buyerAccount))
            notifyDataSetChanged()
        }
        hideLoading()
        fpmBuyer?.run { stopTrace() }
    }

    private fun onSuccessGetFirstRecommendationData(recommendation: RecommendationWidget) {
        widgetTitle = recommendation.title
        addItem(RecommendationTitleView(widgetTitle), addSeparator = false)
        addTopAdsHeadLine()
        adapter?.notifyDataSetChanged()
        addRecommendationItem(recommendation.recommendationItemList)
    }

    private fun addTopAdsHeadLine() {
        addItem(TopadsHeadlineUiModel(), addSeparator = false)
    }

    private fun addRecommendationItem(list: List<RecommendationItem>) {
        for (item in list) {
            adapter?.addItem(item)
        }
        adapter?.notifyDataSetChanged()
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun getFirstRecommendation() {
        showLoadMoreLoading()
        viewModel.getFirstRecommendation()
    }

    private fun showLoading() {
        home_account_shimmer_layout?.show()
    }

    private fun hideLoading() {
        home_account_shimmer_layout?.hide()
    }

    private fun setupStatusBar() {
        activity?.let {
            status_bar_bg.background = ColorDrawable(
                    ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            )
        }
        status_bar_bg.layoutParams.height = ViewHelper.getStatusBarHeight(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            status_bar_bg.visibility = View.INVISIBLE
        } else {
            status_bar_bg.visibility = View.VISIBLE
        }
        setStatusBarAlpha(0f)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_account_user_toolbar?.let {
            it.setIcon(IconBuilder(
                    IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_ACCOUNT)
            ).addIcon(iconId = IconList.ID_NAV_GLOBAL) {})
            viewLifecycleOwner.lifecycle.addObserver(it)
        }

        setupStatusBar()
        setupObserver()

        context?.run {
            coachMark = CoachMark2(this)
        }

        financialAdapter = HomeAccountFinancialAdapter(this)
        memberAdapter = HomeAccountMemberAdapter(this)

        adapter = HomeAccountUserAdapter(this, financialAdapter, memberAdapter, userSession)
        setupList()
        setLoadMore()
        showLoading()
        getData()

        home_account_user_fragment_rv?.addOnScrollListener(NavRecyclerViewScrollListener(
                navToolbar = home_account_user_toolbar,
                startTransitionPixel = 200,
                toolbarTransitionRangePixel = 50,
                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                    override fun onAlphaChanged(offsetAlpha: Float) {
                        setStatusBarAlpha(offsetAlpha)
                    }

                    override fun onSwitchToDarkToolbar() {
                        home_account_user_toolbar.switchToLightToolbar()
                    }

                    override fun onSwitchToLightToolbar() {
                    }

                    override fun onYposChanged(yOffset: Int) {
                    }
                }
        ))

        home_account_user_fragment_rv?.swipeLayout = home_account_user_fragment_swipe_refresh
        home_account_user_fragment_swipe_refresh?.setOnRefreshListener {
            coachMark?.dismissCoachMark()
            onRefresh()
            getData()
            isNeedRefreshProfileItems = true
            home_account_user_fragment_swipe_refresh?.isRefreshing = false
        }
    }

    private fun getProfileData() {
        if (showHomeAccountTokopoints()) {
            viewModel.getUserPageAssetConfig()
        } else {
            viewModel.getSaldoBalance()
        }
        viewModel.getShortcutData()
    }

    private fun showHomeAccountTokopoints(): Boolean {
        return isShowHomeAccountTokopoints
    }

    private fun getData() {
        home_account_user_fragment_rv?.scrollToPosition(0)
        endlessRecyclerViewScrollListener?.resetState()
        viewModel.getBuyerData()
        setupSettingList()
        getFirstRecommendation()
    }

    private fun onRefresh() {
        showLoading()
        adapter?.clearAllItems()
        financialAdapter?.list?.clear()
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
        home_account_user_fragment_rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        home_account_user_fragment_rv?.adapter = adapter
        home_account_user_fragment_rv?.isNestedScrollingEnabled = false
    }

    private fun setupSettingList() {
        addItem(menuGenerator.generateUserSettingMenu(), addSeparator = true)
        addItem(menuGenerator.generateApplicationSettingMenu(accountPref, permissionChecker), addSeparator = true)
        addItem(menuGenerator.generateAboutTokopediaSettingMenu(), addSeparator = true)
        if (GlobalConfig.isAllowDebuggingTools()) {
            addItem(menuGenerator.generateDeveloperOptionsSettingMenu(), addSeparator = true)
        }
        addItem(SettingDataView("", arrayListOf(
                CommonDataView(id = AccountConstants.SettingCode.SETTING_OUT_ID, title = getString(R.string.menu_account_title_sign_out), body = "", type = CommonViewHolder.TYPE_WITHOUT_BODY, icon = IconUnify.SIGN_OUT, endText = "Versi ${GlobalConfig.VERSION_NAME}")
        ), isExpanded = true), addSeparator = true)
        adapter?.notifyDataSetChanged()
    }

    private fun addItem(item: Any, addSeparator: Boolean, position: Int = -1) {
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
        adapter?.notifyDataSetChanged()
    }

    override fun onProfileClicked() {
        homeAccountAnalytic.eventClickProfile()
    }

    override fun onIconWarningClicked(profile: ProfileDataView) {
        showBottomSheetAddName(profile)
    }

    override fun onEditProfileClicked() {
        homeAccountAnalytic.eventClickProfile()
        goToApplink(ApplinkConstInternalGlobal.SETTING_PROFILE)
    }

    private fun goToApplink(applink: String) {
        if (applink.isNotEmpty()) {
            val intent = RouteManager.getIntent(context, applink)
            startActivity(intent)
        }
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
                if (isActive) {
                    createAndShowSafeModeAlertDialog(isActive)
                }
            }
            else -> {
            }
        }
    }

    private fun createAndShowSafeModeAlertDialog(currentValue: Boolean) {
        var dialogTitleMsg = getString(R.string.new_home_account_safe_mode_selected_dialog_title)
        var dialogBodyMsg = getString(R.string.new_home_account_safe_mode_selected_dialog_msg)
        var dialogPositiveButton = getString(R.string.new_home_account_safe_mode_selected_dialog_positive_button)
        val dialogNegativeButton = getString(R.string.new_home_account_label_cancel)

        if (currentValue) {
            dialogTitleMsg = getString(R.string.new_home_account_safe_mode_unselected_dialog_title)
            dialogBodyMsg = getString(R.string.new_home_account_safe_mode_unselected_dialog_msg)
            dialogPositiveButton = getString(R.string.new_home_account_safe_mode_unselected_dialog_positive_button)
        }

        context?.run {
            val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.apply {
                setTitle(dialogTitleMsg)
                setDescription(dialogBodyMsg)
                setPrimaryCTAText(dialogPositiveButton)
                setPrimaryCTAClickListener {
                    viewModel.setSafeMode(currentValue)
                }
                setSecondaryCTAText(dialogNegativeButton)
                setSecondaryCTAClickListener { dismiss() }
            }
        }

    }


    private fun mapSettingId(item: CommonDataView) {
        when (item.id) {
            AccountConstants.SettingCode.SETTING_OVO -> {
                homeAccountAnalytic.eventViewOvoHomepage()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_SALDO -> {
                homeAccountAnalytic.eventClickBalance()
                goToApplink(item.applink)
            }
            AccountConstants.SettingCode.SETTING_TOKOPOINTS -> {
                homeAccountAnalytic.eventClickTokopoints()
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
                if (userSession.hasPassword()) {
                    goToApplink(item.applink)
                } else {
                    showNoPasswordDialog()
                }
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
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_TERM_CONDITION)
            }

            AccountConstants.SettingCode.SETTING_ABOUT_US -> {
                homeAccountAnalytic.eventClickSetting(ABOUT_US)
                homeAccountAnalytic.eventClickGetToKnowAboutTokopedia()
                RouteManager.getIntent(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK
                        + AccountConstants.Url.BASE_MOBILE
                        + AccountConstants.Url.PATH_ABOUT_US).run {
                    startActivity(this)
                }
            }

            AccountConstants.SettingCode.SETTING_IP -> {
                homeAccountAnalytic.eventClickIpAboutTokopedia()
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_IP)
            }

            AccountConstants.SettingCode.SETTING_PRIVACY_ID -> {
                homeAccountAnalytic.eventClickSetting(PRIVACY_POLICY)
                homeAccountAnalytic.eventClickPrivacyPolicyAboutTokopedia()
                RouteManager.route(activity, AccountConstants.Url.BASE_WEBVIEW_APPLINK + AccountConstants.Url.BASE_MOBILE + AccountConstants.Url.PATH_PRIVACY_POLICY)
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
            AccountConstants.SettingCode.SETTING_OLD_ACCOUNT -> if (GlobalConfig.isAllowDebuggingTools()) {
                RouteManager.route(activity, ApplinkConstInternalGlobal.OLD_HOME_ACCOUNT)
            }
            AccountConstants.SettingCode.SETTING_OUT_ID -> {
                homeAccountAnalytic.eventClickSetting(LOGOUT)
                homeAccountAnalytic.eventClickLogout()
                showDialogLogout()
            }
            AccountConstants.SettingCode.SETTING_QUALITY_SETTING -> {
                RouteManager.route(context, ApplinkConstInternalGlobal.MEDIA_QUALITY_SETTING)
            }
            AccountConstants.SettingCode.SETTING_APP_ADVANCED_CLEAR_CACHE -> {
                homeAccountAnalytic.eventClickAppSettingCleanCache()
                showDialogClearCache()
            }
            AccountConstants.SettingCode.SETTING_SECURITY -> {
                homeAccountAnalytic.eventClickAccountSettingAccountSecurity()
                val intent = RouteManager.getIntent(context, item.applink).apply {
                    putExtras(Bundle().apply {
                        putExtra(ApplinkConstInternalGlobal.PARAM_NEW_HOME_ACCOUNT, true)
                    })
                }
                startActivity(intent)
            }

            else -> {
                goToApplink(item.applink)
            }
        }
    }

    private fun doLogout() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.LOGOUT))
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
            val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            888 -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationSwitch(true)
                } else {
                    goToApplicationDetailActivity()
                }
            }
        }
    }

    private fun askPermissionLocation() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                888)
    }

    private fun updateLocationSwitch(isEnable: Boolean) {
        commonAdapter?.list?.find { it.id == AccountConstants.SettingCode.SETTING_GEOLOCATION_ID }?.isChecked = isEnable
        commonAdapter?.notifyDataSetChanged()
        adapter?.notifyDataSetChanged()
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
                it.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(AccountConstants.Url.PLAYSTORE_URL + it.application.packageName)))
            }
        }
    }

    private fun showNoPasswordDialog() {
        activity?.run {
            val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(getString(R.string.error_bank_no_password_title))
            dialog.setDescription(getString(R.string.error_bank_no_password_content))
            dialog.setPrimaryCTAText(getString(R.string.error_no_password_yes))
            dialog.setPrimaryCTAClickListener {
                intentToAddPassword()
                dialog.dismiss()
            }
            dialog.setSecondaryCTAText(getString(R.string.error_no_password_no))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun intentToAddPassword() {
        if (activity != null) {
            startActivityForResult(RouteManager.getIntent(activity,
                    ApplinkConstInternalGlobal.ADD_PASSWORD), AccountConstants.REQUEST.REQUEST_ADD_PASSWORD)
        }
    }

    override fun onProfileAdapterReady(financialAdapter: HomeAccountFinancialAdapter, memberAdapter: HomeAccountMemberAdapter) {
        this.financialAdapter = financialAdapter
        this.memberAdapter = memberAdapter
        if (isNeedRefreshProfileItems) {
            getProfileData()
        }
        isNeedRefreshProfileItems = false
    }

    override fun onItemViewBinded(position: Int, itemView: View, data: Any) {
        initCoachMark(position, itemView, data)
        if (position == 0) {
            initMemberLocalLoad(itemView)
            initMemberTitle(itemView)
        }
    }

    private fun initCoachMark(position: Int, itemView: View, data: Any) {
        if (accountPref.isShowCoachmark()) {
            if (!isProfileSectionBinded) {
                if (coachMarkItem.count() < 3) {
                    if (position == 0 && data is ProfileDataView) {
                        coachMarkItem.add(
                                CoachMark2Item(
                                        itemView.findViewById(R.id.account_user_item_profile_email),
                                        getString(R.string.coachmark_title_account_info),
                                        getString(R.string.coachmark_desc_account_info),
                                        CoachMark2.POSITION_BOTTOM
                                )
                        )
                    }
                    if (position == 1 && data is SettingDataView) {
                        coachMarkItem.add(
                                CoachMark2Item(
                                        itemView.findViewById(R.id.home_account_expandable_layout_title),
                                        getString(R.string.coachmark_title_setting),
                                        getString(R.string.coachmark_desc_setting),
                                        CoachMark2.POSITION_TOP
                                )
                        )
                        showCoachMark()
                    }
                } else {
                    showCoachMark()
                }
            }
        }
    }

    private fun showCoachMark() {
        context?.run {
            coachMark?.onFinishListener = {
                accountPref.saveSettingValue(AccountConstants.KEY.KEY_SHOW_COACHMARK, false)
            }
            coachMark?.showCoachMark(coachMarkItem)
            isProfileSectionBinded = true
        }
    }

    private fun initMemberLocalLoad(itemView: View) {
        itemView.findViewById<LocalLoad>(R.id.home_account_local_load)?.let {
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

    private fun initMemberTitle(itemView: View) {
        itemView.findViewById<Typography>(R.id.home_account_member_layout_title)?.let {
            memberTitle = it
        }

        itemView.findViewById<ImageUnify>(R.id.home_account_member_layout_member_icon)?.let {
            memberIcon = it
        }
    }

    private fun setLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            val layoutManager = home_account_user_fragment_rv?.layoutManager
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
            home_account_user_fragment_rv?.addOnScrollListener(it)
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

    override fun onProductRecommendationImpression(item: RecommendationItem, adapterPosition: Int) {
        trackingQueue?.let {
            homeAccountAnalytic.eventAccountProductView(it, item, adapterPosition)
        }
        activity?.let {
            if (item.isTopAds) {
                TopAdsUrlHitter(it)
                        .hitImpressionUrl(it::class.qualifiedName,
                                item.trackerImageUrl,
                                item.productId.toString(),
                                item.name,
                                item.imageUrl,
                                COMPONENT_NAME_TOP_ADS)
            }
        }
    }

    override fun onProductRecommendationClicked(item: RecommendationItem, adapterPosition: Int) {
        homeAccountAnalytic.eventAccountProductClick(item, adapterPosition, widgetTitle)
        activity?.let {
            if (item.isTopAds) {
                TopAdsUrlHitter(it).hitClickUrl(it::class.qualifiedName,
                        item.clickUrl,
                        item.productId.toString(),
                        item.name,
                        item.imageUrl,
                        COMPONENT_NAME_TOP_ADS)
            }
        }

        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, adapterPosition)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    override fun onProductRecommendationThreeDotsClicked(item: RecommendationItem, adapterPosition: Int) {
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = item.isWishlist,
                        productId = item.productId.toString(),
                        isTopAds = item.isTopAds,
                        topAdsWishlistUrl = item.wishlistUrl,
                        productPosition = adapterPosition
                )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
        }

        if (requestCode == REQUEST_CODE_CHANGE_NAME && resultCode == Activity.RESULT_OK) {
            gotoSettingProfile()
        }

        if (requestCode == REQUEST_CODE_PROFILE_SETTING) {
            getData()
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })
    }

    private fun updateWishlist(wishlistStatusFromPdp: Boolean, position: Int) {
        adapter?.let {
            if (it.getItems()[position] is RecommendationItem) {
                (it.getItems()[position] as RecommendationItem).isWishlist = wishlistStatusFromPdp
                it.notifyItemChanged(position)
            }
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        homeAccountAnalytic.eventClickWishlistButton(!productCardOptionsModel.isWishlisted)
        if (productCardOptionsModel.wishlistResult.isSuccess)
            handleWishlistActionSuccess(productCardOptionsModel)
        else
            handleWishlistActionError()
    }

    private fun handleWishlistActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        val recommendationItem = adapter?.getItems()?.getOrNull(productCardOptionsModel.productPosition) as? RecommendationItem
                ?: return
        recommendationItem.isWishlist = productCardOptionsModel.wishlistResult.isAddWishlist

        if (productCardOptionsModel.wishlistResult.isAddWishlist)
            showSuccessAddWishlist()
        else
            showSuccessRemoveWishlist()
    }

    private fun showSuccessAddWishlist() {
        view?.let {
            Toaster.make(
                    it,
                    getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.new_home_account_go_to_wishlist),
                    setOnClickSuccessAddWishlist())
        }
    }

    private fun setOnClickSuccessAddWishlist(): View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(activity, ApplinkConst.WISHLIST)
        }
    }

    private fun showBottomSheetAddName(profile: ProfileDataView) {
        activity?.let {
            val addNameLayout = View.inflate(context, R.layout.layout_bottom_sheet_add_name, null)
            val btnAddName: UnifyButton = addNameLayout.findViewById(R.id.layout_bottom_sheet_add_name_button)
            val iconAddName: ImageUnify = addNameLayout.findViewById(R.id.layout_bottom_sheet_add_name_icon)
            val bottomSheet = BottomSheetUnify()

            ImageUtils.loadImage(iconAddName, URL_ICON_ADD_NAME_BOTTOM_SHEET)
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

    private fun gotoChangeName(profile: ProfileDataView) {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.CHANGE_NAME, profile.name, "")
        startActivityForResult(intent, REQUEST_CODE_CHANGE_NAME)
    }

    private fun gotoSettingProfile() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.SETTING_PROFILE)
        startActivityForResult(intent, REQUEST_CODE_PROFILE_SETTING)
    }

    private fun showSuccessRemoveWishlist() {
        view?.let {
            Toaster.make(
                    it,
                    getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL
            )
        }
    }

    private fun handleWishlistActionError() {
        view?.let {
            Toaster.make(
                    it,
                    ErrorHandler.getErrorMessage(activity, null),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR)
        }
    }

    private fun showErrorNoConnection() {
        if (view != null && userVisibleHint) {
            view?.let {
                Toaster.make(it, getString(R.string.new_home_account_error_no_internet_connection), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                        getString(R.string.title_try_again), setOnClickErrorNoConnection())
            }
        }
        fpmBuyer?.run { stopTrace() }
    }

    private fun setOnClickErrorNoConnection(): View.OnClickListener {
        return View.OnClickListener { getData() }
    }

    override fun onCommonAdapterReady(position: Int, commonAdapter: HomeAccountUserCommonAdapter) {
        if (position == 2)
            this.commonAdapter = commonAdapter
    }

    companion object {
        private const val REQUEST_CODE_CHANGE_NAME = 300
        private const val REQUEST_CODE_PROFILE_SETTING = 301

        private const val COMPONENT_NAME_TOP_ADS = "Account Home Recommendation Top Ads"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val REQUEST_FROM_PDP = 394
        private val FPM_BUYER = "mp_account_buyer"
        private const val URL_ICON_ADD_NAME_BOTTOM_SHEET = "https://images.tokopedia.net/img/android/user/profile_page/Group3082@3x.png"

        private const val OVO_ASSET_TYPE = "ovo"
        private const val TOKOPOINT_ASSET_TYPE = "tokopoint"
        private const val REMOTE_CONFIG_KEY_HOME_ACCOUNT_TOKOPOINTS = "android_user_home_account_tokopoints"

        fun newInstance(bundle: Bundle?): Fragment {
            return HomeAccountUserFragment().apply {
                arguments = bundle
            }
        }
    }
}